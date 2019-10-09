package model;


import jssc.SerialPort;
import jssc.SerialPortException;

public class ComMonitor extends Thread {
    private static String COMPORT;
    private String msg;
    private Boolean isPortOpen;

    public ComMonitor(String comPort) {
        COMPORT = comPort;
        isPortOpen = true;
    }

    @Override
    public void run() {
        SerialPort serialPort = new SerialPort(COMPORT);
        try {
            serialPort.openPort();
            //Выставляем параметры
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            //Включаем аппаратное управление потоком
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
            //Устанавливаем ивент лисенер и маску
            serialPort.addEventListener(event -> {
                if (event.isRXCHAR() && event.getEventValue() > 0) {
                    try {
                        //Получаем ответ от устройства, обрабатываем данные и т.д.
                        msg = serialPort.readString(event.getEventValue());
                        if (!isPortOpen) {
                            serialPort.closePort();
                        }
                    } catch (SerialPortException ex) {
                        System.out.println(ex.getPortName() + " " + ex.getExceptionType());
                    }
                }
            }, SerialPort.MASK_RXCHAR);
        } catch (
                SerialPortException ex) {
            System.out.println("DHC001 port error, please check config file and controller");
        }
    }

    public String getMsg() {
        return msg;
    }

    public void closePor() {
        isPortOpen = false;
    }

}



