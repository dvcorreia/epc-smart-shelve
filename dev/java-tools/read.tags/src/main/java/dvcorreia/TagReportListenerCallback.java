package dvcorreia;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import com.nike.epc.decode.Decoding;
import com.nike.epc.model.*;
//import com.nike.epc.model.sgtin.*;

import org.apache.log4j.Logger;
import java.util.List;

public class TagReportListenerCallback implements TagReportListener {
    private static final Logger log = Logger.getLogger(TagReportListenerCallback.class);

    private void printTag(Tag t) {
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

        if (t.isAntennaPortNumberPresent()) {
            logstr += (" antenna: " + t.getAntennaPortNumber());
        }

        if (t.isFirstSeenTimePresent()) {
            logstr += (" first: " + t.getFirstSeenTime().ToString());
        }

        if (t.isLastSeenTimePresent()) {
            logstr += (" last: " + t.getLastSeenTime().ToString());
        }

        if (t.isSeenCountPresent()) {
            logstr += (" count: " + t.getTagSeenCount());
        }

        if (t.isRfDopplerFrequencyPresent()) {
            logstr += (" doppler: " + t.getRfDopplerFrequency());
        }

        if (t.isPeakRssiInDbmPresent()) {
            logstr += (" peak_rssi: " + t.getPeakRssiInDbm());
        }

        if (t.isChannelInMhzPresent()) {
            logstr += (" chan_MHz: " + t.getChannelInMhz());
        }

        if (t.isRfPhaseAnglePresent()) {
            logstr += (" phase angle: " + t.getPhaseAngleInRadians());
        }

        if (t.isFastIdPresent()) {
            logstr += ("\n     fast_id: " + t.getTid().toHexString());
            logstr += (" model: " + t.getModelDetails().getModelName());
            logstr += (" epcsize: " + t.getModelDetails().getEpcSizeBits());
            logstr += (" usermemsize: " + t.getModelDetails().getUserMemorySizeBits());
        }

        log.info(logstr);
    }

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();

        for (Tag t : tags) {
            this.printTag(t);
        }
    }
}
