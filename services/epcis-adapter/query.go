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
func (e *Epcis) GetObjectEventsbyLocation(location string) ([]byte, error) {
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
						<name>MATCH_epc</name>
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
	body, err := ioutil.ReadAll(res.Body)

	envelope := Envelope{}
	err = xml.Unmarshal(body, &envelope)
	if err != nil {
		fmt.Println(err)
		return nil, err
	}

	b, err := json.Marshal(envelope)

	return b, err
}

//func (e *Epcis) Subscribe()
