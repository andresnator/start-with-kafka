package org.example;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class CallbackProducerMain {

    private static final Logger log = LoggerFactory.getLogger(CallbackProducerMain.class);

    public static void main(final String[] args) {

        final Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("linger.ms", "10");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        final int cantMessages = 10;

        try (final Producer<String, String> producer = new KafkaProducer<>(props)) {
            for (int i = 0; i < cantMessages; i++) {
                producer.send(new ProducerRecord<>("devs4j-topic", "key-async" + i, String.valueOf(i)), (metadata, error) -> {

                    if (error != null) {
                        log.error("Error send message: ", error);
                    } else {
                        log.info("offset = {}, partition  = {}, topic = {}", metadata.offset(),
                                metadata.partition(), metadata.topic());
                    }

                });
            }
            producer.flush();
        }

    }
}