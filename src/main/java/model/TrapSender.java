package model;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class TrapSender {
    private static final TrapSender INSTANCE = new TrapSender();
    //Ideally Port 162 should be used to send receive Trap, any other available Port can be used
    private static final int port = 162;
    private static String community;
    // Sending Trap for sysLocation of RFC1213
    private static String oid;
    //IP of Local Host
    private static String ipAddress;
    private final long pause;
    private long lastSend = 0;

    private TrapSender() {
        Map<String, String> settingsList = Config.get().getSnmpSettings();
        community = settingsList.get("community");
        oid = settingsList.get("oid");
        ipAddress = settingsList.get("ip");
        pause = Long.parseLong(settingsList.get("pause"))*1000;
    }

    public static TrapSender getINSTANCE() {
        return INSTANCE;
    }

    public void sendTrap(String text) {
        if (Math.abs(lastSend - new Date().getTime())>pause) {
            /* Create Transport Mapping */
            lastSend = new Date().getTime();
            try {
                TransportMapping transport = new DefaultUdpTransportMapping();
                transport.listen();
                // Create Target
                CommunityTarget cTarget = new CommunityTarget();
                cTarget.setCommunity(new OctetString(community));
                cTarget.setVersion(SnmpConstants.version2c);
                cTarget.setAddress(new UdpAddress(ipAddress + "/" + port));
                cTarget.setRetries(2);
                cTarget.setTimeout(5000);
                // Create PDU for V2
                PDU pdu = new PDU();
                // need to specify the system up time
                pdu.add(new VariableBinding(SnmpConstants.sysUpTime,
                        new OctetString(new Date().toString())));
                pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(
                        oid)));
                pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress,
                        new IpAddress(ipAddress)));
                pdu.add(new VariableBinding(new OID(oid), new OctetString(
                        text)));
                pdu.setType(PDU.NOTIFICATION);
                // Send the PDU
                Snmp snmp = new Snmp(transport);
                //   System.out.println("Sending V2 Trap... Check Wheather NMS is Listening or not? ");
                snmp.send(pdu, cTarget);
                snmp.close();
            }catch (Exception e){
                System.out.println("Lan error");
            }
        }
    }

    public static String[] getSNMPSettings() {
        String[] values = new String[4];
        values[0] = String.valueOf(port);
        values[1] = String.valueOf(community);
        values[2] = String.valueOf(oid);
        values[3] = String.valueOf(ipAddress);
        return values;
    }
    /*         catch (Exception e) {
                e.printStackTrace();
        }*/
}
