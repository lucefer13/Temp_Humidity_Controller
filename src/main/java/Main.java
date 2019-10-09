import model.ComMonitor;
import model.CommandType;
import model.UtilPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {
    // mask from controller: THC001 27.90 26.50 28.40 26.10
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("THC001_Shell start processing...");
        String portNum = Config.get().getPortNumber();
        if ("auto".equals(portNum)) {
            portNum = UtilPort.get().portFinder(false);
        }
        ComMonitor comMonitor = new ComMonitor(portNum);
        comMonitor.start();
        Thread.sleep(500);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String command;
        System.out.print("THC001_Shell>");
        while (!"EXIT".equals((command = br.readLine().toUpperCase()))) {
            try {
                switch (CommandType.valueOf(command)) {
                    case STATUS:
                        System.out.println(comMonitor.getMsg());
                        break;
                    case FIND_COM:
                        UtilPort.get().portFinder(true);
                        break;
                    case COMMAND_LIST:
                        for (CommandType commandType : CommandType.values()) {
                            System.out.println(commandType);
                        }
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Wrong command. Enter COMMAND_LIST to take all commands");
            }
            System.out.print("THC001_Shell>");
        }
        comMonitor.closePor();
    }
}
