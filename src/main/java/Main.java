import model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

//TODO Add email https://www.mkyong.com/java/java-how-to-send-email/
//TODO Add Exception and log
public class Main {
    // mask from controller: THC001 27.90 26.50 28.40 26.10
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("THC001_Shell start service...");
        String portNum = Config.get().getPortNumber();
        if ("auto".equals(portNum)) {
            portNum = UtilPort.get().portFinder(false);
        }
        //TODO check valid comPort from model.Config (refactor UtilPort to exist mode)
        ComMonitor comMonitor = new ComMonitor(portNum);
        System.out.println("THC001_Shell start service...DONE");
        comMonitor.start();
        Thread.sleep(500);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String command = "";
        System.out.println();
        while (!"EXIT".equals(command)) {
            try {
                System.out.print("THC001_Shell>");
                command = br.readLine().toUpperCase();
                switch (CommandType.valueOf(command)) {
                    case STATUS:
                        if (comMonitor.getMsg() == null) {
                            System.out.println("Controller not connected ");
                        } else {
                            System.out.println();
                            System.out.println("Port number: " + comMonitor.getCOMPORT());
                            String[] values = comMonitor.getMsg().split(" ");
                            System.out.println("Controller name: " + values[0]);
                            System.out.println("First humidity: " + values[1]);
                            System.out.println("First temperature: " + values[2]);
                            System.out.println("Second humidity: " + values[3]);
                            System.out.println("Second temperature: " + values[4]);
                            System.out.println();
                        }
                        break;
                    case FIND_COM:
                        UtilPort.get().portFinder(true);
                        break;
                    case TEST_SNMP:
                        System.out.println();
                        String[] valuse = TrapSender.getSNMPSettings();
                        System.out.println("Port: " + valuse[0]);
                        System.out.println("Community: " + valuse[1]);
                        System.out.println("Oid: " + valuse[2]);
                        System.out.println("Ip: " + valuse[3]);
                        System.out.println("Test message send to SNMP");
                        TrapSender.getINSTANCE().sendTrap("Test THC001 SNMP");
                        System.out.println();
                        break;
                    case EXIT:
                        System.out.println();
                        comMonitor.closePor();
                        System.out.println("THC001_Shell stop service...");
                        break;
                    case ALERTS_LIST:
                        System.out.println();
                        for (Map.Entry alert : Config.get().getAlertsList().entrySet()) {
                            System.out.println(alert.getKey() + " " + alert.getValue());
                        }
                        System.out.println();
                        break;
                    case HELP:
                    case COMMAND_LIST:
                        System.out.println();
                        for (CommandType commandType : CommandType.values()) {
                            System.out.println(commandType);
                        }
                        System.out.println();
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println();
                System.out.println("Wrong command. Enter COMMAND_LIST / HELP to take all commands");
                System.out.println();
            }
        }
    }
}
