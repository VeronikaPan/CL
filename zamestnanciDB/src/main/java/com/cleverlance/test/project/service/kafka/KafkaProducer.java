package com.cleverlance.test.project.service.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaProducer {

    @Value("${spring.kafka.topic-add-response.name}")
    private String addResponsetopicName;
    
    @Value("${spring.kafka.topic-delete-response.name}")
    private String deleteResponsetopicName;
    
    @Value("${spring.kafka.topic-update-response.name}")
    private String updateResponsetopicName;

    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAddResponse(String message){
        log.info(String.format("Message sent %s", message));
        kafkaTemplate.send(addResponsetopicName, message);
    }
    
    public void sendDeleteResponse(String message){
        log.info(String.format("Message sent %s", message));
        kafkaTemplate.send(deleteResponsetopicName, message);
    }
    
    public void sendUpdateResponse(String message){
        log.info(String.format("Message sent %s", message));
        kafkaTemplate.send(updateResponsetopicName, message);
    }
}
