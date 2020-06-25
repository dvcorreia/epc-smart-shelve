/*****************************************************************************
 *                                                                           *
 *              IMPINJ CONFIDENTIAL AND PROPRIETARY                          *
 *                                                                           *
 * This source code is the sole property of Impinj, Inc. Reproduction or     *
 * utilization of this source code in whole or in part is forbidden without  *
 * the prior written consent of Impinj, Inc.                                 *
 *                                                                           *
 * (c) Copyright Impinj, Inc. 2014. All rights reserved.                     *
 *                                                                           *
 *****************************************************************************/

package dvcorreia;

import java.io.*;
import java.util.concurrent.TimeoutException;
import org.jdom.*;
import org.jdom.JDOMException;
import org.llrp.ltk.exceptions.*;
import org.llrp.ltk.generated.*;
import org.llrp.ltk.generated.enumerations.StatusCode;
import org.llrp.ltk.generated.messages.*;
import org.llrp.ltk.generated.parameters.*;
import org.llrp.ltk.types.*;
import org.llrp.ltk.net.*;
import org.llrp.ltk.util.Util;

/*
 * LtkXmlEndpoint provides a convent base class for LLRP java programs sending LTKXML
 * strings to the reader. This class also provides methods to perform basic operations such as
 * factoryDefaults, impinjExtensions, connect, start, enable, stop, disable, and disconnect.
 * Lastly, the user can define the callback handler messageReceived() to receive tag reports in
 * java.
 */
public abstract class LtkXmlEndpoint implements LLRPEndpoint {
    final int TRANSACTION_TIMEOUT_MS = 10000; // 10 seconds
    private LLRPConnection connection;
    private ROSpec rospec;
    private int MessageID = 23; // a random starting point
    boolean verbose; // Verbpse will output an a lot of messages

    private UnsignedInteger getUniqueMessageID() {
        return new UnsignedInteger(MessageID++);
    }

    public LtkXmlEndpoint(boolean verbose) {
        this.verbose = verbose;
    }

    /*
     * Create client-initiated LLRP connection
     */
    protected void connect(String ip) throws Exception {
        connection = new LLRPConnector(this, ip);
        // LLRPConnector.connect waits for successful
        // READER_EVENT_NOTIFICATION from reader
        log("Initiate LLRP connection to reader");
        ((LLRPConnector) connection).connect();
        enableImpinjExtensions();
        factoryDefault();
    }

    protected void disconnect() throws Exception {
        log("Disconnecting...");
        transactXML("<CLOSE_CONNECTION MessageID=\"" + getUniqueMessageID()
                + "\"  xmlns=\"http://www.llrp.org/ltk/schema/core/encoding/xml/1.0\">" + "</CLOSE_CONNECTION>");
        log("Disconnected");
    }

    /*
     * Enables Impinj extentions in Reader
     */
    protected void enableImpinjExtensions() throws Exception {
        log("IMPINJ_ENABLE_EXTENSIONS ...");
        transactXML("<IMPINJ_ENABLE_EXTENSIONS MessageID=\"" + getUniqueMessageID()
                + "\"  xmlns=\"http://developer.impinj.com/ltk/schema/encoding/xml/1.18\">"
                + "</IMPINJ_ENABLE_EXTENSIONS>");
    }

    /*
     * Sets reader to a known configuration to layer reader configuration parameter
     * on top of.
     */
    protected void factoryDefault() throws Exception {
        log("SET_READER_CONFIG with factory default ...");
        transactXML("<SET_READER_CONFIG MessageID=\"" + getUniqueMessageID()
                + "\"  xmlns=\"http://www.llrp.org/ltk/schema/core/encoding/xml/1.0\">"
                + "<ResetToFactoryDefault>1</ResetToFactoryDefault>" + "</SET_READER_CONFIG>");
    }

    private LLRPMessage buildFromXmlFile(String xmlFilePath) throws Exception {
        log("Loading message from file " + xmlFilePath);
        try {
            LLRPMessage llrpMessage = Util.loadXMLLLRPMessage(new File(xmlFilePath));
            return llrpMessage;
        } catch (FileNotFoundException ex) {
            throw new Exception("Could not find file");
        } catch (IOException ex) {
            throw new Exception("IO Exception on file");
        } catch (JDOMException ex) {
            throw new Exception("Unable to convert LTK-XML to DOM " + ex.getMessage());
        } catch (InvalidLLRPMessageException ex) {
            throw new Exception("Unable to convert LTK-XML to Internal Object " + ex.getMessage());
        }
    }

