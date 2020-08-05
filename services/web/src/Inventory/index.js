import React from 'react'
import InventoryTable from './InventoryTable'
import MovementsTable from './MovementsTable'
import { Table } from 'antd'

const Inventory = () => {
    return (
        <div>
            <MovementsTable />
            {/* <InventoryTable /> */}
        </div>
    )
}

export default Inventory