import React, { useState } from 'react';
import { deleteCampaign, startCampaign, stopCampaign, getStatsForCampaign, updateCampaign } from '../api/campaignService';
import Modal from './Modal';
import buttonStyles from '../styles/buttonStyles';

import { ReactComponent as EditIcon } from '../assets/icons/edit.svg';
import { ReactComponent as TrashIcon } from '../assets/icons/trash.svg';
import { ReactComponent as PlayIcon } from '../assets/icons/play.svg';
import { ReactComponent as PauseIcon } from '../assets/icons/pause.svg';
import { ReactComponent as StatsIcon } from '../assets/icons/stats.svg';

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

    return (
        <div style={styles.grid}>
            {campaigns.map(c => {
                const isActive = c.active.toString() === "true";
                return (
                    <div key={c.id} style={styles.card}>
                        <div style={styles.cardHeader}>
                            <h3 style={styles.cardTitle}>{c.name}</h3>
                            <span style={isActive ? styles.statusActive : styles.statusInactive}>
                                {isActive ? 'Aktywna' : 'Nieaktywna'}
                            </span>
                        </div>
                        <p style={styles.cardDetails}>Produkt: {products.find(p => p.id === c.product)?.name || 'Brak'}</p>
                        <p style={styles.cardDetails}>Budżet: {c.campaign_amount} | Stawka: {c.bid_amount}</p>
                        <div style={styles.cardActions}>
                            <button onClick={() => handleEdit(c)} style={{...buttonStyles.base, ...buttonStyles.secondary}}>
                                <EditIcon width="16" height="16" />
                                <span>Edytuj</span>
                            </button>
                            <button onClick={() => handleDelete(c.id)} style={{...buttonStyles.base, ...buttonStyles.danger}}>
                                <TrashIcon width="16" height="16" />
                                <span>Usuń</span>
                            </button>
                            <button onClick={() => handleToggleStatus(c)} style={isActive ? {...buttonStyles.base, ...buttonStyles.stop} : {...buttonStyles.base, ...buttonStyles.start}}>
                                {isActive ? <PauseIcon width="16" height="16" /> : <PlayIcon width="16" height="16" />}
                                <span>{isActive ? 'Zatrzymaj' : 'Wznów'}</span>
                            </button>
                            <button onClick={() => handleShowStats(c.id)} style={{...buttonStyles.base, ...buttonStyles.secondary}}>
                                <StatsIcon width="16" height="16" />
                                <span>Statystyki</span>
                            </button>
                        </div>
                    </div>
                );
            })}
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
                        {/* Wersja uproszczona, rozbuduj w razie potrzeby */}
                        <input name="name" value={editingCampaign.name} onChange={e => setEditingCampaign(prev => ({...prev, name: e.target.value}))} />
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
    grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: '20px', marginTop: '20px' },
    card: { border: '1px solid #e0e0e0', borderRadius: '8px', padding: '20px', boxShadow: '0 2px 5px rgba(0,0,0,0.05)', background: 'white' },
    cardHeader: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '15px' },
    cardTitle: { margin: 0, fontSize: '1.2rem' },
    cardDetails: { color: '#555', margin: '5px 0' },
    statusActive: { background: '#28a745', color: 'white', padding: '4px 10px', borderRadius: '12px', fontSize: '0.8rem', fontWeight: 'bold' },
    statusInactive: { background: '#6c757d', color: 'white', padding: '4px 10px', borderRadius: '12px', fontSize: '0.8rem', fontWeight: 'bold' },
    cardActions: { display: 'flex', gap: '10px', marginTop: '20px', flexWrap: 'wrap', borderTop: '1px solid #f0f0f0', paddingTop: '20px' },
};

export default CampaignList;