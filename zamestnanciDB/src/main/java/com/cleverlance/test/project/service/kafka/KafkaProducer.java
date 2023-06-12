package com.cleverlance.test.project.service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Value("${spring.kafka.topic-add-response.name}")
    private String addResponsetopicName;
    
    @Value("${spring.kafka.topic-delete-response.name}")
    private String deleteResponsetopicName;
    
    @Value("${spring.kafka.topic-update-response.name}")
    private String updateResponsetopicName;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAddResponse(String message){
        LOGGER.info(String.format("Message sent %s", message));
        kafkaTemplate.send(addResponsetopicName, message);
    }
    
    public void sendDeleteResponse(String message){
        LOGGER.info(String.format("Message sent %s", message));
        kafkaTemplate.send(deleteResponsetopicName, message);
    }
    
    public void sendUpdateResponse(String message){
        LOGGER.info(String.format("Message sent %s", message));
        kafkaTemplate.send(updateResponsetopicName, message);
    }
}
