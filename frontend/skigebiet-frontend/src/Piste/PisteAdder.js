import React, { useState } from 'react';
import axios from 'axios';

const url = "http://localhost:8080"; // Replace with your backend API URL

function PisteForm() {
    const [name, setName] = useState('');
    const [schwierigkeitsgrad, setSchwierigkeitsgrad] = useState('');
    const [laenge, setLaenge] = useState('');
    const [skiliftId, setSkiliftId] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

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
        } catch (error) {
            console.error('Error creating Piste:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>
                Name:
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
            </label>
            <label>
                Schwierigskeitsgrad:
                <input
                    type="text"
                    value={schwierigkeitsgrad}
                    onChange={(e) => setSchwierigkeitsgrad(e.target.value)}
                />
            </label>
            <label>
                Laenge:
                <input
                    type="number"
                    value={laenge}
                    onChange={(e) => setLaenge(e.target.value)}
                />
            </label>
            <label>
                Skilift ID:
                <input
                    type="number"
                    value={skiliftId}
                    onChange={(e) => setSkiliftId(e.target.value)}
                />
            </label>
            <button type="submit">Submit</button>
        </form>
    );
}

export default PisteForm;
