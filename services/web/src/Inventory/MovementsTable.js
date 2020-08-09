import React, { useEffect, useState } from 'react'
import { Timeline } from 'antd'
import { DropboxOutlined } from '@ant-design/icons'
import movements from './fakemovements.json'
import getCoffee from './../libs/epc'
import ColumnGroup from 'antd/lib/table/ColumnGroup'

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
            if (grouping[j].epc === epcs[i]) {
                ++grouping[j].n
                isIn = true
                break
            }
        }

        if (!isIn) {
            grouping.push({
                epc: epcs[i],
                n: 1
            })
        }
    }

    return grouping
}

const MovementsTable = () => {
    const [data, setData] = useState([])

    useEffect(() => {
        let events = movements["Body"]["QueryResults"]["resultsBody"]["EventList"]["ObjectEvents"]
        setData(sortEvents(events))
        console.log(events)
    }, [])

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
                                groupEPCs(e.epcList.epc).map((t) => {
                                    const cof = getCoffee(t.epc)
                                    return <p key={cof.itemRef}>{cof.name} {cof.packagingLvl > 1 ? <DropboxOutlined /> : ""}</p>
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