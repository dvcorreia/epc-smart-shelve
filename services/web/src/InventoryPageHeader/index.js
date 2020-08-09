import React, { useState, Fragment } from 'react'
import { PageHeader, Descriptions, Button, Tag, Menu } from 'antd'
import logo from '../assets/nespresso-business-logo.png'
import store from './fake.json'


const dataOptions = ['movements', 'inventory']

const InventoryPageHeader = () => {
    const [dataShow, setDataShow] = useState(dataOptions[0])
    const [reader] = useState(store.readers[0])

    return (
        <Fragment>
            <PageHeader
                title="Inventory"
                subTitle="Warehouse"
                tags={<Tag color="green">Updated at {new Date().toLocaleString()}</Tag>}
                avatar={{ src: logo, shape: "square" }}
                extra={dataOptions.map(
                    (op, i) => <Button
                        key={i}
                        shape="round"
                        type={dataShow === op ? "primary" : "default"}
                        onClick={() => setDataShow(op)}
                        className="data-option-button"
                        disabled={op === 'inventory' ? true : false}
                    >
                        {op}
                    </Button>
                )}
            >
                <Descriptions size="small" column={3}>
                    <Descriptions.Item label="Location">{store.location}</Descriptions.Item>
                    <Descriptions.Item label="Type">{store.type}</Descriptions.Item>
                    <Descriptions.Item label="Nº Readers">{store.readers.length}</Descriptions.Item>
                </Descriptions>
            </PageHeader>

            <Menu mode="horizontal" selectedKeys={[reader.urn]}>
                {store.readers.map(({ urn, nickname, state }) =>
                    <Menu.Item key={urn} disabled={!state}>
                        {nickname}
                    </Menu.Item>
                )}
                <Menu.Item key={0} disabled>All</Menu.Item>
            </Menu>
        </Fragment>
    )
}

export default InventoryPageHeader