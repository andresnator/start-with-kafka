package org.example;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In assign and seek you could to choose the spècific partion's number and from what offset's number you get the messages
 */
public class ConsumerSeekMain {

    private static final Logger log = LoggerFactory.getLogger(ConsumerSeekMain.class);

    public static void main(final String[] args) {

        final Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "devs4j-group");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        try (final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {

            final TopicPartition topicPartition =
                    new TopicPartition("devs4j-topic", 0); // Here you can select the specific partition's number
            consumer.assign(List.of(topicPartition));
            consumer.seek(topicPartition, 50); // For the test it's necessary to reset topic

            while (true) {
                final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (final ConsumerRecord<String, String> consumerRecord : records)
                    log.info("offset = {}, partition  = {}, key = {}, value ={}, {}", consumerRecord.offset(),
                            consumerRecord.partition(),
                            consumerRecord.key(), consumerRecord.value(), consumerRecord.timestamp());
            }
        }
    }
}