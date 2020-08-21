package dvcorreia;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import com.nike.epc.decode.Decoding;
import com.nike.epc.model.*;

import org.apache.log4j.Logger;

import java.util.List;

public class TagReportListenerImplementation implements TagReportListener {
    private static final Logger log = Logger.getLogger(TagReportListenerImplementation.class);

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();

        System.out.println("");
        log.info("RO_REPORT:");

        for (Tag t : tags) {
            String logstr = "";
            String pureURIEPC = "";

            try {
                Epc epc = Decoding.decode(t.getEpc().toHexString());
                pureURIEPC = epc.tagUri();
            } catch (Exception e) {
                log.error("Could not convert EPC to Pure Identity URI - " + e.getMessage());
            }

            if (pureURIEPC.length() > 1) {
                logstr += ("EPC URI: " + pureURIEPC);
            } else {
                logstr += ("EPC: " + t.getEpc().toString());
            }

            log.info(logstr);
        }
    }
}
