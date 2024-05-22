package org.example;

import java.time.Instant;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class OrderProducerMain {

    private static final Logger log = LoggerFactory.getLogger(OrderProducerMain.class);

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("max.in.flight.requests.per.connection",
                "1"); // This is necessary to maintain the correct order due to the cause of retries
        props.put("linger.ms", "10");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        final int cantMessages = 1000000;

        var key = Instant.now().toEpochMilli();

        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            for (int i = 0; i < cantMessages; i++) {
                producer.send(new ProducerRecord<>("devs4j-topic", "key-async-" + key, String.valueOf(i)));
            }
            producer.flush();
        }

    }
}