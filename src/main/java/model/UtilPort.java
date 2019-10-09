package model;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.util.ArrayList;
import java.util.Date;

public class UtilPort {
    private static UtilPort instance;

    private UtilPort() {
    }

    public static UtilPort get() {
        if (instance == null) {
            instance = new UtilPort();
        }
        return instance;
    }

    public String portFinder(Boolean showLog) {
        ArrayList<PortFinderThread> arrayList = new ArrayList<>();
        int i = 0;
        for (String port : SerialPortList.getPortNames()) {
            arrayList.add(new PortFinderThread(port, showLog));
            arrayList.get(i++).start();
        }
        boolean statusTread = false;
        while (!statusTread) {
            statusTread = true;
            for (int j = 0; j < i; j++) {
                if (!arrayList.get(j).isTreadStatusFinish()) statusTread = false;
            }
        }
        int counter = 0;
        for (int j = 0; j < i; j++) {
            if (arrayList.get(j).getPort()) {
                if (showLog) {
                    System.out.println("THC001 port is " + arrayList.get(j).getNameThread());
                    counter++;
                } else {
                    return arrayList.get(j).getNameThread();
                }
            }
        }
        if ((counter == 0) && showLog) {
            System.out.println("THC001 port not found");
        }
        return null;
    }

    private static class PortFinderThread extends Thread {
        private String nameThread;
        private boolean treadStatusFinish;
        private boolean port = false;
        private boolean showLog;

        private PortFinderThread(String name, Boolean showLog) {
            nameThread = name;
            treadStatusFinish = false;
            this.showLog = showLog;
        }

        @Override
        public void run() {
            if (showLog) {
                System.out.println(new Date().toString() + "\t" + nameThread + " start searching...");
            }
            SerialPort serialPort = new SerialPort(nameThread);
            try {
                serialPort.openPort();
                serialPort.setParams(SerialPort.BAUDRATE_9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                        SerialPort.FLOWCONTROL_RTSCTS_OUT);
                Thread.sleep(3000);
                String comBuffer = serialPort.readString();
                serialPort.closePort();
                if ((comBuffer != null) && (comBuffer.contains("THC001"))) {
                    if (showLog) {
                        System.out.println(new Date().toString() + "\t" + nameThread + " successfully");
                    }
                    port = true;
                }
            } catch (SerialPortException e) {
                if (showLog) {
                    System.out.println(new Date().toString() + "\t" + nameThread + " " + e.getExceptionType());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (showLog) {
                System.out.println(new Date().toString() + "\t" + nameThread + " searching stop");
            }
            treadStatusFinish = true;
        }

        private boolean getPort() {
            return port;
        }

        private String getNameThread() {
            return nameThread;
        }

        private boolean isTreadStatusFinish() {
            return treadStatusFinish;
        }
    }
}
