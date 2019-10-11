package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
    //TODO save config
    private static final File PROPERTIES_FILE = new File("config/controller.properties");
    private static final Config INSTANCE = new Config();
    private String portNumber;
    private Map<String, Double> alertsList = new HashMap<>();
    private Map<String, String> snmpSettings = new HashMap<>();


    private Config() {
        try (InputStream is = new FileInputStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            properties.load(is);
            portNumber = properties.getProperty("port.number");

            alertsList.put("first.t.max", Double.parseDouble(properties.getProperty("first.t.max")));
            alertsList.put("first.t.min", Double.parseDouble(properties.getProperty("first.t.min")));
            alertsList.put("first.h.max", Double.parseDouble(properties.getProperty("first.h.max")));
            alertsList.put("first.h.min", Double.parseDouble(properties.getProperty("first.h.min")));
            alertsList.put("second.t.min", Double.parseDouble(properties.getProperty("second.t.min")));
            alertsList.put("second.h.max", Double.parseDouble(properties.getProperty("second.h.max")));
            alertsList.put("second.h.min", Double.parseDouble(properties.getProperty("second.h.min")));
            alertsList.put("second.t.max", Double.parseDouble(properties.getProperty("second.t.max")));

            snmpSettings.put("community", properties.getProperty("snmp.community"));
            snmpSettings.put("oid", properties.getProperty("snmp.oid"));
            snmpSettings.put("ip", properties.getProperty("snmp.ip"));
            snmpSettings.put("message", properties.getProperty("snmp.message"));
            snmpSettings.put("pause", properties.getProperty("snmp.pause"));


        } catch (Exception e) {
            throw new IllegalStateException("Invalid config file " + PROPERTIES_FILE.getAbsolutePath());
        }
    }

    public static Config get() {
        return INSTANCE;
    }

    public Map<String, String> getSnmpSettings() {
        return snmpSettings;
    }

    public Map<String, Double> getAlertsList() {
        return alertsList;
    }

    public String getPortNumber() {
        return portNumber;
    }
}
