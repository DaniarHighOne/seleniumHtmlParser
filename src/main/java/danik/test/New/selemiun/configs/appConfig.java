package danik.test.New.selemiun.configs;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import java.text.SimpleDateFormat;

@Configuration
public class appConfig {

    @Value("${spring.rabbitmq.output-exchange}")
    private String outputExchange;

    @Value("${spring.rabbitmq.output-routing-key}")
    private String outputKey;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"))
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {
        MappingJackson2MessageConverter mappingJackson2MessageConverter = new MappingJackson2MessageConverter();
        mappingJackson2MessageConverter.setObjectMapper(objectMapper());
        return mappingJackson2MessageConverter;
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(outputExchange);
        rabbitTemplate.setRoutingKey(outputKey);
        return rabbitTemplate;
    }


}
