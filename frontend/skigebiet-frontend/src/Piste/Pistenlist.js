import React, { useState } from 'react';

const url = "http://localhost:8080"; // Replace with your backend API URL

function PistenList() {
  const [pisten, setPisten] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Fetch all Pisten data from the backend
  const getAllPisten = () => {
    setLoading(true);
    setError(null);

    fetch(`${url}/piste`)
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to fetch Pisten');
        }
        return response.json();
      })
      .then((data) => {
        setPisten(data);
        setLoading(false);
      })
      .catch((error) => {
        setError(error.message);
        setLoading(false);
      });
  };

  // Get difficulty color
  const getDifficultyColor = (schwierigkeitsgrad) => {
    switch (schwierigkeitsgrad.toLowerCase()) {
      case 'leicht':
        return 'bg-green-100 text-green-800';
      case 'mittel':
        return 'bg-blue-100 text-blue-800';
      case 'schwer':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <h2 className="text-xl font-semibold text-gray-800">Pisten List</h2>
        <button 
          onClick={getAllPisten}
          className="px-4 py-2 bg-ski-blue text-white rounded-md hover:bg-blue-800 transition-colors flex items-center space-x-2"
        >
          <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
            <path fillRule="evenodd" d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z" clipRule="evenodd" />
          </svg>
          <span>Refresh</span>
        </button>
      </div>

      {loading && (
        <div className="flex justify-center items-center py-8">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-ski-blue"></div>
        </div>
      )}

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
          <strong className="font-bold">Error!</strong>
          <span className="block sm:inline"> {error}</span>
        </div>
      )}

      <div className="grid gap-4">
        {pisten.map((piste) => (
          <div 
            key={piste.id}
            className="bg-white rounded-lg shadow-sm border border-gray-200 p-4 hover:shadow-md transition-shadow"
          >
            <div className="flex justify-between items-start">
              <div>
                <h3 className="text-lg font-semibold text-gray-800">{piste.name}</h3>
                <div className="mt-2 space-x-2">
                  <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getDifficultyColor(piste.schwierigkeitsgrad)}`}>
                    {piste.schwierigkeitsgrad}
                  </span>
                  <span className="text-sm text-gray-500">
                    {piste.laenge}m
                  </span>
                </div>
              </div>
              <button className="text-gray-400 hover:text-gray-600">
                <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                  <path d="M6 10a2 2 0 11-4 0 2 2 0 014 0zM12 10a2 2 0 11-4 0 2 2 0 014 0zM16 12a2 2 0 100-4 2 2 0 000 4z" />
                </svg>
              </button>
            </div>
          </div>
        ))}
      </div>

      {pisten.length === 0 && !loading && !error && (
        <div className="text-center py-8 text-gray-500">
          No pisten found. Click refresh to load the list.
        </div>
      )}
    </div>
  );
}

export default PistenList;
