package org.example.thread;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiThreadConsumer extends Thread {
    private final KafkaConsumer<String, String> consumer;

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public static final Logger log = LoggerFactory.getLogger(MultiThreadConsumer.class);

    public MultiThreadConsumer(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(List.of("devs4j-topic"));
            while (!closed.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
                for (ConsumerRecord<String, String> consumerRecord : records) {
                    log.info("offset = {}, partition  = {}, key = {}, value ={}", consumerRecord.offset(),
                            consumerRecord.partition(),
                            consumerRecord.key(), consumerRecord.value());
                }
            }
        } catch (WakeupException e) {
            if (!closed.get())
                throw e;
        } finally {
            consumer.close();
        }
    }

    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
}