    protected void applyROSpec(String xmlFilePath) throws Exception {
        ADD_ROSPEC addRospec = (ADD_ROSPEC) buildFromXmlFile(xmlFilePath);
        addRospec.setMessageID(getUniqueMessageID());
        rospec = addRospec.getROSpec();
        log("Sending ADD_ROSPEC message  ...");
        try {
            LLRPMessage response = connection.transact(addRospec, TRANSACTION_TIMEOUT_MS);
            // check whether ROSpec addition was successful
            StatusCode status = ((ADD_ROSPEC_RESPONSE) response).getLLRPStatus().getStatusCode();
            if (status.equals(new StatusCode("M_Success"))) {
                log("ADD_ROSPEC was successful");
            } else {
                throw new Exception("ADD_ROSPEC: " + response.toXMLString());
            }
        } catch (InvalidLLRPMessageException ex) {
            throw new Exception("Could not display response string");
        } catch (TimeoutException ex) {
            throw new Exception("Timeout waiting for ADD_ROSPEC response: " + ex.getMessage());
        }
    }

    protected void applyReaderConfig(String xmlFilePath) throws Exception {
        LLRPMessage response;
        log("Loading SET_READER_CONFIG message from file " + xmlFilePath);
        try {
            LLRPMessage setConfigMsg = buildFromXmlFile(xmlFilePath);
            // Cast message to needed type
            SET_READER_CONFIG setConfig = (SET_READER_CONFIG) setConfigMsg;
            response = connection.transact(setConfig, TRANSACTION_TIMEOUT_MS);
            // check whetherSET_READER_CONFIG addition was successful
            StatusCode status = ((SET_READER_CONFIG_RESPONSE) response).getLLRPStatus().getStatusCode();
            if (status.equals(new StatusCode("M_Success"))) {
                log("SET_READER_CONFIG was successful");
                return;
            } else {
                log(response.toXMLString());
                throw new Exception("SET_READER_CONFIG failures");
            }
        } catch (TimeoutException ex) {
            throw new Exception("Timeout waiting for SET_READER_CONFIG response");
        } catch (FileNotFoundException ex) {
            throw new Exception("Could not find file");
        } catch (IOException ex) {
            throw new Exception("IO Exception on file");
        } catch (JDOMException ex) {
            throw new Exception("Unable to convert LTK-XML to DOM: " + ex.getMessage());
        } catch (InvalidLLRPMessageException ex) {
            throw new Exception("Unable to convert LTK-XML to Internal Object: " + ex.getMessage());
        }
    }

    /*
     * Enable and Start the reader
     */
    protected void enable() throws Exception {
        log("ENABLE_ROSPEC ...");
        transactXML("<ENABLE_ROSPEC MessageID=\"" + getUniqueMessageID()
                + "\"  xmlns=\"http://www.llrp.org/ltk/schema/core/encoding/xml/1.0\">" + "<ROSpecID>"
                + rospec.getROSpecID() + "</ROSpecID>" + "</ENABLE_ROSPEC>");
    }

    protected void start() throws Exception {
        enable(); // Now that we are enabled start the RO Spec
        log("START_ROSPEC ...");
        transactXML("<START_ROSPEC MessageID=\"" + getUniqueMessageID()
                + "\"  xmlns=\"http://www.llrp.org/ltk/schema/core/encoding/xml/1.0\">" + "<ROSpecID>"
                + rospec.getROSpecID() + "</ROSpecID>" + "</START_ROSPEC>");
    }

    /*
     * Stop reader
     */
    protected void stop() throws Exception {
        log("STOP_ROSPEC ...");
        transactXML("<STOP_ROSPEC MessageID=\"" + getUniqueMessageID()
                + "\"  xmlns=\"http://www.llrp.org/ltk/schema/core/encoding/xml/1.0\">" + "<ROSpecID>"
                + rospec.getROSpecID() + "</ROSpecID>" + "</STOP_ROSPEC>");
        disable(); // automatically disable
    }

    protected void disable() throws Exception {
        log("DISABLE_ROSPEC ...");
        transactXML("<DISABLE_ROSPEC MessageID=\"" + getUniqueMessageID()
                + "\"  xmlns=\"http://www.llrp.org/ltk/schema/core/encoding/xml/1.0\">" + "<ROSpecID>"
                + rospec.getROSpecID() + "</ROSpecID>" + "</DISABLE_ROSPEC>");
    }

    protected void checkVendor(Custom cust) throws Exception {
        if (!cust.getVendorIdentifier().equals(25882)) {
            throw new Exception("Non Impinj Extension Found in message");
        }
    }

