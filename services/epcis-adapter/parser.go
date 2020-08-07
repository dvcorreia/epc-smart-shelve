package main

import (
	"encoding/xml"
)

// Envelope is the Soap app root tag in the response
type Envelope struct {
	XMLName xml.Name `xml:"Envelope" json:"-"`
	Body    body     `xml:"Body"`
}

// EPCISQueryDocument is the root tag in the subscription post requests
type EPCISQueryDocument struct {
	XMLName   xml.Name  `xml:"EPCISQueryDocument" json:"-"`
	EPCISBody epcisbody `xml:"EPCISBody"`
}

type epcisbody struct {
	XMLName      xml.Name     `xml:"EPCISBody" json:"-"`
	QueryResults queryResults `xml:"QueryResults"`
}

type body struct {
	XMLName      xml.Name     `xml:"Body" json:"-"`
	QueryResults queryResults `xml:"QueryResults"`
}

type queryResults struct {
	XMLName        xml.Name    `xml:"QueryResults" json:"-"`
	SubscriptionID string      `xml:"subscriptionID,omitempty" json:"subscriptionID,omitempty"`
	QueryName      string      `xml:"queryName" json:"queryName"`
	ResultsBody    resultsBody `xml:"resultsBody" json:"resultsBody"`
}

type resultsBody struct {
	XMLName   xml.Name  `xml:"resultsBody" json:"-"`
	EventList eventList `xml:"EventList"`
}

type eventList struct {
	XMLName      xml.Name      `xml:"EventList" json:"-"`
	ObjectEvents []objectEvent `xml:"ObjectEvent"`
}

type objectEvent struct {
	XMLName             xml.Name    `xml:"ObjectEvent" json:"-"`
	EventTime           string      `xml:"eventTime" json:"eventTime"`
	RecordTime          string      `xml:"recordTime json:"recordTime"`
	EventTimeZoneOffSet string      `xml:"eventTimeZoneOffset" json:"eventTimeZoneOffset"`
	EpcList             epcList     `xml:"epcList" json:"epcList"`
	Action              string      `xml:"action" json:"action"`
	BizStep             string      `xml:"bizStep" json:"bizStep"`
	Disposition         string      `xml:"disposition" json:"disposition"`
	ReadPoint           readPoint   `xml:"readPoint" json:"readPoint"`
	BizLocation         bizLocation `xml:"bizLocation" json:"bizLocation"`
}

type epcList struct {
	XMLName xml.Name `xml:"epcList" json:"-"`
	Epcs    []string `xml:"epc" json:"epc"`
}

type readPoint struct {
	XMLName xml.Name `xml:"readPoint" json:"-"`
	ID      []string `xml:"id" json:"id"`
}

type bizLocation struct {
	XMLName xml.Name `xml:"bizLocation" json:"-"`
	ID      []string `xml:"id" json:"id"`
}
