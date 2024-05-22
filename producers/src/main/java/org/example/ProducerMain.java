package org.example;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ProducerMain {
    /**
     * When you use the same key kafka use the same partition whe it's async mode
     */
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
                // this it's send ASYNC, this one is faster than SYNC
                producer.send(new ProducerRecord<>("devs4j-topic", "key-async" + i, String.valueOf(i)));

                // this it's send SYNC 'bad performance'
                // producer.send(new ProducerRecord<>("devs4j-topic", "key-sync" + i, String.valueOf(i))).get();

            }
            producer.flush();
        }

    }
}