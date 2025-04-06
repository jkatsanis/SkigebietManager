import React, { useState, useEffect } from 'react';
import axios from 'axios';

const url = "http://localhost:8080"; // Replace with your backend API URL

function SkiLiftDisplay() {
    const [skiLifts, setSkiLifts] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        // Fetch the list of SkiLifts from the backend API when the component mounts
        const fetchSkiLifts = async () => {
            try {
                const response = await axios.get(`${url}/skilift`);
                setSkiLifts(response.data); // Set the retrieved data in state
            } catch (err) {
                console.error('Error fetching SkiLifts:', err);
                setError('Failed to fetch SkiLifts. Please try again later.');
            }
        };

        fetchSkiLifts(); // Call the function to fetch data
    }, []); // Empty dependency array ensures this runs only once when the component mounts

    return (
        <div>
            <h2>SkiLifts</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Type</th>
                        <th>Capacity</th>
                    </tr>
                </thead>
                <tbody>
                    {skiLifts.length > 0 ? (
                        skiLifts.map((skiLift) => (
                            <tr key={skiLift.id}>
                                <td>{skiLift.name}</td>
                                <td>{skiLift.typ}</td>
                                <td>{skiLift.kapazität}</td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="3">No SkiLifts found</td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
}

export default SkiLiftDisplay;
