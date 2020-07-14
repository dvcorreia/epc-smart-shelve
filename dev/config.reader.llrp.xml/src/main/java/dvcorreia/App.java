package dvcorreia;

import java.text.*;
import java.util.*;
import org.llrp.ltk.generated.messages.*;
import org.llrp.ltk.generated.parameters.*;
import org.llrp.ltk.types.LLRPMessage;
import org.llrp.ltk.generated.custom.parameters.*;
import org.llrp.ltk.types.UnsignedLong;

/**
 * XmlHarness uses base class EasyLLRPEndpoint to show a simpler way to
 * configure a reader using LTKXML.
 */
public class App extends LtkXmlEndpoint {
    static final int WAIT_FOR_EVENTS_MS = 5000;

    public App(boolean debug) {
        super(debug);
    }

    // Main program that will use the LtkXmlEndpoint base class to startup the
    // speedway reader or xArray,
    // receive some events, and shutdown.
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.print("App speedwayr-XX-XX-XX  <RoSpec xml file>  <Config xml path>(optional)");
            System.exit(1);
        } else {
            System.out.println("Reader=" + args[0]);
            System.out.println("ReaderConfig=" + args[1]);
            if (args.length == 3)
                System.out.println("ROSpec=" + args[2]);
        }
        App ltkXml = null;
        try {
            ltkXml = new App(true);
            ltkXml.connect(args[0]); // args[0] is reader host name SpeedwayR-XX-XX-XX
            // connect calls enableImpinjExtensions and factoryDefault so no need to
            ltkXml.applyROSpec(args[1]); // args[1] contains the Reader Spec XML path
            if (args.length == 3)
                ltkXml.applyReaderConfig(args[2]); // args[1] contains the Config XML path
            ltkXml.start();
            try {
                Thread.sleep(WAIT_FOR_EVENTS_MS); // Wait to capture some events
            } catch (InterruptedException ex) {
                System.out.println("Sleep Interrupted");
            }
            // ltkXml.stop();
            ltkXml.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("DONE");
        }
        System.exit(1); // Force exit! Otherwise program will hang before exiting.
    }

    // Converts all messages received to LTK-XML representation and prints them to
    // the console
    public void messageReceived(LLRPMessage message) {
        // log("Received " + message.getName() + " message asychronously");
        if (message.getTypeNum() == RO_ACCESS_REPORT.TYPENUM) {
            RO_ACCESS_REPORT report = (RO_ACCESS_REPORT) message;
            try {
                for (TagReportData tr : report.getTagReportDataList()) {
                    printTagReport(tr);
                }
            } catch (Exception ex) {
                System.out.println("printTagReport Exception");
            }
            try {
                for (Custom cust : report.getCustomList()) {
                    checkVendor(cust);
                    if (cust instanceof ImpinjExtendedTagInformation) {
                        ImpinjExtendedTagInformation tagInfo = (ImpinjExtendedTagInformation) cust;
                        if (tagInfo.getImpinjLocationReportData() != null) {
                            System.out.println(impinjLocationOutput(tagInfo));
                        } else if (tagInfo.getImpinjTransitionReportData() != null) {
                            System.out.println(impinjTransitionOutput(tagInfo));
                        } else
                            System.out.println("Custom message not supported.");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } // RO_ACCESS_REPORT
        else if (message.getTypeNum().intValue() == 63)
            eventNotification((READER_EVENT_NOTIFICATION) message);
        else
            System.out.println("MessageTypeNum " + message.getTypeNum() + " not handled.");
    }

    // Build String with xArray Location report details.
    private String impinjLocationOutput(ImpinjExtendedTagInformation tagInfo) {
        // Get the EPC associated with this location report
        // xArray reports one tag per report for location, so there is only one EPC
        String output = "Location EPCs: ";
        for (EPCData epc : tagInfo.getEPCDataList())
            output += epc.getEPC() + " ";
        // Get the confidence factors
        output += " ReadCnt=" + tagInfo.getImpinjLocationReportData().getImpinjLocationConfidence().getReadCount();
        output += " Confs:";
        for (int dataIndex = 0; dataIndex < tagInfo.getImpinjLocationReportData().getImpinjLocationConfidence()
                .getConfidenceData().size(); dataIndex++)
            output += tagInfo.getImpinjLocationReportData().getImpinjLocationConfidence().getConfidenceData()
                    .get(dataIndex) + " ";
        // Get the other location report parameters
        output += " Xcm=" + tagInfo.getImpinjLocationReportData().getLocXCentimeters();
        output += " Ycm=" + tagInfo.getImpinjLocationReportData().getLocYCentimeters();
        output += " Type=" + tagInfo.getImpinjLocationReportData().getType();
        output += " TS=" + getDateStringFromEpoch(tagInfo.getImpinjLocationReportData().getLastSeenTimestampUTC());
        return output;
    }

    // Build String with xArray Transition report details.
    private String impinjTransitionOutput(ImpinjExtendedTagInformation tagInfo) {
        // Get all of the EPCs associated with this transition report
        String output = "Transition  EPCs: ";
        for (EPCData epc : tagInfo.getEPCDataList())
            output += epc.getEPC().toString() + " ";
        output += " Confs=" + tagInfo.getImpinjTransitionReportData().getConfidence();
        output += " FromZone=" + tagInfo.getImpinjTransitionReportData().getFromZoneID();
        output += " ToZone=" + tagInfo.getImpinjTransitionReportData().getToZoneID();
        output += " TS=" + getDateStringFromEpoch(tagInfo.getImpinjTransitionReportData().getTimestampUTC());
        output += " Type=" + tagInfo.getImpinjTransitionReportData().getReportType();
        return output;
    }

    // Human readable version of Epoch time
    public String getDateStringFromEpoch(UnsignedLong epoch) {
        Date expiry = new Date(epoch.toLong() / 1000);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return df.format(expiry);
    }

}
