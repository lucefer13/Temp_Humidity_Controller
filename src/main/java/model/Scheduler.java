package model;

import java.io.IOException;
import java.util.Map;

public class Scheduler extends Thread {
    private final ComMonitor comMonitor;
    private Boolean isMonitoring;
    private final String message;

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
                    try {
                        TrapSender.getINSTANCE().sendTrap(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
            if (Double.parseDouble(arrValue[1]) > alertsList.get("inner.h.max")) sb.append("inner humidity > ")
                    .append(alertsList.get("inner.h.max"))
                    .append("\n");
            if (Double.parseDouble(arrValue[1]) < alertsList.get("inner.h.min")) sb.append("inner humidity < ")
                    .append(alertsList.get("inner.h.max"))
                    .append("\n");
            if (Double.parseDouble(arrValue[2]) > alertsList.get("inner.t.max")) sb.append("inner temperature > ")
                    .append(alertsList.get("inner.t.max"))
                    .append("\n");
            if (Double.parseDouble(arrValue[2]) < alertsList.get("inner.t.min")) sb.append("inner temperature < ")
                    .append(alertsList.get("inner.t.min"))
                    .append("\n");
        } else sb.append("outer sensor did't work \n");

        if (!"nan".equals(arrValue[3])) {
            if (Double.parseDouble(arrValue[3]) > alertsList.get("outer.h.max")) sb.append("outer humidity > ")
                    .append(alertsList.get("outer.h.max"))
                    .append("\n");
            if (Double.parseDouble(arrValue[3]) < alertsList.get("outer.h.min")) sb.append("outer humidity < ")
                    .append(alertsList.get("outer.h.min"))
                    .append("\n");
            if (Double.parseDouble(arrValue[4]) > alertsList.get("outer.t.max")) sb.append("outer temperature > ")
                    .append(alertsList.get("outer.t.max"))
                    .append("\n");
            if (Double.parseDouble(arrValue[4]) < alertsList.get("outer.t.min")) sb.append("outer temperature < ")
                    .append(alertsList.get("outer.t.min"))
                    .append("\n");
        } else sb.append("inner sensor did't work \n");
        return sb.toString();
    }
}

