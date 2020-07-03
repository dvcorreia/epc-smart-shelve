import React, { useState } from 'react';
import Inventory from './Inventory'
import InventoryPageHeader from './InventoryPageHeader'
import './App.css';


const App = () => {
  return (
    <div className="App">
      <InventoryPageHeader />
      <Inventory />
    </div>
  )
};

export default App;