package com.cleverlance.test.project;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

	@Value("${spring.kafka.topic-add.name}")
	private String addEmployeeTopic;
	@Value("${spring.kafka.topic-update.name}")
	private String updateEmployeeTopic;
	@Value("${spring.kafka.topic-delete.name}")
	private String deleteEmployeeTopic;
	@Value("${spring.kafka.topic-add-response.name}")
	private String addEmployeeResponseTopic;
	@Value("${spring.kafka.topic-delete-response.name}")
	private String deleteEmployeeResponseTopic;
	@Value("${spring.kafka.topic-update-response.name}")
	private String updateEmployeeResponseTopic;
	
	@Bean
	public NewTopic addEmployeeTopic() {
		return TopicBuilder.name(addEmployeeTopic).build();
	}

	@Bean
	public NewTopic updateEmployeeTopic() {
		return TopicBuilder.name(updateEmployeeTopic).build();
	}

	@Bean
	public NewTopic deleteEmployeeTopic() {
		return TopicBuilder.name(deleteEmployeeTopic).build();
	}
	
	@Bean
	public NewTopic addEmployeeResponseTopic() {
	    return TopicBuilder.name("addEmployeeResponseTopic").build();
	}
	
	@Bean
	public NewTopic deleteEmployeeResponseTopic() {
	    return TopicBuilder.name("deleteEmployeeResponseTopic").build();
	}
	
	@Bean
	public NewTopic updateEmployeeResponseTopic() {
	    return TopicBuilder.name("updateEmployeeResponseTopic").build();
	}
}
