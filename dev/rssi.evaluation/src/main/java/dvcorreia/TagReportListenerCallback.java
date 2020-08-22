package dvcorreia;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import org.apache.log4j.Logger;
import java.util.List;

public class TagReportListenerCallback implements TagReportListener {
    private static final Logger log = Logger.getLogger(TagReportListenerCallback.class);

    private void printTag(Tag t) {
        if (t.isPeakRssiInDbmPresent()) {
            log.info("RSSI: " + t.getPeakRssiInDbm());
        }
    }

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();

        for (Tag t : tags) {
            this.printTag(t);
        }
    }
}
