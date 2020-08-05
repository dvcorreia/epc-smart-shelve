/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Fosstrak (www.fosstrak.org).
 *
 * Fosstrak is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Fosstrak is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Fosstrak; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.fosstrak.capturingapp.util;

import java.util.LinkedList;
import java.util.List;

import org.fosstrak.ale.xsd.ale.epcglobal.ECReaderStat;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReport;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroup;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroupListMember;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReports;
import org.fosstrak.ale.xsd.ale.epcglobal.ECTagStat;
import org.fosstrak.ale.xsd.epcglobal.EPC;

/**
 * helper class to perform transformations on ECReports.
 * 
 * @author sawielan
 *
 */
public class Util {

	/**
	 * Simple interface that allows you to select an EPC programmatically (eg.
	 * raw-hex, tag, ...);
	 * 
	 * @author sawielan
	 *
	 */
	public interface EPCSelector {
		public EPC select(ECReportGroupListMember member);
	}

	/** selector that returns the raw hex. */
	public static final EPCSelector selectRawHex = new EPCSelector() {
		public EPC select(ECReportGroupListMember member) {
			return member.getRawHex();
		}
	};

	/** selector that returns the raw decimal. */
	public static final EPCSelector selectRawDecimal = new EPCSelector() {
		public EPC select(ECReportGroupListMember member) {
			return member.getRawDecimal();
		}
	};

	/** selector that returns the tag. */
	public static final EPCSelector selectTag = new EPCSelector() {
		public EPC select(ECReportGroupListMember member) {
			return member.getTag();
		}
	};

	/** selector that returns the epc . */
	public static final EPCSelector selectEPC = new EPCSelector() {
		public EPC select(ECReportGroupListMember member) {
			return member.getEpc();
		}
	};

	/** the default selector. */
	public static final EPCSelector DEFAULT_SELECTOR = selectRawHex;

	/**
	 * checks if a member was retrieved by a given reader.
	 * 
	 * @param readerName the name of the reader to check.
	 * @param member     the member to inspect.
	 * @return true if read from the reader, false otherwise.
	 */
	public static boolean fromReader(String readerName, ECReportGroupListMember member) {
		try {
			for (ECTagStat stat : member.getExtension().getStats().getStat()) {
				for (ECReaderStat rstat : stat.getStatBlocks().getStatBlock()) {
					if (rstat.getReaderName().equals(readerName)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * extracts all the members containing the EPCs from the reports.
	 * 
	 * @param reports the report to "digest".
	 * @return a list of members providing access to the EPC data.
	 */
	public static List<ECReportGroupListMember> extractReportMembers(ECReports reports) {
		List<ECReportGroupListMember> members = new LinkedList<ECReportGroupListMember>();

		if (null == reports)
			return members;
		if (null == reports.getReports())
			return members;
		for (ECReport report : reports.getReports().getReport()) {
			members.addAll(extractReportMembers(report));
		}
		return members;
	}

	/**
	 * extracts all the members containing the EPCs from the report.
	 * 
	 * @param report the report to "digest".
	 * @return a list of members providing access to the EPC data.
	 */
	public static List<ECReportGroupListMember> extractReportMembers(ECReport report) {
		List<ECReportGroupListMember> members = new LinkedList<ECReportGroupListMember>();

		if (null == report)
			return members;
		if (null == report.getGroup())
			return members;
		for (ECReportGroup group : report.getGroup()) {
			if ((null != group) && (null != group.getGroupList()) && (null != group.getGroupList().getMember())) {

				for (ECReportGroupListMember member : group.getGroupList().getMember()) {
					members.add(member);
				}
			}
		}

		return members;
	}

	/**
	 * extracts EPC values from the reports. when the selector is set to
	 * <code>null</code>, then the default selector will be chosen.
	 * 
	 * @param selector the selector selecting the matching EPCs.
	 * @param reports  the reports from where to select the EPCs.
	 * @return returns a list of selected EPCs.
	 */
	public static List<EPC> extractEPC(EPCSelector selector, ECReports reports) {
		List<EPC> epcs = new LinkedList<EPC>();
		if (null == reports.getReports())
			return epcs;
		for (ECReport report : reports.getReports().getReport()) {
			epcs.addAll(extractEPC(selector, report));
		}
		return epcs;
	}

	/**
	 * extracts EPC values from the report. when the selector is set to
	 * <code>null</code>, then the default selector will be chosen.
	 * 
	 * @param selector the selector selecting the matching EPCs.
	 * @param report   the report from where to select the EPCs.
	 * @return returns a list of selected EPCs.
	 */
	public static List<EPC> extractEPC(EPCSelector selector, ECReport report) {
		List<EPC> epcs = new LinkedList<EPC>();
		for (ECReportGroupListMember member : extractReportMembers(report)) {
			EPCSelector s = selector;
			if (null == selector)
				s = DEFAULT_SELECTOR;

			EPC epc = s.select(member);
			if (null != epc)
				epcs.add(epc);
		}
		return epcs;
	}

	/**
	 * helper to prepare a nice pretty print of a whole ECReport.
	 * 
	 * @param report the ECReport to print.
	 * @return a nice pretty print.
	 */
	public static String printReport(ECReport report) {
		StringBuffer buffer = new StringBuffer(String.format("name: %n", report.getReportName()));
		if (null != report.getGroup()) {
			for (ECReportGroup group : report.getGroup()) {
				if (null != group.getGroupList()) {
					for (ECReportGroupListMember member : group.getGroupList().getMember()) {
						buffer.append(printGroupMember(member));
					}
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * helper to prepare a nice pretty print of a group member of a ECReport.
	 * 
	 * @param member the member to print.
	 * @return a nice pretty print.
	 */
	public static String printGroupMember(ECReportGroupListMember member) {
		StringBuffer b = new StringBuffer();
		if (null != member.getEpc()) {
			b.append(String.format("epc: %s\n", member.getEpc().getValue()));
		}
		if (null != member.getTag()) {
			b.append(String.format("tag: %s\n", member.getTag().getValue()));
		}
		if (null != member.getRawDecimal()) {
			b.append(String.format("decimal: %s\n", member.getRawDecimal().getValue()));
		}
		if (null != member.getRawHex()) {
			b.append(String.format("hex: %s\n", member.getRawHex().getValue()));
		}
		b.append("\n");

		return b.toString();
	}

	public static List<EPC> warehouseReportHandler(ECReports reports, String reportName) {
		List<EPC> epcs = new LinkedList<EPC>();
		if (null == reports.getReports())
			return epcs;
		for (ECReport report : reports.getReports().getReport()) {
			if (report.getReportName() == reportName) {
				epcs.addAll(extractEPC(selectTag, report));
			}
		}
		return epcs;
	}

	public static List<EPC> warehouseReportHandler(ECReport report, String reportName) {
		List<EPC> epcs = new LinkedList<EPC>();
		for (ECReportGroupListMember member : extractReportMembers(report)) {
			EPC epc = selectTag.select(member);
			if (null != epc)
				epcs.add(epc);
		}
		return epcs;
	}
}