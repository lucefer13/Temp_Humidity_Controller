import model.CommandType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    // mask from controller: getModel  THC001 , getStatus 27.90 26.50 28.40 26.10
    public static void main(String[] args) throws IOException {
        System.out.println("test");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String command;
        while (!"EXIT".equals((command = br.readLine().toUpperCase()))) {
            try {
                switch (CommandType.valueOf(command)) {
                    case STATUS:
                        System.out.println("status");
                        break;
                    case FIND_COM:
                        System.out.println("find_com");
                        break;
                    case SETUP_ALERTS:
                        break;
                    case SETUP_COM:
                        break;
                    case SETUP_MAIL:
                        break;
                    case SETUP_SNMP:
                        break;
                    case SETUP_FILE:
                        break;
                    case COMMAND_LIST:
                        System.out.println(Arrays.asList(CommandType.values()));
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Wrong command. Enter COMMAND_LIST to take all command");
            }
        }

    }
}
