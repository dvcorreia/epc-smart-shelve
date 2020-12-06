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
import java.util.Collection;
import java.util.HashSet;
import dvcorreia.TagList;

public class TagReportListenerCallback implements TagReportListener {
    private static final Logger log = Logger.getLogger(TagReportListenerCallback.class);
    private static final TagList tagList = new TagList();

    private void printTag(String hexEPC) {
        try {
            Epc epc = Decoding.decode(hexEPC);
            String pureURIEPC = epc.tagUri();
            log.info("EPC URI: " + pureURIEPC);
        } catch (Exception e) {
            log.error("Could not convert EPC to Pure Identity URI - " + e.getMessage());
        }
    }

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();
        Collection<String> tagHex = new HashSet<String>();

        log.info("Tags found ...");
        for (Tag t : tags) {
            tagHex.add(t.getEpc().toHexString());
        }

        Collection<String> different = new HashSet<String>();
        different.addAll(tagList.listEpcBank);
        different.removeAll(tagHex);

        log.info("Tags failed to read ...");
        for (String s : different) {
            this.printTag(s);
        }

        log.info("Inventory completed!");

    }
}
