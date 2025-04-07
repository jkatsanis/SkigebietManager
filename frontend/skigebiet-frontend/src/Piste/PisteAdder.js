import React, { useState } from 'react';
import axios from 'axios';

const url = "http://localhost:8080"; // Replace with your backend API URL

function PisteForm() {
    const [name, setName] = useState('');
    const [schwierigkeitsgrad, setSchwierigkeitsgrad] = useState('');
    const [laenge, setLaenge] = useState('');
    const [skiliftId, setSkiliftId] = useState('');
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSuccess(false);
        setError(null);

        const newPiste = {
            name,
            schwierigkeitsgrad,
            laenge: parseFloat(laenge),
            skiLift: {
                id: parseInt(skiliftId),
            },
        };

        try {
            const response = await axios.post(`${url}/piste`, newPiste);
            console.log('Piste created:', response.data);
            setSuccess(true);
            // Reset form
            setName('');
            setSchwierigkeitsgrad('');
            setLaenge('');
            setSkiliftId('');
        } catch (error) {
            console.error('Error creating Piste:', error);
            setError('Failed to create Piste. Please try again.');
        }
    };

    return (
        <div className="bg-white rounded-lg p-6">
            <h2 className="text-xl font-semibold text-gray-800 mb-6">Add New Piste</h2>
            
            {success && (
                <div className="mb-4 bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative" role="alert">
                    <span className="block sm:inline">Piste successfully created!</span>
                </div>
            )}
            
            {error && (
                <div className="mb-4 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
                    <span className="block sm:inline">{error}</span>
                </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Name
                    </label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-ski-blue focus:border-ski-blue"
                        required
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Schwierigkeitsgrad
                    </label>
                    <select
                        value={schwierigkeitsgrad}
                        onChange={(e) => setSchwierigkeitsgrad(e.target.value)}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-ski-blue focus:border-ski-blue"
                        required
                    >
                        <option value="">Select difficulty</option>
                        <option value="leicht">Leicht</option>
                        <option value="mittel">Mittel</option>
                        <option value="schwer">Schwer</option>
                    </select>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Länge (in meters)
                    </label>
                    <input
                        type="number"
                        value={laenge}
                        onChange={(e) => setLaenge(e.target.value)}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-ski-blue focus:border-ski-blue"
                        min="0"
                        required
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Skilift ID
                    </label>
                    <input
                        type="number"
                        value={skiliftId}
                        onChange={(e) => setSkiliftId(e.target.value)}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-ski-blue focus:border-ski-blue"
                        min="1"
                        required
                    />
                </div>

                <div className="pt-4">
                    <button
                        type="submit"
                        className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-ski-blue"
                    >
                        Add Piste
                    </button>
                </div>
            </form>
        </div>
    );
}

export default PisteForm;
