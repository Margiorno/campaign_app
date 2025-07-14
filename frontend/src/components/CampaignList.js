// src/components/CampaignList.js

import React, { useState } from 'react';
import { deleteCampaign, startCampaign, stopCampaign, getStatsForCampaign } from '../api/campaignService';
import Modal from './Modal';

const CampaignList = ({ campaigns, onDataChange }) => {
    const [stats, setStats] = useState(null);
    const [isStatsModalOpen, setIsStatsModalOpen] = useState(false);

    const handleDelete = async (id) => {
        if (window.confirm('Czy na pewno chcesz usunąć tę kampanię?')) {
            await deleteCampaign(id);
            onDataChange();
        }
    };

    const handleToggleStatus = async (campaign) => {
        // Poprawka: Backend zwraca 'true'/'false' jako stringi
        const action = campaign.active.toString() === "true" ? stopCampaign : startCampaign;
        await action(campaign.id);
        onDataChange();
    };

    const handleShowStats = async (id) => {
        // === KROK DIAGNOSTYCZNY ===
        // Sprawdźmy, co dokładnie jest przekazywane jako ID
        console.log("Próba pobrania statystyk dla ID:", id);

        // Zabezpieczenie przed wysłaniem żądania z nieprawidłowym ID
        if (!id) {
            alert("Błąd: ID kampanii jest nieprawidłowe.");
            return;
        }

        try {
            const response = await getStatsForCampaign(id);
            setStats(response.data);
            setIsStatsModalOpen(true);
        } catch (error) {
            console.error("Błąd podczas pobierania statystyk:", error);
            alert("Nie udało się pobrać statystyk. Sprawdź konsolę deweloperską, aby uzyskać więcej informacji.");
        }
    };

    // Reszta komponentu pozostaje bez zmian...
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
                    {/* Tutaj również upewnijmy się, że ID produktu jest wyświetlane */}
                    <p>Produkt ID: {c.product || 'Brak'}</p>
                    <p>Budżet: {c.campaign_amount} | Stawka: {c.bid_amount}</p>
                    <div style={styles.cardActions}>
                        <button onClick={() => { /* logika edycji */ }}>Edytuj</button>
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
        </div>
    );
};


const styles = {
    grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '20px', marginTop: '20px' },
    card: { border: '1px solid #e0e0e0', borderRadius: '8px', padding: '20px', boxShadow: '0 2px 5px rgba(0,0,0,0.05)', display: 'flex', flexDirection: 'column' },
    cardHeader: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '15px' },
    cardTitle: { margin: 0, fontSize: '1.2rem' },
    statusActive: { background: '#28a745', color: 'white', padding: '4px 10px', borderRadius: '12px', fontSize: '0.8rem', fontWeight: 'bold' },
    statusInactive: { background: '#6c757d', color: 'white', padding: '4px 10px', borderRadius: '12px', fontSize: '0.8rem', fontWeight: 'bold' },
    cardActions: { display: 'flex', gap: '10px', marginTop: '20px', flexWrap: 'wrap' }
};

export default CampaignList;