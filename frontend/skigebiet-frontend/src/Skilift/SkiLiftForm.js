import React, { useState } from 'react';
import { skiLiftService } from '../services/apiService';

function SkiLiftForm({ onSkiLiftAdded }) {
    const [name, setName] = useState('');
    const [typ, setTyp] = useState('');
    const [kapazitaet, setKapazitaet] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        // Validate required fields
        if (!name || !typ || !kapazitaet) {
            setError('All fields are required');
            return;
        }

        try {
            setLoading(true);
            const newSkiLift = {
                name,
                typ,
                kapazitaet: parseInt(kapazitaet)
            };

            await skiLiftService.create(newSkiLift);
            
            // Reset form
            setName('');
            setTyp('');
            setKapazitaet('');

            // Notify parent component to refresh ski lifts
            if (onSkiLiftAdded) {
                onSkiLiftAdded();
            }
        } catch (error) {
            console.error('Error creating SkiLift:', error);
            setError(error.message || 'Failed to create Ski Lift');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="bg-white rounded-lg p-6">
            <h2 className="text-xl font-semibold text-gray-800 mb-6">Add New Ski Lift</h2>

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
                        placeholder="Enter ski lift name"
                        disabled={loading}
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Type
                    </label>
                    <select
                        value={typ}
                        onChange={(e) => setTyp(e.target.value)}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-ski-blue focus:border-ski-blue"
                        required
                        disabled={loading}
                    >
                        <option value="">Select a type</option>
                        <option value="Sessellift">Sessellift</option>
                        <option value="Schlepplift">Schlepplift</option>
                    </select>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Capacity (persons/hour)
                    </label>
                    <input
                        type="number"
                        value={kapazitaet}
                        onChange={(e) => setKapazitaet(e.target.value)}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-ski-blue focus:border-ski-blue"
                        required
                        min="1"
                        placeholder="Enter capacity"
                        disabled={loading}
                    />
                </div>

                <div>
                    <button
                        type="submit"
                        className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-ski-blue flex items-center justify-center space-x-2 disabled:opacity-50"
                        disabled={loading}
                    >
                        {loading ? (
                            <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                        ) : (
                            <>
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                                    <path fillRule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clipRule="evenodd" />
                                </svg>
                                <span>Add Ski Lift</span>
                            </>
                        )}
                    </button>
                </div>
            </form>
        </div>
    );
}

export default SkiLiftForm;
