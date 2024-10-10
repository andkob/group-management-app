import React, {useState} from 'react';
import BasicFetchScheduleData from './components/BasicFetchScheduleData.js';
import Dashboard from './components/Dashboard.js';

function App() {
  console.log('App component rendering');
  const [showSchedule, setShowSchedule] = useState(false);

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