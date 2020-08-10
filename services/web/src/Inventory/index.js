import React from 'react'
/* import InventoryTable from './InventoryTable' */
import MovementsTable from './MovementsTable'

const Inventory = () => {
    return (
        <div>
            <MovementsTable location="urn:epc:id:sgln:76300544.00000.0" />
            {/* <InventoryTable /> */}
        </div>
    )
}

export default Inventory