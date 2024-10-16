import React, {useState} from 'react';
import BasicFetchScheduleData from './components/BasicFetchScheduleData.js';
import Dashboard from './components/Dashboard.js';

function App() {
  console.log('App component rendering');
  const [showSchedule, setShowSchedule] = useState(false);

  // TEMPORARY: load schedule data into in-memory database
  const testSheetID = "1ZXZwDDLnk7MCthMLQ-N6noKPqVf-10pf_2Qv_KxEbKM";

  const storeScheduleData = async () => {
    const response = await fetch(`/api/store-schedule?sheetID=${testSheetID}`);
    if (response.ok) {
      console.log("schedule data stored in database");
    } else {
      console.error("error loading schedule data");
    }
  }
  storeScheduleData();
  // END TEMPORARY

  const handleFetchResponses = () => {
    if (!showSchedule)
      setShowSchedule(true); // Show the Schedule component
    else
      setShowSchedule(false);
  };

  return (
    <div className="App">
      {showSchedule ? (
        <BasicFetchScheduleData handleFetchResponses={handleFetchResponses}/>
      ) : (
        <Dashboard handleFetchResponses={handleFetchResponses} />
      )}
    </div>
  );
}

export default App;