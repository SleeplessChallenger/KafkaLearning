package producer.alerttrendproducer.serializer;


import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AlertSerializer implements Serializer<AlertPojo>, Deserializer<AlertPojo> {

    public byte[] serialize(String topic, AlertPojo key) {
        if (key == null) {
            return null;
        }
        // this is where we will group by as each stageId is unique
        return key.getStageId().getBytes(StandardCharsets.UTF_8);
    }

    public AlertPojo deserialize(String topic, byte[] value) {
        return null;
    }

    public void configure(final Map<String, ?> configs, final boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    public void close() {
        Serializer.super.close();
    }
}
