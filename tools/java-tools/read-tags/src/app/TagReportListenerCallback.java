package app;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

//import org.apache.log4j.Logger;
//import org.epctagcoder.parse;
import java.util.List;

public class TagReportListenerCallback implements TagReportListener {
    // private static final Logger log =
    // Logger.getLogger(TagReportListenerCallback.class);
    private void printTag(Tag t) {
        System.out.print(" EPC: " + t.getEpc().toString());

        if (t.isAntennaPortNumberPresent()) {
            System.out.print(" antenna: " + t.getAntennaPortNumber());
        }

        if (t.isFirstSeenTimePresent()) {
            System.out.print(" first: " + t.getFirstSeenTime().ToString());
        }

        if (t.isLastSeenTimePresent()) {
            System.out.print(" last: " + t.getLastSeenTime().ToString());
        }

        if (t.isSeenCountPresent()) {
            System.out.print(" count: " + t.getTagSeenCount());
        }

        if (t.isRfDopplerFrequencyPresent()) {
            System.out.print(" doppler: " + t.getRfDopplerFrequency());
        }

        if (t.isPeakRssiInDbmPresent()) {
            System.out.print(" peak_rssi: " + t.getPeakRssiInDbm());
        }

        if (t.isChannelInMhzPresent()) {
            System.out.print(" chan_MHz: " + t.getChannelInMhz());
        }

        if (t.isRfPhaseAnglePresent()) {
            System.out.print(" phase angle: " + t.getPhaseAngleInRadians());
        }

        if (t.isFastIdPresent()) {
            System.out.print("\n     fast_id: " + t.getTid().toHexString());

            System.out.print(" model: " + t.getModelDetails().getModelName());

            System.out.print(" epcsize: " + t.getModelDetails().getEpcSizeBits());

            System.out.print(" usermemsize: " + t.getModelDetails().getUserMemorySizeBits());
        }

        System.out.println("");
    }

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();

        for (Tag t : tags) {
            this.printTag(t);
            // ParseSSCC parseSSCC =
            // ParseSSCC.Builder().withRFIDTag("31AC16465751CCD0C2000000").build();

        }
    }
}
