import React, { useState, useEffect } from 'react';
import { addCampaign, getAllProducts, getAllCities } from '../api/campaignService';

const AddCampaignForm = ({ onCampaignAdded }) => {
    const [formData, setFormData] = useState({
        name: '',
        description: '',
        product: '',
        keywords: '',
        bid_amount: '',
        campaign_amount: '',
        city: '',
        radius: ''
    });
    const [products, setProducts] = useState([]);
    const [cities, setCities] = useState([]);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const productsRes = await getAllProducts();
                setProducts(productsRes.data);
                const citiesRes = await getAllCities();
                setCities(citiesRes.data);
            } catch (err) {
                setError('Nie udało się załadować danych potrzebnych do formularza.');
            }
        };
        fetchData();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');
        try {
            const payload = {
                ...formData,
                keywords: formData.keywords.split(',').map(k => k.trim()) // Przetwarzanie słów kluczowych
            };
            await addCampaign(payload);
            setSuccess('Kampania dodana pomyślnie!');
            onCampaignAdded(); // Odśwież listę
            // Reset formularza
            setFormData({ name: '', description: '', product: '', keywords: '', bid_amount: '', campaign_amount: '', city: '', radius: '' });
        } catch (err) {
            setError('Błąd podczas dodawania kampanii. Sprawdź, czy wszystkie pola są poprawne.');
            console.error(err);
        }
    };

    return (
        <form onSubmit={handleSubmit} style={campaignFormStyle}>
            <h3>Dodaj nową kampanię</h3>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {success && <p style={{ color: 'green' }}>{success}</p>}
            <div style={formGrid}>
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
                <input name="bid_amount" type="number" value={formData.bid_amount} onChange={handleChange} placeholder="Kwota oferty (np. 1.50)" required />
                <input name="campaign_amount" type="number" value={formData.campaign_amount} onChange={handleChange} placeholder="Budżet kampanii" required />
                <input name="radius" type="number" value={formData.radius} onChange={handleChange} placeholder="Promień (w km)" required />
                <input name="keywords" value={formData.keywords} onChange={handleChange} placeholder="Słowa kluczowe (po przecinku)" />
            </div>
            <button type="submit" style={buttonStyle}>Dodaj Kampanię</button>
        </form>
    );
};

const campaignFormStyle = { border: '1px solid #eee', padding: '15px', borderRadius: '8px', marginTop: '20px' };
const formGrid = { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px', marginBottom: '15px' };
const buttonStyle = { padding: '10px 15px', borderRadius: '4px', border: 'none', backgroundColor: '#28a745', color: 'white', cursor: 'pointer' };

export default AddCampaignForm;