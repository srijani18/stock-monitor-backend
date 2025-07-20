package com.stock.demo.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.demo.model.Stock;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
//public class KafkaConsumerService {
//
//    @KafkaListener(topics = "stock-prices", groupId = "stock-group")
//    public void listen(ConsumerRecord<String, String> record) {
//        System.out.println("Received message: " + record.value());
//    }
//}

@Component
public class KafkaConsumerService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaConsumerService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "stock-prices", groupId = "stock-group")
    public void consume(String message) throws JsonProcessingException {
        Stock stock = objectMapper.readValue(message, Stock.class);
        messagingTemplate.convertAndSend("/topic/live-stocks", List.of(stock));
    }
}