    protected void printTagReport(TagReportData tr) throws Exception {
        // As an example here, we'll just get the stuff out of here and
        // for a super long string
        LLRPParameter epcp = (LLRPParameter) tr.getEPCParameter();
        // epc is not optional, so we should fail if we can't find it
        String epcString = "EPC: ";
        if (epcp != null) {
            if (epcp.getName().equals("EPC_96")) {
                EPC_96 epc96 = (EPC_96) epcp;
                epcString += epc96.getEPC().toString();
            } else if (epcp.getName().equals("EPCData")) {
                EPCData epcData = (EPCData) epcp;
                epcString += epcData.getEPC().toString();
            }
        } else {
            throw new Exception("Could not find EPC in Tag Report");
        }
        // all of these values are optional, so check their non-nullness first
        if (tr.getAntennaID() != null) {
            epcString += " Antenna: " + tr.getAntennaID().getAntennaID().toString();
        }
        if (tr.getChannelIndex() != null) {
            epcString += " ChanIndex: " + tr.getChannelIndex().getChannelIndex().toString();
        }
        if (tr.getFirstSeenTimestampUTC() != null) {
            epcString += " FirstSeen: " + tr.getFirstSeenTimestampUTC().getMicroseconds().toString();
        }
        if (tr.getInventoryParameterSpecID() != null) {
            epcString += " ParamSpecID: " + tr.getInventoryParameterSpecID().getInventoryParameterSpecID().toString();
        }
        if (tr.getLastSeenTimestampUTC() != null) {
            epcString += " LastTime: " + tr.getLastSeenTimestampUTC().getMicroseconds().toString();
        }
        if (tr.getPeakRSSI() != null) {
            epcString += " RSSI: " + tr.getPeakRSSI().getPeakRSSI().toString();
        }
        if (tr.getROSpecID() != null) {
            epcString += " ROSpecID: " + tr.getROSpecID().getROSpecID().toString();
        }
        if (tr.getTagSeenCount() != null) {
            epcString += " SeenCount: " + tr.getTagSeenCount().getTagCount().toString();
        }
        System.out.println(epcString);
        // logger.debug(output);
    }

    @Override
    abstract public void messageReceived(LLRPMessage message);

    @Override
    public void errorOccured(String str) {
        System.out.println(str);
    }

    protected void log(String str) {
        if (verbose)
            System.out.println(str);
    }

    /*
     * transactXML takes an xml string input and sends it to the speedwayr.
     */
    public void transactXML(String str) throws Exception {
        Document doc = new org.jdom.input.SAXBuilder().build(new StringReader(str));
        LLRPMessage message = LLRPMessageFactory.createLLRPMessage(doc);
        LLRPMessage response = connection.transact(message, TRANSACTION_TIMEOUT_MS);
        String respStr = response.toXMLString();
        if (!respStr.contains("M_Success")) {
            log(respStr);
            throw new Exception("transactXML failures");
        }
    }

    /**
     * Handles a READER_EVENT_NOTIFICATION message from the Reader
     *
     * @param pNotification READER_EVENT_NOTIFICATION from the Reader
     */
    public void eventNotification(READER_EVENT_NOTIFICATION pNotification) {
        ReaderEventNotificationData eventData = pNotification.getReaderEventNotificationData();
        if (eventData != null) {
            if (eventData.getAISpecEvent() != null) {
                System.out.println("\tAISpec Event: " + eventData.getAISpecEvent().toString());
            } else if (eventData.getAntennaEvent() != null) {
                System.out.println("\tAntenna Event: " + eventData.getAntennaEvent().getAntennaID().intValue()
                        + " has been " + eventData.getAntennaEvent().getEventType().toString());
            } else if (eventData.getConnectionAttemptEvent() != null) {
                System.out.println("\tConnection Attempt Event: " + eventData.getConnectionAttemptEvent().toString());
            } else if (eventData.getConnectionCloseEvent() != null) {
                System.out.println("\tConnection Close Event: " + eventData.getConnectionCloseEvent().toString());
            } else if (eventData.getGPIEvent() != null) {
                System.out.println("\tGPI Event: " + eventData.getGPIEvent().toString());
            } else if (eventData.getHoppingEvent() != null) {
                System.out.println("\tHopping Event: " + eventData.getHoppingEvent().toString());
            } else if (eventData.getReaderExceptionEvent() != null) {
                System.out.println("\tReader Exception Event: " + eventData.getReaderExceptionEvent().toString());
            } else if (eventData.getReportBufferLevelWarningEvent() != null) {
                System.out.println("\tReport Buffer Level Warning Event: "
                        + eventData.getReportBufferLevelWarningEvent().toString());
            } else if (eventData.getReportBufferOverflowErrorEvent() != null) {
                System.out.println("\tReport Buffer Overflow Error Event: "
                        + eventData.getReportBufferOverflowErrorEvent().toString());
            } else if (eventData.getRFSurveyEvent() != null) {
                System.out.println("\tRF Survey Event: " + eventData.getRFSurveyEvent().toString());
            } else if (eventData.getROSpecEvent() != null) {
                System.out.println("\tROSpec Event: " + eventData.getROSpecEvent().toString());
            }
        }
    }
}
