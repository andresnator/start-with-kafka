package com.kafka.spring;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * CommandLineRunner implement 'run' this method run when the application start.
 */
@Component
@Slf4j
public class GenericProducer implements CommandLineRunner {
    public static final String TOPIC = "devs4j-topic";

    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @Autowired
    private MeterRegistry meterRegistry;

    @Override
    public void run(final String... args) throws ExecutionException, InterruptedException, TimeoutException {

    }

    /**
     * fixedDelay: Each period time it'll execute.
     * initialDelay: Period time the schedule start after application is running.
     */
    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void scheduleProducer() {
        for (int i = 0; i < 100; i++) {
            final var future = this.kafkaTemplate.send(TOPIC, "sample message number: " + i);
            future.whenComplete((meta, error) -> {
                if (error == null) {
                    this.meterRegistry.counter("kafka.messages.sent", "topic", TOPIC);
                }
            });
        }
    }

    @Scheduled(fixedDelay = 3000, initialDelay = 1000)
    public void messageCountMetric() {

        try {

            for (final Meter meter : this.meterRegistry.getMeters()) {
                log.info("Metric {} ", meter.getId());
            }

            final var count = this.meterRegistry.get("kafka.messages.sent")
                    .functionCounter().count();

            log.info("Cant metrics send: " + count);
        } catch (final Exception ignored) {
        }

    }

    /**
     * Method with callback used for get metadata register in kafka.
     */
    @SneakyThrows
    private void asyncMethod() {

        for (int i = 0; i < 10000; i++) {
            final CompletableFuture<SendResult<Integer, String>> sendResultCompletableFuture =
                    this.kafkaTemplate.send(TOPIC, "sample message async number: " + i);

            sendResultCompletableFuture.whenComplete((result, ex) -> {
                log.info("Offset {}", result.getRecordMetadata().offset());
                log.info("Partition {}", result.getRecordMetadata().partition());
                log.info("Topic {}", result.getRecordMetadata().topic());
                log.info("Timestamp {}", result.getRecordMetadata().timestamp());
                log.info("Value {}", result.getProducerRecord().value());
            });

        }

        Thread.sleep(10000);
        // start consumer manual
        this.registry.getListenerContainer("consumeBatchesId").start();

    }

    /**
     * It's necessary to use '.get()' if you're using 'TimeUnit' and the message takes more time. Then, execute the TimeoutException exception.
     *
     * @throws ExecutionException   .
     * @throws InterruptedException .
     * @throws TimeoutException     .
     */
    private void syncMethod() throws ExecutionException, InterruptedException, TimeoutException {

        this.kafkaTemplate.send(TOPIC, "sample message sync").get(100, TimeUnit.MILLISECONDS);

    }
}

