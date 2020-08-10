package main

import (
	"encoding/json"
	"encoding/xml"
	"fmt"
	"io/ioutil"
	"net/http"
	"strings"
)

// Epcis is the epcis repository object
type Epcis struct {
	uri string
}

// GetObjectEventsbyLocation gets Object Events by Location from the EPCIS repository
func GetObjectEventsbyLocation(location string) ([]byte, error) {
	payload := strings.NewReader(fmt.Sprintf(`
	<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
		<soap:Body>
			<epcisq:Poll xmlns:epcisq="urn:epcglobal:epcis-query:xsd:1">
				<queryName>SimpleEventQuery</queryName>
				<params>
					<param>
						<name>eventType</name>
						<value>
							<string>ObjectEvent</string>
						</value>
					</param>
					<param>
						<name>EQ_bizLocation</name>
						<value>
							<string>%s</string>
						</value>
					</param>
				</params>
			</epcisq:Poll>
		</soap:Body>
	</soap:Envelope>
	`, location))

	client := &http.Client{}
	req, err := http.NewRequest("POST", "http://epcis:8080/epcis-repository-0.5.0/query", payload)

	if err != nil {
		fmt.Println(err)
	}
	req.Header.Add("Content-Type", "text/xml")

	res, err := client.Do(req)
	defer res.Body.Close()
	body, err := ioutil.ReadAll(res.Body)

	b, err := ParseObjectEventEnvelopeJSON(body)

	return b, err
}

// SubscribeEventsbyLocation subscribes to the EPCIS repostiory to obj type events
func SubscribeEventsbyLocation(e Epcis, location string) error {
	payload := strings.NewReader(fmt.Sprintf(`
	<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
		<soap:Body>
			<epcisq:Poll xmlns:epcisq="urn:epcglobal:epcis-query:xsd:1">
				<queryName>SimpleEventQuery</queryName>
				<params>
					<param>
						<name>eventType</name>
						<value>
							<string>ObjectEvent</string>
						</value>
					</param>
					<param>
						<name>EQ_bizLocation</name>
						<value>
							<string>%s</string>
						</value>
					</param>
				</params>
			</epcisq:Poll>
		</soap:Body>
	</soap:Envelope>
	`, location))

	client := &http.Client{}
	req, err := http.NewRequest("POST", e.uri, payload)

	if err != nil {
		fmt.Println(err)
	}
	req.Header.Add("Content-Type", "text/xml")

	res, err := client.Do(req)
	defer res.Body.Close()
	return err
}

// GetObjectEventbyLocationAndTime gets Object Events by location and after time "time" from the ECPIS repository
func GetObjectEventbyLocationAndTime(location string, time string) ([]byte, error) {
	payload := strings.NewReader(fmt.Sprintf(`
	<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
		<soap:Body>
			<ns3:Poll xmlns:ns2="http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader" xmlns:ns3="urn:epcglobal:epcis-query:xsd:1" xmlns:ns4="urn:epcglobal:epcis:xsd:1" xmlns:ns5="urn:epcglobal:epcis-masterdata:xsd:1">
				<queryName>SimpleEventQuery</queryName>
				<params>
					<param>
						<name>eventType</name>
						<value xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="ns3:ArrayOfString">
							<string>ObjectEvent</string>
						</value>
					</param>
					<param>
						<name>EQ_bizLocation</name>
						<value xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="ns3:ArrayOfString">
							<string>%s</string>
						</value>
					</param>
					<param>
						<name>GE_eventTime</name>
						<value xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:dateTime">%s</value>
					</param>
				</params>
			</ns3:Poll>
		</soap:Body>
	</soap:Envelope>
	`, location, time))

	client := &http.Client{}
	req, err := http.NewRequest("POST", "http://epcis:8080/epcis-repository-0.5.0/query", payload)

	if err != nil {
		fmt.Println(err)
	}
	req.Header.Add("Content-Type", "text/xml")

	res, err := client.Do(req)
	defer res.Body.Close()
	body, err := ioutil.ReadAll(res.Body)

	b, err := ParseObjectEventEnvelopeJSON(body)

	return b, err
}

/*
func (e *Epcis) Subscribe() */

// ParseObjectEventEnvelopeJSON parses the xml envelope from de EPCIS repository returns JSON []byte
func ParseObjectEventEnvelopeJSON(data []byte) (b []byte, err error) {
	envelope := Envelope{}
	err = xml.Unmarshal(data, &envelope)
	if err != nil {
		fmt.Println(err)
		return nil, err
	}
	b, err = json.Marshal(envelope)
	return
}

// ParseObjectEventEnvelope parses the xml envelope from de EPCIS repository
func ParseObjectEventEnvelope(data []byte) (Envelope, error) {
	envelope := Envelope{}
	err := xml.Unmarshal(data, &envelope)
	if err != nil {
		fmt.Println(err)
	}

	return envelope, err
}

// ParseEPCISQueryDocument parses the xml EPCISQueryDocument from de EPCIS repository
func ParseEPCISQueryDocument(data []byte) (EPCISQueryDocument, error) {
	epcisdoc := EPCISQueryDocument{}
	err := xml.Unmarshal(data, &epcisdoc)
	if err != nil {
		fmt.Println(err)
	}

	return epcisdoc, err
}
