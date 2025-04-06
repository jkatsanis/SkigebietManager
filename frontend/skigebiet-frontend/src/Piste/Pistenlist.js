import React, { useState, useEffect } from 'react';

const url = "http://localhost:8080"; // Replace with your backend API URL

function PistenList() {
  const [pisten, setPisten] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Fetch all Pisten data from the backend
  const getAllPisten = () => {
    setLoading(true); // Set loading state to true before making the request
    setError(null);   // Reset error state if there's any

    fetch(`${url}/piste`)  // Make a GET request to fetch all Pisten
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to fetch Pisten');
        }
        return response.json();  // Parse the JSON response
      })
      .then((data) => {
        setPisten(data);  // Set the fetched Pisten data to the state
        setLoading(false); // Set loading to false once the data is loaded
        console.log("pisten");
        console.log(pisten);

      })
      .catch((error) => {
        setError(error.message); // Set error message in case of failure
        setLoading(false); // Set loading to false in case of error
      });
  };
  return (
    <div>
      <h1>All Pisten</h1>

      {/* Button to manually fetch Pisten */}
      <button onClick={getAllPisten}>Refresh Pisten</button>

      {/* Loading state */}
      {loading && <p>Loading...</p>}

      {/* Error state */}
      {error && <p>Error: {error}</p>}

      {/* Display list of Pisten */}
      <ul>
        {pisten.map((piste) => (
          <li key={piste.id}>
            <strong>{piste.name}</strong> - Difficulty: {piste.schwierigkeitsgrad}, Length: {piste.laenge} meters
          </li>
        ))}
      </ul>
    </div>
  );
}

export default PistenList;
