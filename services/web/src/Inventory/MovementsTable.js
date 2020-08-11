import React, { useEffect, useState } from 'react'
import { Timeline } from 'antd'
import { DropboxOutlined } from '@ant-design/icons'
import { getCoffee, getEPC } from './../libs/epc'

let reloadFlag

const MovementsTable = ({ location }) => {
    const [data, setData] = useState([])
    const [time, setTime] = useState(new Date())

    function processEventColor(bizStep) {
        switch (bizStep) {
            case "urn:epcglobal:cbv:bizstep:storing":
                return "green"
            case "urn:epcglobal:cbv:bizstep:unpacking":
                return "red"
            default:
                return "grey"
        }
    }

    function sortEvents(events) {
        if (events === null) {
            return null
        }

        let sorted = events.sort((a, b) => {
            // Turn your strings into dates, and then subtract them
            // to get a value that is either negative, positive, or zero.
            return new Date(b.eventTime) - new Date(a.eventTime)
        })
        return sorted
    }

    function processTime(timeString) {
        const date = new Date(timeString)
        return date.toLocaleString('en-GB', { timeZone: 'UTC' })
    }

    function groupEPCs(epcs) {
        var grouping = [];

        for (let i = 0; i < epcs.length; i++) {
            let isIn = false
            for (let j = 0; j < grouping.length; j++) {
                if (grouping[j].itemRef === epcs[i].itemRef) {
                    ++grouping[j].n
                    grouping[j].serials.push(epcs[i].serial)
                    isIn = true
                    break
                }
            }

            if (!isIn) {
                const { tagTypem, serial, ...ProductInfo } = epcs[i]
                grouping.push({
                    serials: [epcs[i].serial],
                    n: 1,
                    ...ProductInfo
                })
            }
        }

        return grouping
    }

    function processEvent(event) {
        const epcs = groupEPCs(event.epcList.epc.map(e => getEPC(e)))

        const eventReturn = {
            eventTime: event.eventTime,
            bizStep: event.bizStep,
            disposition: event.disposition,
            readPoint: event.readPoint.id[0],
            bizLocation: event.bizLocation.id[0],
            epcs: epcs
        }

        return eventReturn
    }

    async function fetchData(location, time = null) {
        let url = `/query/location?location=${location}${time !== null ? "&time=" + time : ""}`
        const events = await (await fetch(url))
            .json()
            .then(movements => movements["Body"]["QueryResults"]["resultsBody"]["EventList"]["ObjectEvents"])
            .then(events => sortEvents(events))
            .then(events => events !== null ? events.map(event => processEvent(event)) : null)
        return events
    }

    const loadData = () => {
        setTimeout(() => {
            if (reloadFlag) {
                setTime(new Date())
                console.log("Loading data ...")
                fetchData(location, time.toISOString())
                    .then(events => events !== null ? setData([...events, ...data]) : console.log("No new data"))
                loadData()
            }
        }, 5000);
    }

    useEffect(() => {
        reloadFlag = true
        console.log("Fetching initial Data ...")
        fetchData(location).then(events => setData([...events, ...data]))

        loadData()

        return () => {
            reloadFlag = false
        }
    }, [location])

    return (
        <div>
            <Timeline mode="left">
                {
                    data.map((e, i) => {
                        return <Timeline.Item
                            key={i}
                            color={processEventColor(e.bizStep)}
                            label={processTime(e.eventTime)}
                        >
                            {
                                e.epcs.map((epc) => {
                                    const cof = getCoffee(epc)
                                    return <p key={cof.itemRef}>[{epc.serials.toString()}] {cof.name} {epc.filter > 1 ? <DropboxOutlined /> : ""} {epc.n > 1 ? " [x" + epc.n + "]" : ""}</p>
                                })
                            }
                        </Timeline.Item>
                    })
                }
            </Timeline>
        </div>
    )
}

export default MovementsTable