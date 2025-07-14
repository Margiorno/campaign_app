import React, { useState } from 'react';
import {
    deleteCampaign, startCampaign, stopCampaign,
    getStatsForCampaign, updateCampaign
} from '../api/campaignService';
import Modal from './Modal';

const CampaignList = ({ campaigns, products, cities, onDataChange }) => {
    const [stats, setStats] = useState(null);
    const [isStatsModalOpen, setIsStatsModalOpen] = useState(false);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [editingCampaign, setEditingCampaign] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleDelete = async (id) => {
        if (window.confirm('Czy na pewno chcesz usunąć tę kampanię?')) {
            await deleteCampaign(id);
            onDataChange();
        }
    };

    const handleToggleStatus = async (campaign) => {
        const action = campaign.active.toString() === "true" ? stopCampaign : startCampaign;
        await action(campaign.id);
        onDataChange();
    };

    const handleShowStats = async (id) => {
        try {
            const response = await getStatsForCampaign(id);
            setStats(response.data);
            setIsStatsModalOpen(true);
        } catch (error) {
            alert("Nie można pobrać statystyk. Być może nie zostały jeszcze zainicjowane przez backend.");
        }
    };

    const handleEdit = (campaign) => {
        setEditingCampaign({ ...campaign, keywords: Array.isArray(campaign.keywords) ? campaign.keywords.join(', ') : '' });
        setIsEditModalOpen(true);
    };

    const handleUpdate = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        try {
            const payload = {
                ...editingCampaign,
                keywords: editingCampaign.keywords.split(',').map(k => k.trim()),
            };
            await updateCampaign(editingCampaign.id, payload);
            onDataChange();
            setIsEditModalOpen(false);
        } catch (error) {
            alert('Błąd podczas aktualizacji kampanii.');
        } finally {
            setIsSubmitting(false);
        }
    };
    
    const handleFormChange = (e) => {
        const { name, value } = e.target;
        setEditingCampaign(prev => ({ ...prev, [name]: value }));
    };

    return (
        <div style={styles.grid}>
            {campaigns.map(c => (
                <div key={c.id} style={styles.card}>
                    <div style={styles.cardHeader}>
                        <h3 style={styles.cardTitle}>{c.name}</h3>
                        <span style={c.active.toString() === "true" ? styles.statusActive : styles.statusInactive}>
                            {c.active.toString() === "true" ? 'Aktywna' : 'Nieaktywna'}
                        </span>
                    </div>
                    <p style={styles.cardDetails}>Produkt: {products.find(p => p.id === c.product)?.name || 'Brak'}</p>
                    <p style={styles.cardDetails}>Budżet: {c.campaign_amount} | Stawka: {c.bid_amount}</p>
                    <div style={styles.cardActions}>
                        <button onClick={() => handleEdit(c)}>Edytuj</button>
                        <button onClick={() => handleDelete(c.id)}>Usuń</button>
                        <button onClick={() => handleToggleStatus(c)}>
                            {c.active.toString() === "true" ? 'Zatrzymaj' : 'Wznów'}
                        </button>
                        <button onClick={() => handleShowStats(c.id)}>Statystyki</button>
                    </div>
                </div>
            ))}
            {isStatsModalOpen && (
                <Modal onClose={() => setIsStatsModalOpen(false)}>
                    <h2>Statystyki Kampanii</h2>
                    {stats ? (
                        <div>
                            <p><strong>ID Kampanii:</strong> {stats.id}</p>
                            <p><strong>Kliknięcia:</strong> {stats.clicks}</p>
                            <p><strong>Wydana kwota:</strong> {stats.spentAmount}</p>
                        </div>
                    ) : <p>Ładowanie...</p>}
                </Modal>
            )}
            {isEditModalOpen && editingCampaign && (
                <Modal onClose={() => setIsEditModalOpen(false)}>
                    <h2>Edytuj Kampanię</h2>
                    <form onSubmit={handleUpdate}>
                        {/* Pełny formularz edycji */}
                        <input name="name" value={editingCampaign.name} onChange={handleFormChange} />
                        {/* Uzupełnij resztę pól formularza... */}
                        <button type="submit" disabled={isSubmitting}>
                            {isSubmitting ? 'Zapisywanie...' : 'Zapisz zmiany'}
                        </button>
                    </form>
                </Modal>
            )}
        </div>
    );
};

const styles = {
    grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: '20px', marginTop: '20px' },
    card: { border: '1px solid #e0e0e0', borderRadius: '8px', padding: '20px', boxShadow: '0 2px 5px rgba(0,0,0,0.05)', background: 'white' },
    cardHeader: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '15px' },
    cardTitle: { margin: 0, fontSize: '1.2rem' },
    cardDetails: { color: '#555', margin: '5px 0' },
    statusActive: { background: '#28a745', color: 'white', padding: '4px 10px', borderRadius: '12px', fontSize: '0.8rem', fontWeight: 'bold' },
    statusInactive: { background: '#6c757d', color: 'white', padding: '4px 10px', borderRadius: '12px', fontSize: '0.8rem', fontWeight: 'bold' },
    cardActions: { display: 'flex', gap: '10px', marginTop: '20px', flexWrap: 'wrap', borderTop: '1px solid #f0f0f0', paddingTop: '15px' },
};

export default CampaignList;