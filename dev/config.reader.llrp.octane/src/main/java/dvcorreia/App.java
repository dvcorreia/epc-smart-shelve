package dvcorreia;

import com.impinj.octane.*;
import org.apache.log4j.Logger;
import java.util.Scanner;

/**
 * Configures Reader with Octane SDK and saves / loads local configurations
 */
public final class App {
    private static final Logger log = Logger.getLogger(App.class);

    private static Settings configReader(ImpinjReader reader) throws Exception {
        Settings settings = reader.queryDefaultSettings();

        ReportConfig report = settings.getReport();
        report.setIncludeAntennaPortNumber(true);

        // The reader will collect and coalesce tag reports and send them the the
        // current operation completes and the reader stops
        report.setMode(ReportMode.BatchAfterStop);

        settings.getAutoStart().setMode(AutoStartMode.Periodic);
        settings.getAutoStart().setPeriodInMs(5000);
        settings.getAutoStop().setMode(AutoStopMode.Duration);
        settings.getAutoStop().setDurationInMs(5000);

        // A refinement of AutoSetDenseReaderDeepScan, targeted toward static
        // environments where difficult to read tags are expected and we are ready to
        // sacrifice performance to ensure that they are read
        settings.setReaderMode(ReaderMode.AutoSetDenseReaderDeepScan);

        // Single Target Inventory with Suppression (aka TagFocus)
        settings.setSearchMode(SearchMode.TagFocus);
        settings.setSession(1);

        // Setup filtering

        // this will match the first 16 bits of the target EPC.
        String matchingMask = "91882001";
        TagFilter t1 = settings.getFilters().getTagFilter1();
        t1.setBitCount(27);
        t1.setBitPointer(BitPointers.Epc + 14);
        t1.setMemoryBank(MemoryBank.Epc);
        t1.setFilterOp(TagFilterOp.Match);
        t1.setTagMask(matchingMask);
        settings.getFilters().setMode(TagFilterMode.OnlyFilter1);

        // set some special settings for antenna 1
        AntennaConfigGroup antennas = settings.getAntennas();
        antennas.disableAll();
        antennas.enableById(new short[] { 1 });
        antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
        antennas.getAntenna((short) 1).setIsMaxTxPower(false);
        antennas.getAntenna((short) 1).setTxPowerinDbm(30.0);
        antennas.getAntenna((short) 1).setRxSensitivityinDbm(-80);

        return (settings);
    }

    public static void main(String[] args) {
        try {
            String hostname = System.getProperty("hostname");
            String saveSettings = System.getProperty("savesettings");

            if (hostname == null) {
                throw new Exception("Must specify the 'hostname' property");
            }

            ImpinjReader reader = new ImpinjReader();

            log.info("Connecting to Reader at " + hostname + System.lineSeparator());
            reader.connect(hostname);

            Settings settings = configReader(reader);
            if (saveSettings != null) {
                log.info("Saving reader configuration in /reader_settings.json ...");
                settings.save("./reader_settings.json");
            }

            reader.setTagReportListener(new TagReportListenerImplementation());

            log.info("Applying Settings to Reader ..." + System.lineSeparator());
            reader.applySettings(settings);

            log.info("Starting readings ..." + System.lineSeparator());
            reader.start();

            log.info("Press Enter to exit." + System.lineSeparator());
            Scanner s = new Scanner(System.in);
            s.nextLine();

            s.close();

            log.info("Removing tag report listener ...");
            reader.removeTagReportListener();
            log.info("Disconnecting from reader ...");
            reader.disconnect();
            log.warn("RO_SPEC is still active ...");
            log.info("Exited with success!");
        } catch (OctaneSdkException ex) {
            log.error(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            // ex.printStackTrace(System.out);
        }
    }
}
