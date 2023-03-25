package producer.alerttrendproducer.serializer;

import java.io.Serializable;

public class AlertPojo implements Serializable {
    private final int alertId;
    private String stageId;
    private final String alertLevel;
    private final String alertMessage;

    public AlertPojo(int alertId,
                     String stageId,
                     String alertLevel,
                     String alertMessage) {
        this.alertId = alertId;
        this.stageId = stageId;
        this.alertLevel = alertLevel;
        this.alertMessage = alertMessage;
    }

    public int getAlertId() {
        return alertId;
    }

    public String getStageId() {
        return stageId;
    }

    public String getAlertLevel() {
        return alertLevel;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }
}
