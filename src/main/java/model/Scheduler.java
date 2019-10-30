package model;

import java.util.Map;

public class Scheduler extends Thread {
    private final ComMonitor comMonitor;
    private final String message;
    private Boolean isMonitoring;

    Scheduler(ComMonitor comMonitor) {
        this.comMonitor = comMonitor;
        isMonitoring = true;
        message = Config.get().getSnmpSettings().get("message");
    }

    public void setMonitoring(Boolean monitoring) {
        isMonitoring = monitoring;
    }

    @Override
    public void run() {
        String msgFromMonitor;
        String[] arrValue;
        String buffer;
        while (isMonitoring) {
            msgFromMonitor = comMonitor.getMsg();
            if (msgFromMonitor != null) {
                arrValue = msgFromMonitor.split(" ");
                buffer = alertTest(arrValue, Config.get().getAlertsList());
                if (buffer.length() > 0) {
                    TrapSender.getINSTANCE().sendTrap(message);
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String alertTest(String[] arrValue, Map<String, Double> alertsList) {
        StringBuilder sb = new StringBuilder();
        if (!"nan".equals(arrValue[1])) {
            if (Double.parseDouble(arrValue[1]) > alertsList.get("first.h.max")) sb.append("first humidity > ")
                    .append(alertsList.get("first.h.max"))
                    .append("\n");
            if (Double.parseDouble(arrValue[1]) < alertsList.get("first.h.min")) sb.append("first humidity < ")
                    .append(alertsList.get("first.h.max"))
                    .append("\n");
            if (Double.parseDouble(arrValue[2]) > alertsList.get("first.t.max")) sb.append("first temperature > ")
                    .append(alertsList.get("first.t.max"))
                    .append("\n");
            if (Double.parseDouble(arrValue[2]) < alertsList.get("first.t.min")) sb.append("first temperature < ")
                    .append(alertsList.get("first.t.min"))
                    .append("\n");
        } else sb.append("second sensor did't work \n");
        if (!"nan".equals(arrValue[3])) {
            if (Double.parseDouble(arrValue[3]) > alertsList.get("second.h.max")) sb.append("second humidity > ")
                    .append(alertsList.get("second.h.max"))
                    .append("\n");
            if (Double.parseDouble(arrValue[3]) < alertsList.get("second.h.min")) sb.append("second humidity < ")
                    .append(alertsList.get("second.h.min"))
                    .append("\n");
            if (Double.parseDouble(arrValue[4]) > alertsList.get("second.t.max")) sb.append("second temperature > ")
                    .append(alertsList.get("second.t.max"))
                    .append("\n");
            if (Double.parseDouble(arrValue[4]) < alertsList.get("second.t.min")) sb.append("second temperature < ")
                    .append(alertsList.get("second.t.min"))
                    .append("\n");
        } else sb.append("first sensor did't work \n");
        return sb.toString();
    }
}

