package producer.auditproducer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AuditProducer {
    /**
     * Kafka feature              Concern
     * Message Loss:              Yes (can't lose data)
     * Grouping:                  No
     * Ordering:                  No
     * Last value only:           No
     * Independent consumer:      Yes (we don't care which consumer will process it)
     */
    private static final Logger LOG = LoggerFactory.getLogger(AuditProducer.class);
    private static final String TOPIC_NAME = "kinaction_audit";

    public static void main(String[] unused) {
        final Properties kafkaProperties = new Properties();
        kafkaProperties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        // 2 params below are crucial for guaranteed delivery
        kafkaProperties.put("acks", "all"); // For not losing any messages
        kafkaProperties.put("max.in.flight.requests.per.connection", "1");

        kafkaProperties.put("retries", "3");
        kafkaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProperties)) {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC_NAME,
                    null,
                    "audit event");
            Future<RecordMetadata> recordMetadataFuture = producer.send(producerRecord);

            try {
                RecordMetadata recordMetadata = recordMetadataFuture.get();
                // wait for each response to come back as here focus is on no data loss rather than speed
                LOG.info("Success: offset = {}, topic = {}, timestamp = {}",
                        recordMetadata.offset(), recordMetadata.topic(), recordMetadata.timestamp());
            } catch (ExecutionException | InterruptedException e) {
                LOG.error("Error during retrieving data");
                throw new RuntimeException(e);
            }
        }
    }
}
