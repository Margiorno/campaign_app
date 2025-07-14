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
            onDataChange(); // Odświeżenie danych w komponencie nadrzędnym
        } catch (err) {
            setError('Błąd podczas dodawania produktu.');
            console.error(err);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <form onSubmit={handleSubmit} style={styles.form}>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <input
                type="text" value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Nazwa produktu" required
                style={styles.input}
            />
            <input
                type="text" value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Opis produktu"
                style={styles.input}
            />
            <button type="submit" style={styles.button} disabled={isSubmitting}>
                {isSubmitting ? 'Dodawanie...' : 'Dodaj Produkt'}
            </button>
        </form>
    );
};

const styles = {
    form: { display: 'flex', gap: '10px', alignItems: 'center', marginBottom: '20px', flexWrap: 'wrap' },
    input: { padding: '10px', borderRadius: '4px', border: '1px solid #ccc', flex: 1, minWidth: '200px' },
    button: { padding: '10px 15px', borderRadius: '4px', border: 'none', backgroundColor: '#0275d8', color: 'white', cursor: 'pointer' },
};

export default AddProductForm;