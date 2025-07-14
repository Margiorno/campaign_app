import React, { useState } from 'react';
import { addCampaign } from '../api/campaignService';

const AddCampaignForm = ({ products, cities, onDataChange }) => {
    const [isFormVisible, setIsFormVisible] = useState(false);

    const initialFormData = {
        name: '', description: '', product: '', keywords: '',
        bid_amount: '', campaign_amount: '', city: '', radius: ''
    };
    
    const [formData, setFormData] = useState(initialFormData);
    const [error, setError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleCancel = () => {
        setIsFormVisible(false);
        setError('');
        setFormData(initialFormData);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsSubmitting(true);
        try {
            const payload = {
                ...formData,
                keywords: formData.keywords.split(',').map(k => k.trim()).filter(k => k)
            };
            await addCampaign(payload);
            onDataChange();
            handleCancel();
        } catch (err) {
            const errorMsg = err.response?.data?.message || 'Błąd podczas dodawania kampanii. Sprawdź, czy wszystkie pola są poprawne.';
            setError(errorMsg);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div style={styles.container}>
            {!isFormVisible ? (
                <button style={styles.showFormButton} onClick={() => setIsFormVisible(true)}>
                    + Dodaj nową kampanię
                </button>
            ) : (
                <form onSubmit={handleSubmit} style={styles.form}>
                    {error && <div style={styles.errorBox}>{error}</div>}

                    <fieldset style={styles.fieldset}>
                        <legend style={styles.legend}>Dane podstawowe</legend>
                        <div className="form-group">
                            <label htmlFor="campaignName" className="form-label">Nazwa kampanii</label>
                            <input id="campaignName" name="name" className="form-input" value={formData.name} onChange={handleChange} required />
                        </div>
                        <div className="form-group">
                             <label htmlFor="campaignDesc" className="form-label">Opis</label>
                            <textarea id="campaignDesc" name="description" className="form-input" value={formData.description} onChange={handleChange} />
                        </div>
                    </fieldset>

                    <fieldset style={styles.fieldset}>
                        <legend style={styles.legend}>Targetowanie i budżet</legend>
                        <div style={styles.formGrid}>
                            <div className="form-group">
                                <label htmlFor="campaignProduct" className="form-label">Produkt</label>
                                <select id="campaignProduct" name="product" className="form-input" value={formData.product} onChange={handleChange} required>
                                    <option value="">Wybierz produkt...</option>
                                    {products.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
                                </select>
                            </div>
                             <div className="form-group">
                                <label htmlFor="campaignCity" className="form-label">Miasto</label>
                                <select id="campaignCity" name="city" className="form-input" value={formData.city} onChange={handleChange} required>
                                    <option value="">Wybierz miasto...</option>
                                    {cities.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                                </select>
                            </div>
                            <div className="form-group">
                                <label htmlFor="campaignBid" className="form-label">Stawka (bid)</label>
                                <input id="campaignBid" name="bid_amount" type="number" step="0.01" className="form-input" value={formData.bid_amount} onChange={handleChange} required />
                            </div>
                            <div className="form-group">
                                <label htmlFor="campaignBudget" className="form-label">Budżet kampanii</label>
                                <input id="campaignBudget" name="campaign_amount" type="number" step="0.01" className="form-input" value={formData.campaign_amount} onChange={handleChange} required />
                            </div>
                             <div className="form-group">
                                <label htmlFor="campaignRadius" className="form-label">Promień (km)</label>
                                <input id="campaignRadius" name="radius" type="number" step="0.1" className="form-input" value={formData.radius} onChange={handleChange} required />
                            </div>
                             <div className="form-group">
                                <label htmlFor="campaignKeywords" className="form-label">Słowa kluczowe (po przecinku)</label>
                                <input id="campaignKeywords" name="keywords" className="form-input" value={formData.keywords} onChange={handleChange} />
                            </div>
                        </div>
                    </fieldset>

                    <div style={styles.actions}>
                        <button type="button" style={styles.cancelButton} onClick={handleCancel}>
                            Anuluj
                        </button>
                        <button type="submit" style={styles.submitButton} disabled={isSubmitting}>
                            {isSubmitting ? 'Dodawanie...' : 'Dodaj Kampanię'}
                        </button>
                    </div>
                </form>
            )}
        </div>
    );
};

const styles = {
    container: { border: '1px solid #eee', padding: '20px', borderRadius: '8px', marginTop: '20px', background: '#fcfcfc' },
    showFormButton: { width: '100%', padding: '12px', fontSize: '1rem', border: '2px dashed #ccc', borderRadius: '8px', background: '#f9f9f9', color: '#555', cursor: 'pointer' },
    form: { display: 'flex', flexDirection: 'column', gap: '20px' },
    fieldset: { border: '1px solid #ddd', borderRadius: '6px', padding: '20px' },
    legend: { padding: '0 10px', fontWeight: 'bold', color: '#333' },
    formGrid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '15px' },
    errorBox: { background: '#ffefef', color: '#c53030', border: '1px solid #fbb2b2', padding: '10px', borderRadius: '6px', textAlign: 'center' },
    actions: { display: 'flex', justifyContent: 'flex-end', gap: '10px', borderTop: '1px solid #eee', paddingTop: '20px', marginTop: '10px' },
    submitButton: { padding: '10px 20px', borderRadius: '5px', border: 'none', background: '#5cb85c', color: 'white', cursor: 'pointer', fontWeight: 'bold' },
    cancelButton: { padding: '10px 20px', borderRadius: '5px', border: '1px solid #ccc', background: '#fff', color: '#333', cursor: 'pointer' },
};

export default AddCampaignForm;