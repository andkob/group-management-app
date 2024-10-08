// src/main/webapp/src/components/Schedule.js
import React, { useState } from 'react';

function Schedule() {
  console.log('Schedule component rendering');
  const [scheduleData, setScheduleData] = useState([]);
  const [status, setStatus] = useState('');
  const [spreadsheetId, setSpreadsheetId] = useState('');

  const fetchSchedule = async () => {
    if (!spreadsheetId) {
      setStatus('Please enter a Spreadsheet ID');
      return;
    }

    setStatus('Loading...');
    try {
      const response = await fetch(`/api/schedule?sheetID=${spreadsheetId}`);
      console.log('Response status:', response.status);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const data = await response.json();
      console.log('Received data:', data);
      setScheduleData(data);
      setStatus('Data loaded successfully');
      console.log('Data loaded successfully');
    } catch (error) {
      console.error('Error fetching schedule:', error);
      setStatus(`Error: ${error.message}`);
    }
  };

  return (
    <div>
      <h2>Group Schedule</h2>
      <div>
        <input 
          type="text" 
          value={spreadsheetId} 
          onChange={(e) => setSpreadsheetId(e.target.value)}
          placeholder="Enter Spreadsheet ID"
        />
        <button onClick={fetchSchedule}>Load Schedule</button>
      </div>
      
      <p>Status: {status}</p>
      
      {scheduleData.length > 0 && (
        <div>
          <h3>Schedule Data:</h3>
          <table style={{borderCollapse: 'collapse', width: '100%'}}>
            <tbody>
              {scheduleData.map((row, rowIndex) => (
                <tr key={rowIndex}>
                  {row.map((cell, cellIndex) => (
                    <td 
                      key={cellIndex}
                      style={{
                        border: '1px solid black',
                        padding: '8px',
                        textAlign: 'left'
                      }}
                    >
                      {cell?.toString() || ''}
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default Schedule;