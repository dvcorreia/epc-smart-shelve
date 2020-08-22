package dvcorreia;

import com.impinj.octane.*;
import org.apache.log4j.Logger;

import java.util.Scanner;

/**
 * Hello world!
 */
public final class App {
    private static final Logger log = Logger.getLogger(App.class);

    public static void main(String[] args) {
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
            report.setIncludePeakRssi(true);
            report.setMode(ReportMode.Individual);

            // A refinement of AutoSetDenseReaderDeepScan, targeted toward static
            // environments where difficult to read tags are expected and we are ready to
            // sacrifice performance to ensure that they are read
            settings.setReaderMode(ReaderMode.AutoSetDenseReaderDeepScan);

            // this will match only the tag pretended for tests
            String matchingMask1 = "30317E3DFA80CF00008299A5";
            TagFilter t1 = settings.getFilters().getTagFilter1();
            t1.setBitCount(96);
            t1.setBitPointer(BitPointers.Epc);
            t1.setMemoryBank(MemoryBank.Epc);
            t1.setFilterOp(TagFilterOp.Match);
            t1.setTagMask(matchingMask1);

            settings.getFilters().setMode(TagFilterMode.OnlyFilter1);

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
