package dvcorreia;

import com.impinj.octane.*;
import org.apache.log4j.Logger;

import java.util.Scanner;

/**
 * Hello world!
 */
public final class App {
    private static final Logger log = Logger.getLogger(App.class);
    public static String file_name = "";

    public static void main(String[] args) {
        try {
            String hostname = System.getProperty("hostname");

            if (hostname == null) {
                throw new Exception("Must specify the 'hostname' property");
            }

            String shelve = System.getProperty("shelve");

            if (shelve == null) {
                throw new Exception("Must specify the 'shelve' property");
            }

            String height = System.getProperty("height");

            if (height == null) {
                throw new Exception("Must specify the 'height' property");
            }

            String tag_orientation = System.getProperty("tagorientation");

            if (tag_orientation == null) {
                throw new Exception("Must specify the 'tagorientation' property");
            }

            String position = System.getProperty("position");

            if (position == null) {
                throw new Exception("Must specify the 'position' property");
            }

            App.file_name = shelve + height + tag_orientation + position + ".csv";

            ImpinjReader reader = new ImpinjReader();

            log.info("Connecting to Reader at " + hostname + System.lineSeparator());
            reader.connect(hostname);

            Settings settings = reader.queryDefaultSettings();

            ReportConfig report = settings.getReport();
            report.setIncludeAntennaPortNumber(true);
            report.setIncludePeakRssi(true);
            report.setMode(ReportMode.Individual);

            // The reader can be set into various modes in which reader
            // dynamics are optimized for specific regions and environments.
            // The following mode, AutoSetDenseReader, monitors RF noise and interference
            // and then automatically
            // and continuously optimizes the reader's configuration
            settings.setReaderMode(ReaderMode.AutoSetDenseReader);
            // settings.setSearchMode(SearchMode.TagFocus);
            // settings.setSession(1);

            // set some special settings for antenna 1
            AntennaConfigGroup antennas = settings.getAntennas();
            antennas.disableAll();
            antennas.enableById(new short[] { 1 });
            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 1).setIsMaxTxPower(false);
            antennas.getAntenna((short) 1).setTxPowerinDbm(30.0);
            antennas.getAntenna((short) 1).setRxSensitivityinDbm(-80);

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
