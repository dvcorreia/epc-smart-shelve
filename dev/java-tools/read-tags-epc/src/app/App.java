package app;

import com.impinj.octane.*;
import org.apache.log4j.Logger;
import java.util.Scanner;

public class App {

    private static final Logger log = Logger.getLogger(App.class);

    public static void main(String[] args) throws Exception {

        try {
            String hostname = System.getProperty("hostname");

            if (hostname == null) {
                throw new Exception("Must specify the 'hostname' property");
            }

            ImpinjReader reader = new ImpinjReader();

            log.info("Connecting to Reader at " + hostname + System.lineSeparator());
            reader.connect(hostname);

            Settings settings = reader.queryDefaultSettings();

            ReportConfig report = settings.getReport();
            report.setIncludeAntennaPortNumber(true);
            report.setMode(ReportMode.Individual);

            // The reader can be set into various modes in which reader
            // dynamics are optimized for specific regions and environments.
            // The following mode, AutoSetDenseReader, monitors RF noise and interference
            // and then automatically
            // and continuously optimizes the reader's configuration
            settings.setReaderMode(ReaderMode.AutoSetDenseReader);

            // set some special settings for antenna 1
            AntennaConfigGroup antennas = settings.getAntennas();
            antennas.disableAll();
            antennas.enableById(new short[] { 1 });
            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 1).setIsMaxTxPower(false);
            antennas.getAntenna((short) 1).setTxPowerinDbm(18.0);
            antennas.getAntenna((short) 1).setRxSensitivityinDbm(-70);

            reader.setTagReportListener(new TagReportListenerCallback());

            log.info("Applying Settings to Reader ..." + System.lineSeparator());
            reader.applySettings(settings);

            log.info("Starting readings ..." + System.lineSeparator());
            reader.start();

            log.info("Press Enter to exit." + System.lineSeparator());
            Scanner s = new Scanner(System.in);
            s.nextLine();

            s.close();
            reader.stop();
            reader.disconnect();
        } catch (OctaneSdkException ex) {
            log.error(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            // ex.printStackTrace(System.out);
        }

    }
}