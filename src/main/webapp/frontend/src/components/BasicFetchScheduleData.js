// src/main/webapp/src/components/Schedule.js
import React, { useState } from 'react';

function BasicFetchScheduleData({ handleFetchResponses }) {
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
    } catch (error) {
      console.error('Error fetching schedule:', error);
      setStatus(`Error: ${error.message}`);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Title */}
        <h2 className="text-2xl font-semibold text-gray-700 mb-6">Group Schedule</h2>

        {/* Input and Button */}
        <div className="flex items-center mb-6">
          <input
            type="text"
            value={spreadsheetId}
            onChange={(e) => setSpreadsheetId(e.target.value)}
            placeholder="Enter Spreadsheet ID"
            className="pl-4 pr-4 py-2 w-64 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
          />
          <button
            onClick={fetchSchedule}
            className="ml-4 px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
          >
            Load Schedule
          </button>
        </div>

        {/* Status */}
        <p className="text-sm text-gray-500 mb-4">Status: {status}</p>

        {/* Schedule Table */}
        {scheduleData.length > 0 && (
          <div className="bg-white rounded-lg shadow">
            <div className="p-6 border-b">
              <h3 className="text-lg font-medium">Schedule Data</h3>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full text-sm text-left text-gray-500">
                <thead className="text-xs text-gray-700 uppercase bg-gray-50">
                  <tr>
                    {scheduleData[0].map((_, index) => (
                      <th key={index} scope="col" className="px-6 py-3">
                        Column {index + 1}
                      </th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {scheduleData.map((row, rowIndex) => (
                    <tr key={rowIndex} className="bg-white border-b">
                      {row.map((cell, cellIndex) => (
                        <td
                          key={cellIndex}
                          className="px-6 py-4 border border-gray-300 text-gray-700"
                        >
                          {cell?.toString() || ''}
                        </td>
                      ))}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}
        <button 
            className="mt-4 px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
            onClick={handleFetchResponses}
          >
            Back To Dashboard
        </button>
      </div>
    </div>
  );
}

export default BasicFetchScheduleData;