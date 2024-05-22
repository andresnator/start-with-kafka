package org.example;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ProducerTransactionalMain {

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all"); // Required for transactional producer
        props.put("transactional.id", "devs4j-producer");// Required for transactional producer
        props.put("linger.ms", "10");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        final int cantMessages = 1000000;

        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            producer.initTransactions(); // Required for transactional producer
            producer.beginTransaction(); // Required for transactional producer

            for (int i = 0; i < cantMessages; i++) {
                producer.send(new ProducerRecord<>("devs4j-topic", "key-async" + i, String.valueOf(i)));
            }
            producer.commitTransaction(); //  Required for transactional producer
            //producer.abortTransaction();  //Optional for transactional producer (for example when failed you can to abort transaction
            producer.flush();
        }
    }
}