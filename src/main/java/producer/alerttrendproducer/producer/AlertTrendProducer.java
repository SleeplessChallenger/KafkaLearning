package producer.alerttrendproducer.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import producer.alerttrendproducer.serializer.AlertPojo;
import producer.alerttrendproducer.serializer.AlertSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AlertTrendProducer {
    /**
     * Kafka feature              Concern
     * Message Loss:              No (can lose data)
     * Grouping:                  Yes - Use Stage ID as each stage is unique
     * Ordering:                  No
     * Last value only:           No
     * Independent consumer:      Yes (we don't care which consumer will process it)
     */
    private static final Logger LOG = LoggerFactory.getLogger(AlertTrendProducer.class);

    public static void main(String[] unused) {
        final Properties kafkaProperties = new Properties();
        kafkaProperties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        kafkaProperties.put("key.serializer", AlertSerializer.class.getName());
        kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try (Producer<AlertPojo, String> producer = new KafkaProducer<>(kafkaProperties)) {
            final AlertPojo alertPojo = new AlertPojo(0,
                    "Stage 0",
                    "CRITICAL",
                    "Stage 0 stopped"
            );

            // Same key will produce the same partition assignment - look in serialize() method of AlertSerializer
            ProducerRecord<AlertPojo, String> producerRecord = new ProducerRecord<>("kinaction_alerttrend",
                    alertPojo,
                    alertPojo.getAlertMessage()
            );

            Future<RecordMetadata> recordMetadataFuture = producer.send(producerRecord);

            try {
                RecordMetadata recordMetadata = recordMetadataFuture.get();
                LOG.info("SUCCESS: offset = {}, topic = {}, timestamp = {}",
                        recordMetadata.offset(), recordMetadata.topic(), recordMetadata.timestamp());
            } catch (ExecutionException | InterruptedException e) {
                LOG.error("Error while getting value from future record");
                throw new RuntimeException(e);
            }
        }
    }
}
