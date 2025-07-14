import React, { useState } from 'react';
import { addCampaign } from '../api/campaignService';

const AddCampaignForm = ({ products, cities, onDataChange }) => {
    const [formData, setFormData] = useState({
        name: '', description: '', product: '', keywords: '',
        bid_amount: '', campaign_amount: '', city: '', radius: ''
    });
    const [error, setError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsSubmitting(true);
        try {
            const payload = {
                ...formData,
                keywords: formData.keywords.split(',').map(k => k.trim())
            };
            await addCampaign(payload);
            onDataChange();
            setFormData({
                name: '', description: '', product: '', keywords: '',
                bid_amount: '', campaign_amount: '', city: '', radius: ''
            });
        } catch (err) {
            setError('Błąd podczas dodawania kampanii. Sprawdź, czy wszystkie pola są poprawne.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <form onSubmit={handleSubmit} style={styles.form}>
            <h3 style={styles.heading}>Dodaj nową kampanię</h3>
            {error && <p style={{ color: 'red', gridColumn: '1 / -1' }}>{error}</p>}
            <div style={styles.formGrid}>
                <input name="name" value={formData.name} onChange={handleChange} placeholder="Nazwa kampanii" required />
                <input name="description" value={formData.description} onChange={handleChange} placeholder="Opis" />
                <select name="product" value={formData.product} onChange={handleChange} required>
                    <option value="">Wybierz produkt</option>
                    {products.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
                </select>
                <select name="city" value={formData.city} onChange={handleChange} required>
                    <option value="">Wybierz miasto</option>
                    {cities.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                </select>
                <input name="bid_amount" type="number" step="0.01" value={formData.bid_amount} onChange={handleChange} placeholder="Kwota oferty (np. 1.50)" required />
                <input name="campaign_amount" type="number" step="0.01" value={formData.campaign_amount} onChange={handleChange} placeholder="Budżet kampanii" required />
                <input name="radius" type="number" step="0.1" value={formData.radius} onChange={handleChange} placeholder="Promień (km)" required />
                <input name="keywords" value={formData.keywords} onChange={handleChange} placeholder="Słowa kluczowe (po przecinku)" />
            </div>
            <button type="submit" style={styles.button} disabled={isSubmitting}>
                {isSubmitting ? 'Dodawanie...' : 'Dodaj Kampanię'}
            </button>
        </form>
    );
};

const styles = {
    form: { border: '1px solid #eee', padding: '20px', borderRadius: '8px', marginTop: '20px', background: '#fcfcfc' },
    heading: { marginTop: 0, borderBottom: '1px solid #eee', paddingBottom: '10px' },
    formGrid: { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px', marginBottom: '15px' },
    button: { padding: '10px 15px', borderRadius: '4px', border: 'none', backgroundColor: '#5cb85c', color: 'white', cursor: 'pointer' },
};

export default AddCampaignForm;