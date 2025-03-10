package danik.test.New.selemiun.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import danik.test.New.selemiun.dto.MsgDTO;
import danik.test.New.selemiun.service.ProxyService;
import danik.test.New.selemiun.service.impl.HtmlUnitImpl;
import danik.test.New.selemiun.service.impl.SeleniumParsing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageProcessorService {

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final HtmlUnitImpl htmlUnitImpl;
    //private final SeleniumParsing seleniumParsing;
    protected final ProxyService proxyService;

    public MessageProcessorService(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate, HtmlUnitImpl htmlUnitImpl,
                                   ProxyService proxyService) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.htmlUnitImpl = htmlUnitImpl;
        this.proxyService = proxyService;
    }

    public MsgDTO parseMessage(Message message) throws Exception {
        return this.objectMapper.readValue(message.getBody(), MsgDTO.class);
    }

    public MsgDTO processMessage(MsgDTO messageDTO) throws Exception {

        try {
            ProxyService.ProxyHolder proxyHolder = proxyService.getAvailableProxy();
            String parsedHtml;
            if (proxyHolder != null) {
                parsedHtml = this.htmlUnitImpl.parseHtmlWithProxy(messageDTO.getUrl(), getHtmlPageStatusCode(), proxyService);
                log.info("Parsing page with HtmlUnit: {}", parsedHtml);
            } else {
                parsedHtml = this.htmlUnitImpl.parseHtml(messageDTO.getUrl(), getHtmlPageStatusCode());
            }

            return messageDTO.toBuilder()
                    .body(parsedHtml)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return messageDTO;
    }

    private int getHtmlPageStatusCode() {


    }

    public void sendMessage(MsgDTO messageDTO) throws Exception {
        Message outputMessage = new Message(this.objectMapper.writeValueAsBytes(messageDTO));
        this.rabbitTemplate.send(outputMessage);
    }
}

