package org.example;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In assign and seek you could to choose the spècific partition's number and from what offset's number you get the messages
 */
public class ConsumerAutoCommitMain {

    private static final Logger log = LoggerFactory.getLogger(ConsumerAutoCommitMain.class);

    public static void main(final String[] args) {

        final Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "devs4j-group");
        props.setProperty("enable.auto.commit", "false"); // Required false
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        try (final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(List.of("devs4j-topic"));

            while (true) {
                final ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(10)); // since messages time before
                for (final ConsumerRecord<String, String> consumerRecord : records)
                    log.info("offset = {}, partition  = {}, key = {}, value ={}", consumerRecord.offset(),
                            consumerRecord.partition(),
                            consumerRecord.key(), consumerRecord.value());
                consumer.commitSync();
            }
        }

    }
}