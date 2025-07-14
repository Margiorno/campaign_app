import React, { useState } from 'react';
import { addProduct } from '../api/campaignService';

const AddProductForm = ({ onDataChange }) => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [error, setError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        if (!name) {
            setError('Nazwa produktu jest wymagana.');
            return;
        }
        setIsSubmitting(true);
        try {
            await addProduct({ name, description });
            setName('');
            setDescription('');
            onDataChange();
        } catch (err) {
            setError('Błąd podczas dodawania produktu.');
            console.error(err);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <form onSubmit={handleSubmit} style={styles.form}>
            {error && <p style={{ color: 'red', width: '100%', textAlign: 'center' }}>{error}</p>}
            
            <div className="form-group" style={{ flex: 1, minWidth: '250px' }}>
                <label htmlFor="productName" className="form-label">Nazwa produktu</label>
                <input
                    id="productName"
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="np. Opony zimowe premium"
                    required
                    className="form-input"
                />
            </div>

            <div className="form-group" style={{ flex: 1, minWidth: '250px' }}>
                <label htmlFor="productDesc" className="form-label">Opis (opcjonalnie)</label>
                <input
                    id="productDesc"
                    type="text"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    placeholder="np. Rozmiar 205/55 R16"
                    className="form-input"
                />
            </div>

            <button type="submit" style={styles.button} disabled={isSubmitting}>
                {isSubmitting ? 'Dodawanie...' : 'Dodaj Produkt'}
            </button>
        </form>
    );
};

const styles = {
    form: { display: 'flex', gap: '15px', alignItems: 'flex-end', flexWrap: 'wrap', marginBottom: '20px' },
    button: { 
        padding: '10px 15px', 
        height: '45px', // Dopasuj wysokość do pola tekstowego
        borderRadius: '6px', 
        border: 'none', 
        backgroundColor: '#0275d8', 
        color: 'white', 
        cursor: 'pointer',
        fontWeight: '500',
    },
};

export default AddProductForm;