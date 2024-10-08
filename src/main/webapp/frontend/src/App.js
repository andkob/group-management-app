import React from 'react';
import Schedule from './components/Schedule.js';
// import './FakeApp.css';

function App() {
  console.log('App component rendering');
  return (
    <div className="App">
      <header className="App-header">
        <h1>Group Schedule App</h1>
      </header>
      <main style={{padding: '20px'}}>
        <p>Debug text - Should appear below header</p>
        <Schedule />
      </main>
    </div>
  );
}

export default App;