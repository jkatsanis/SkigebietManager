import React, { useState } from 'react';
import axios from 'axios';

const url = "http://localhost:8080"; // Replace with your backend API URL

function SkiLiftForm() {
    const [name, setName] = useState('');
    const [typ, setTyp] = useState('');
    const [kapazitaet, setKapazitaet] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Create a new SkiLift object to send in the request
        const newSkiLift = {
            name,
            typ,
            kapazität: parseInt(kapazitaet),  // Make sure it's an integer
        };

        try {
            // Make the POST request to create a new SkiLift
            const response = await axios.post(`${url}/skilift`, newSkiLift);
            console.log('SkiLift created:', response.data);
            // Optionally, clear the form after submission
            setName('');
            setTyp('');
            setKapazitaet('');
        } catch (err) {
            console.error('Error creating SkiLift:', err);
            setError('Failed to create SkiLift. Please try again.');
        }
    };

    return (
        <div>
            <h2>Add a New SkiLift</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <form onSubmit={handleSubmit}>
                <label>
                    Name:
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </label>
                <br />
                <label>
                    Type:
                    <input
                        type="text"
                        value={typ}
                        onChange={(e) => setTyp(e.target.value)}
                        required
                    />
                </label>
                <br />
                <label>
                    Capacity:
                    <input
                        type="number"
                        value={kapazitaet}
                        onChange={(e) => setKapazitaet(e.target.value)}
                        required
                    />
                </label>
                <br />
                <button type="submit">Submit</button>
            </form>
        </div>
    );
}

export default SkiLiftForm;
