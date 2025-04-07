import React, { useState } from 'react';
import PistenList from './Piste/Pistenlist';
import PisteForm from './Piste/PisteAdder';
import SkiLiftDisplay from './Skilift/SkiLift';
import SkiLiftForm from './Skilift/SkiLiftForm';


const url = "http://localhost:8080"; // Base API URL

function App() {
  const [userTickets, setUserTickets] = useState(null);
  const [skiLiftPisten, setSkiLiftPisten] = useState(null);
  const [pistenCount, setPistenCount] = useState(null);
  const [usersForPiste, setUsersForPiste] = useState(null);
  const [skiLiftsWithDifficulty, setSkiLiftsWithDifficulty] = useState(null);
  
  // New Piste Data
  const [newPiste, setNewPiste] = useState({
    name: '',
    schwierigkeitsgrad: '',
    laenge: '',
    skiLiftId: ''
  });

  // Handle input changes for the Add Piste form
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewPiste(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  // Fetch tickets for a specific Benutzer (user)
  const fetchUserTickets = (userId) => {
    fetch(`${url}/api/tickets/${userId}`)
      .then((response) => response.json())
      .then((data) => setUserTickets(data))
      .catch((error) => console.error('Error fetching user tickets:', error));
  };

  // Fetch all Pisten served by a specific SkiLift
  const fetchSkiLiftPisten = (skiLiftId) => {
    fetch(`${url}/api/skilifts/${skiLiftId}/pisten`)
      .then((response) => response.json())
      .then((data) => setSkiLiftPisten(data))
      .catch((error) => console.error('Error fetching Pisten for SkiLift:', error));
  };

  // Fetch total number of Pisten for each SkiLift
  const fetchPistenCount = () => {
    fetch(`${url}/api/skilifts/pisten_count`)
      .then((response) => response.json())
      .then((data) => setPistenCount(data))
      .catch((error) => console.error('Error fetching Pisten count:', error));
  };

  // Fetch users who have tickets for a specific Piste
  const fetchUsersForPiste = (pisteId) => {
    fetch(`${url}/api/pisten/${pisteId}/users`)
      .then((response) => response.json())
      .then((data) => setUsersForPiste(data))
      .catch((error) => console.error('Error fetching users for Piste:', error));
  };

  // Fetch SkiLifts with their associated Pisten and difficulty level
  const fetchSkiLiftsWithDifficulty = () => {
    fetch(`${url}/api/skilifts/pisten_difficulty`)
      .then((response) => response.json())
      .then((data) => setSkiLiftsWithDifficulty(data))
      .catch((error) => console.error('Error fetching SkiLifts with difficulty:', error));
  };

  return (
    <div className="min-h-screen bg-snow-white">
      <header className="bg-ski-blue text-white shadow-lg py-6 mb-8">
        <div className="container mx-auto px-4">
          <h1 className="text-3xl font-bold">Skigebiet Manager</h1>
        </div>
      </header>

      <main className="container mx-auto px-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
          <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-2xl font-semibold text-ski-blue mb-4">Ski Lifts</h2>
            <SkiLiftDisplay />
            <div className="mt-6">
              <SkiLiftForm />
            </div>
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-2xl font-semibold text-ski-blue mb-4">Pisten</h2>
            <PistenList />
            <div className="mt-6">
              <PisteForm />
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div className="bg-white rounded-lg shadow-md p-6">
            <button 
              onClick={() => fetchUserTickets(1)}
              className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
            >
              Get Tickets for Benutzer 1
            </button>
            {userTickets && (
              <div className="mt-4">
                <h3 className="text-lg font-semibold text-mountain-gray mb-2">Tickets for Benutzer 1:</h3>
                <pre className="bg-gray-100 p-4 rounded-md overflow-x-auto">{JSON.stringify(userTickets, null, 2)}</pre>
              </div>
            )}
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <button 
              onClick={() => fetchSkiLiftPisten(1)}
              className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
            >
              Get Pisten for SkiLift 1
            </button>
            {skiLiftPisten && (
              <div className="mt-4">
                <h3 className="text-lg font-semibold text-mountain-gray mb-2">Pisten for SkiLift 1:</h3>
                <pre className="bg-gray-100 p-4 rounded-md overflow-x-auto">{JSON.stringify(skiLiftPisten, null, 2)}</pre>
              </div>
            )}
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <button 
              onClick={fetchPistenCount}
              className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
            >
              Get Total Pisten for each SkiLift
            </button>
            {pistenCount && (
              <div className="mt-4">
                <h3 className="text-lg font-semibold text-mountain-gray mb-2">Total Pisten for each SkiLift:</h3>
                <pre className="bg-gray-100 p-4 rounded-md overflow-x-auto">{JSON.stringify(pistenCount, null, 2)}</pre>
              </div>
            )}
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <button 
              onClick={() => fetchUsersForPiste(1)}
              className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
            >
              Get Users for Piste 1
            </button>
            {usersForPiste && (
              <div className="mt-4">
                <h3 className="text-lg font-semibold text-mountain-gray mb-2">Users for Piste 1:</h3>
                <pre className="bg-gray-100 p-4 rounded-md overflow-x-auto">{JSON.stringify(usersForPiste, null, 2)}</pre>
              </div>
            )}
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <button 
              onClick={fetchSkiLiftsWithDifficulty}
              className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
            >
              Get SkiLifts with Difficulty
            </button>
            {skiLiftsWithDifficulty && (
              <div className="mt-4">
                <h3 className="text-lg font-semibold text-mountain-gray mb-2">SkiLifts with Difficulty:</h3>
                <pre className="bg-gray-100 p-4 rounded-md overflow-x-auto">{JSON.stringify(skiLiftsWithDifficulty, null, 2)}</pre>
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
}

export default App;
