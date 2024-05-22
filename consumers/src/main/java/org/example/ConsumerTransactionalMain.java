package org.example;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerTransactionalMain {
    private static final Logger log = LoggerFactory.getLogger(ConsumerTransactionalMain.class);

    public static void main(String[] args) {

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getProperties())) {
            consumer.subscribe(List.of("devs4j-topic"));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMinutes(10)); // since messages time before
                for (ConsumerRecord<String, String> consumerRecord : records)
                    log.info("offset = {}, partition  = {}, key = {}, value ={}",
                            consumerRecord.offset(),
                            consumerRecord.partition(),
                            consumerRecord.key(), consumerRecord.value());
            }
        }
    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "devs4j-group");
        props.setProperty("isolation.level",
                "read_committed");// Required for transactional consumer, by default is unread_committed
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

}