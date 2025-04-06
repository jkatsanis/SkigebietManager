import React, { useState } from 'react';
import './App.css';
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
      .then((response) => response.json())  // Parse the JSON response
      .then((data) => setUserTickets(data))  // Update state with the fetched data
      .catch((error) => console.error('Error fetching user tickets:', error));  // Handle errors
  };

  // Fetch all Pisten served by a specific SkiLift
  const fetchSkiLiftPisten = (skiLiftId) => {
    fetch(`${url}/api/skilifts/${skiLiftId}/pisten`)
      .then((response) => response.json())  // Parse the JSON response
      .then((data) => setSkiLiftPisten(data))  // Update state with the fetched data
      .catch((error) => console.error('Error fetching Pisten for SkiLift:', error));  // Handle errors
  };

  // Fetch total number of Pisten for each SkiLift
  const fetchPistenCount = () => {
    fetch(`${url}/api/skilifts/pisten_count`)
      .then((response) => response.json())  // Parse the JSON response
      .then((data) => setPistenCount(data))  // Update state with the fetched data
      .catch((error) => console.error('Error fetching Pisten count:', error));  // Handle errors
  };

  // Fetch users who have tickets for a specific Piste
  const fetchUsersForPiste = (pisteId) => {
    fetch(`${url}/api/pisten/${pisteId}/users`)
      .then((response) => response.json())  // Parse the JSON response
      .then((data) => setUsersForPiste(data))  // Update state with the fetched data
      .catch((error) => console.error('Error fetching users for Piste:', error));  // Handle errors
  };

  // Fetch SkiLifts with their associated Pisten and difficulty level
  const fetchSkiLiftsWithDifficulty = () => {
    fetch(`${url}/api/skilifts/pisten_difficulty`)
      .then((response) => response.json())  // Parse the JSON response
      .then((data) => setSkiLiftsWithDifficulty(data))  // Update state with the fetched data
      .catch((error) => console.error('Error fetching SkiLifts with difficulty:', error));  // Handle errors
  };

  return (
    <div className="App">#

<SkiLiftDisplay/>

    <SkiLiftForm/>

    <hr/>
    <hr/>

    <PistenList />  {/* Use the PistenList component here */}

    <PisteForm/>

      <button onClick={() => fetchUserTickets(1)}>Get Tickets for Benutzer 1 (Complex)</button>
      {userTickets && (
        <div>
          <h3>Tickets for Benutzer 1:</h3>
          <pre>{JSON.stringify(userTickets, null, 2)}</pre>
        </div>
      )}

      <hr/>

      <button onClick={() => fetchSkiLiftPisten(1)}>Get Pisten for SkiLift 1 (Einfach)</button>
      {skiLiftPisten && (
        <div>
          <h3>Pisten for SkiLift 1:</h3>
          <pre>{JSON.stringify(skiLiftPisten, null, 2)}</pre>
        </div>
      )}
      <hr/>

      <button onClick={fetchPistenCount}>Get Total Pisten for each SkiLift (Complex)</button>
      {pistenCount && (
        <div>
          <h3>Total Pisten for each SkiLift:</h3>
          <pre>{JSON.stringify(pistenCount, null, 2)}</pre>
        </div>
      )}
      <hr/>

      <button onClick={() => fetchUsersForPiste(1)}>Get Users for Piste 1 (Complex)</button>
      {usersForPiste && (
        <div>
          <h3>Users for Piste 1:</h3>
          <pre>{JSON.stringify(usersForPiste, null, 2)}</pre>
        </div>
      )}
      <hr/>

      <button onClick={fetchSkiLiftsWithDifficulty}>Get SkiLifts with Difficulty (Einfach)</button>
      {skiLiftsWithDifficulty && (
        <div>
          <h3>SkiLifts with Difficulty:</h3>
          <pre>{JSON.stringify(skiLiftsWithDifficulty, null, 2)}</pre>
        </div>
      )}
    </div>
  );
}

export default App;
