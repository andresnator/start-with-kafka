package com.kafka.spring;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GenericConsumer {

    public static final String TOPIC = "devs4j-topic";

    public static final String GROUP = "devs4j-group";

    // @KafkaListener(topics = TOPIC, groupId = GROUP)
    public void consume(final ConsumerRecord<String, Object> payload) {
        log.info(payload.toString());
    }

    /**
     * 'max.poll.interval.ms' range of messages in period of time .
     * 'max.poll.records' max messages for period allow.
     * 'autoStartup' means that Listener not start util do it manual, by default it's true.
     *
     * @param payload .
     */
    @KafkaListener(id = "consumeBatchesId", autoStartup = "true", topics = TOPIC, containerFactory = "listenerContainerFactory", groupId = GROUP, properties =
            { "max.poll.interval.ms:600", "max.poll.records:10" })
    public void consumeBatches(final List<ConsumerRecord<String, String>> payload) {
        //log.info("Start cant {}", payload.size());
        for (final var batch : payload) {
            //log.info("Partition {}", batch.partition());
            //log.info("Message {}", batch.value());
        }

    }
}


