package com.example.springintegration;

import com.example.springintegration.service.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@ImportResource("integration.xml")
public class SpringIntegrationApplication implements ApplicationRunner {

    private DirectChannel inputChannel;
    private DirectChannel outputChannel;

    @Autowired
    @Qualifier("inputChannel")
    public void setInputChannel(DirectChannel inputChannel) {
        this.inputChannel = inputChannel;
    }

    @Autowired
    @Qualifier("outputChannel")
    public void setOutputChannel(DirectChannel outputChannel) {
        this.outputChannel = outputChannel;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringIntegrationApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Not very easy way of creating messages
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        MessageHeaders headers = new MessageHeaders(map);
        Message<String> message = new GenericMessage<>("Hello world", headers);
        PrintService printService = new PrintService();
        printService.print(message);
        // An easier way to create messages.
        Message<String> anotherMessage = MessageBuilder.withPayload("Some payload").setHeader("newHeader",
                "newHeaderValue").build();
        inputChannel.send(anotherMessage);
        outputChannel.subscribe(message1 -> System.out.println(message.getPayload()));
        MessagingTemplate template = new MessagingTemplate();
    }
}
