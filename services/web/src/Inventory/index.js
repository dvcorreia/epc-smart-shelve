import React from 'react'
import InventoryTable from './InventoryTable'
import MovementsTable from './MovementsTable'
import { Table } from 'antd'

const Inventory = () => {
    return (
        <div>
            <InventoryTable />
            <MovementsTable />
        </div>
    )
}

export default Inventory