import React, { useState } from 'react';
import { addProduct } from '../api/campaignService';

const AddProductForm = ({ onProductAdded }) => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');
        if (!name) {
            setError('Nazwa produktu jest wymagana.');
            return;
        }
        try {
            await addProduct({ name, description });
            setSuccess('Produkt został pomyślnie dodany!');
            setName('');
            setDescription('');
            onProductAdded(); // Odświeżenie listy produktów
        } catch (err) {
            setError('Błąd podczas dodawania produktu.');
            console.error(err);
        }
    };

    return (
        <form onSubmit={handleSubmit} style={formStyle}>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {success && <p style={{ color: 'green' }}>{success}</p>}
            <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Nazwa produktu"
                required
                style={inputStyle}
            />
            <input
                type="text"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Opis produktu"
                style={inputStyle}
            />
            <button type="submit" style={buttonStyle}>Dodaj Produkt</button>
        </form>
    );
};

const formStyle = { display: 'flex', gap: '10px', alignItems: 'center', marginBottom: '20px' };
const inputStyle = { padding: '8px', borderRadius: '4px', border: '1px solid #ccc', flex: 1 };
const buttonStyle = { padding: '8px 12px', borderRadius: '4px', border: 'none', backgroundColor: '#007bff', color: 'white', cursor: 'pointer' };

export default AddProductForm;