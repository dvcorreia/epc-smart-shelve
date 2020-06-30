import React, { useState } from 'react';
import { PageHeader, Tag, Descriptions, Button } from 'antd';
import logo from './assets/nespresso-business-logo.png'
import Data from './Data'
import './App.css';

let time = Date.now()

const App = () => {
  const [time, setTime] = useState(new Date().toLocaleString())

  return (
    <div className="App">
      <PageHeader
        title="Inventory"
        subTitle="Warehouse"
        tags={<Tag color="green">Updated</Tag>}
        avatar={{ src: logo, shape: "square" }}
        extra={[
          <Button key="3" shape="round">Added</Button>,
          <Button key="2" shape="round">Removed</Button>,
          <Button key="1" shape="round" type="primary">
            All
        </Button>,
        ]}
      >
        <Descriptions size="small" column={3}>
          <Descriptions.Item label="Location">Fórum Aveiro, loja 1.10, Rua Homem Christo
3810-536 Aveiro</Descriptions.Item>
          <Descriptions.Item label="Type">Boutique</Descriptions.Item>
          <Descriptions.Item label="Last Updated">{time}</Descriptions.Item>
        </Descriptions>
      </PageHeader>
      <Data />
    </div>
  )
};

export default App;