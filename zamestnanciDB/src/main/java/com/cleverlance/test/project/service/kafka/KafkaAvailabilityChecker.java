package com.cleverlance.test.project.service.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaAvailabilityChecker {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    public boolean isKafkaBrokerAvailable() {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        AdminClient adminClient = KafkaAdminClient.create(properties);

        DescribeClusterOptions options = new DescribeClusterOptions().timeoutMs(5000);
        DescribeClusterResult result = adminClient.describeCluster(options);

        try {
            result.nodes().get(); // Pokud nenastane žádná výjimka, Kafka broker je dostupný
            return true;
        } catch (InterruptedException | ExecutionException e) {
            // Výjimka, Kafka broker není dostupný
            return false;
        } finally {
            adminClient.close();
        }
    }
}
