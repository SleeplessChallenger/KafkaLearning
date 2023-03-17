package consumer;

import avro.Alert;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import producer.HelloWorldProducer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class HelloWorldConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldProducer.class);
    private volatile boolean keepConsuming = true;

    public static void main(String[] unused) {
        final Properties kafkaProperties = new Properties();
        kafkaProperties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
        kafkaProperties.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        kafkaProperties.put("schema.registry.url", "http://localhost:8081");

        HelloWorldConsumer helloWorldConsumer = new HelloWorldConsumer();
        helloWorldConsumer.consume(kafkaProperties);

        Runtime.getRuntime().addShutdownHook(new Thread(helloWorldConsumer::shutDown));
    }

    private void consume(Properties kafkaProperties) {
        try (KafkaConsumer<Long, Alert> consumer = new KafkaConsumer<>(kafkaProperties)) {
            consumer.subscribe(List.of("kinaction_schematest"));

            while (this.keepConsuming) {
                ConsumerRecords<Long, Alert> records = consumer.poll(Duration.ofMillis(250));

                records.forEach(record -> {
                    LOG.info("Start consuming record from kinaction_schematest. Offset = {}, vaue = {}",
                            record.offset(), record.value());
                });
            }
        }
    }

    private void shutDown() {
        this.keepConsuming = false;
    }
}
