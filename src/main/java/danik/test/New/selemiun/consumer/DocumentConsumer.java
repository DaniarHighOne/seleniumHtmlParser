package danik.test.New.selemiun.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import danik.test.New.selemiun.dto.MsgDTO;
import danik.test.New.selemiun.service.impl.HtmlUnitImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DocumentConsumer {

    private final MessageProcessorService messageProcessorService;

    @RabbitListener(bindings =
            @QueueBinding(
                    exchange = @Exchange(value = "${spring.rabbitmq.exchange}"),
                    key = {"${spring.rabbitmq.routing-key}"},
                    value = @Queue(name= "${spring.rabbitmq.queue}", durable = "true")
            ))

    public void consume(Message message) {
        try {

            //parse rabbitmq input messages
            MsgDTO messageDTO = messageProcessorService.parseMessage(message);

            log.info("received document for parsing: {}", messageDTO.toString());

            MsgDTO proccesedMessage = this.messageProcessorService.processMessage(messageDTO);
            this.messageProcessorService.sendMessage(proccesedMessage);
            log.info("Send document for parsing and processing: {}", proccesedMessage.toString());


        } catch (Exception ex) {
            log.error("Error while sending message: %s".formatted(message), ex);
            //throw new AmqpRejectAndDontRequeueException(ex);
        }
    }


}
