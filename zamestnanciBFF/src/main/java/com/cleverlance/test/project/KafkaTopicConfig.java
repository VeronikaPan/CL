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
}
