import React, { useState, useEffect } from 'react';
import { pisteService, skiLiftService } from '../services/apiService';

function PisteForm() {
    const [name, setName] = useState('');
    const [schwierigkeitsgrad, setSchwierigkeitsgrad] = useState('');
    const [laenge, setLaenge] = useState('');
    const [skiliftId, setSkiliftId] = useState('');
    const [skiLifts, setSkiLifts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Load available ski lifts
        const loadSkiLifts = async () => {
            try {
                setLoading(true);
                const data = await skiLiftService.getAll();
                setSkiLifts(data);
            } catch (err) {
                console.error('Error loading ski lifts:', err);
                setError('Failed to load ski lifts');
            } finally {
                setLoading(false);
            }
        };
        loadSkiLifts();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        // Validate required fields
        if (!name || !schwierigkeitsgrad || !laenge || !skiliftId) {
            setError('All fields are required');
            return;
        }

        try {
            setLoading(true);
            const newPiste = {
                name,
                schwierigkeitsgrad,
                laenge: parseFloat(laenge),
                skiLiftId: parseInt(skiliftId)
            };

            await pisteService.create(newPiste);
            
            // Reset form
            setName('');
            setSchwierigkeitsgrad('');
            setLaenge('');
            setSkiliftId('');
        } catch (error) {
            console.error('Error creating Piste:', error);
            setError(error.message || 'Failed to create Piste');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="bg-white rounded-lg p-6">
            <h2 className="text-xl font-semibold text-gray-800 mb-6">Add New Piste</h2>

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
                        placeholder="Enter piste name"
                        disabled={loading}
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Difficulty
                    </label>
                    <select
                        value={schwierigkeitsgrad}
                        onChange={(e) => setSchwierigkeitsgrad(e.target.value)}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-ski-blue focus:border-ski-blue"
                        required
                        disabled={loading}
                    >
                        <option value="">Select difficulty</option>
                        <option value="Leicht">Leicht</option>
                        <option value="Mittel">Mittel</option>
                        <option value="Schwer">Schwer</option>
                    </select>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Length (km)
                    </label>
                    <input
                        type="number"
                        value={laenge}
                        onChange={(e) => setLaenge(e.target.value)}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-ski-blue focus:border-ski-blue"
                        required
                        min="0.1"
                        step="0.1"
                        placeholder="Enter length in kilometers"
                        disabled={loading}
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Ski Lift
                    </label>
                    <select
                        value={skiliftId}
                        onChange={(e) => setSkiliftId(e.target.value)}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-ski-blue focus:border-ski-blue"
                        required
                        disabled={loading}
                    >
                        <option value="">Select a ski lift</option>
                        {skiLifts.map((skiLift) => (
                            <option key={skiLift.id} value={skiLift.id}>
                                {skiLift.name}
                            </option>
                        ))}
                    </select>
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
                                <span>Add Piste</span>
                            </>
                        )}
                    </button>
                </div>
            </form>
        </div>
    );
}

export default PisteForm;
