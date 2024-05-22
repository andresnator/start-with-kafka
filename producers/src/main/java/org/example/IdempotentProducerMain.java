package org.example;

import java.time.Instant;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * 'idempotence' This problem occurs, for example, when Kafka does not send the message with acknowledgment to producer for some reason.
 * What does 'acknowledgment' (acks) mean? It's a confirmation from Kafka to inform the producer that it has successfully received the message.
 */
public class IdempotentProducerMain {

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("retries", "10"); // Required > 0
        props.put("acks", "all"); // Required 'all'
        props.put("max.in.flight.requests.per.connection", "1"); // Required <= 5
        props.put("enable.idempotence", "true"); // Required <= 5
        props.put("bootstrap.servers", "localhost:9092");
        props.put("linger.ms", "10");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        final int cantMessages = 10000;

        var key = Instant.now().toEpochMilli();

        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            for (int i = 0; i < cantMessages; i++) {
                producer.send(new ProducerRecord<>("devs4j-topic", "key-idempotence-" + key, String.valueOf("true")));
            }
            producer.flush();
        }

    }
}