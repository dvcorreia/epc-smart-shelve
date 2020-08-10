var { coffee_inventory } = require('./coffee.json'); //(with path)

export const getEPC = (epcString) => {
    const params = epcString.split(".")
    const taginfo = params[0].split(":")
    return {
        tagType: taginfo[3],
        filter: parseInt(taginfo[4]),
        companyPrefix: parseInt(params[1]),
        itemRef: parseInt(params[2]),
        serial: parseInt(params[3], 10)
    }
}

export const getCoffee = (epc) => {
    for (let i = 0; i < coffee_inventory.length; i++) {
        if (coffee_inventory[i].companyPrefix === epc.companyPrefix && coffee_inventory[i].itemRef === epc.itemRef) {
            return coffee_inventory[i]
        }
    }
}
