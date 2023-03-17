package producer;

import avro.Alert;
import avro.AlertStatus;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Properties;

public class HelloWorldProducer {
    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldProducer.class);

    public static void main(String[] unused) {
        final Properties kafkaProperties = new Properties();
        kafkaProperties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        kafkaProperties.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        kafkaProperties.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        kafkaProperties.put("schema.registry.url", "http://localhost:8081");

        try (Producer<Long, Alert> producer = new KafkaProducer<>(kafkaProperties)) {
            final Alert alert = new Alert(12345L, Instant.now().toEpochMilli(), AlertStatus.Critical);

            LOG.info(String.format("Sending data to topic -> %s", "kinaction_schematest"));

            ProducerRecord<Long, Alert> producerRecord = new ProducerRecord<>("kinaction_schematest",
                    alert.getSensorId(),
                    alert
            );
            producer.send(producerRecord);
        }
    }
}
