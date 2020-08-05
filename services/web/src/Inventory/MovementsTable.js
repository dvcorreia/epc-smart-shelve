import React, { useEffect, useState } from 'react'

const MovementsTable = () => {
    const [data, setData] = useState('none')

    const getData = () => {
        let myHeaders = new Headers();
        myHeaders.append("Content-Type", "text/xml");
        myHeaders.append("Access-Control-Allow-Origin", "*")

        const raw = `<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><epcisq:Poll xmlns:epcisq="urn:epcglobal:epcis-query:xsd:1"><queryName>SimpleEventQuery</queryName><params><param><name>eventType</name><value><string>ObjectEvent</string></value></param><param><name>MATCH_epc</name><value><string>urn:epc:tag:sgtin-96:1.76300544.07470.1</string></value></param></params></epcisq:Poll></soap:Body></soap:Envelope>`

        const requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: raw,
            redirect: 'follow'
        };

        fetch("http://localhost:4080/epcis-repository-0.5.0/query", requestOptions)
            .then(response => response.text())
            .then(result => console.log(result))
            .catch(error => console.log('error', error));
    }

    useEffect(() => {
        getData()
    })

    return (
        <p>{data}</p>
    )
}

export default MovementsTable