import React, { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { getPublicActiveCampaigns, registerPublicClick } from '../api/campaignService';

const SimulationPage = () => {
    const [campaigns, setCampaigns] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [clickFeedback, setClickFeedback] = useState({});

    const fetchCampaigns = useCallback(async () => {
        setLoading(true);
        try {
            const response = await getPublicActiveCampaigns();
            setCampaigns(response.data);
        } catch (err) {
            setError('Nie udało się pobrać aktywnych kampanii.');
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchCampaigns();
    }, [fetchCampaigns]);

    const handleCampaignClick = async (id) => {
        setClickFeedback(prev => ({ ...prev, [id]: { loading: true } }));
        try {
            await registerPublicClick(id);
            setClickFeedback(prev => ({ ...prev, [id]: { success: 'Kliknięcie zarejestrowane!' } }));
        } catch (err) {
            const errorMsg = err.response?.data?.message || 'Wystąpił błąd.';
            setClickFeedback(prev => ({ ...prev, [id]: { error: errorMsg } }));
            if (errorMsg === "Not enough funds") {
                fetchCampaigns();
            }
        } finally {
            setTimeout(() => {
                setClickFeedback(prev => ({ ...prev, [id]: null }));
            }, 3000);
        }
    };

    if (loading) return <div style={styles.centered}>Ładowanie symulacji...</div>;
    if (error) return <div style={{...styles.centered, color: 'red'}}>{error}</div>;

    return (
        <div style={styles.page}>
            <header style={styles.header}>
                <h1>Symulator Kliknięć Kampanii</h1>
                <Link to="/login" style={styles.backLink}>Powrót do logowania</Link>
            </header>
            <main style={styles.main}>
                <h2>Aktywne kampanie ({campaigns.length})</h2>
                <p> Ta strona symuluje działanie kampanii reklamowej w czasie rzeczywistym.
                    Każde kliknięcie przycisku "Click!" wywołuje logikę biznesową, która zwiększa sumę wydatków kampanii o wartość stawki (bid)
                    Jeśli łączne wydatki przekroczą zadeklarowany budżet, kampania zostaje automatycznie zatrzymana 
                    i znika z listy aktywnych – co odzwierciedla pełny cykl życia kampanii przy założonym limicie środków. </p>
                {campaigns.length === 0 ? (
                    <p style={styles.centered}>Brak aktywnych kampanii do wyświetlenia.</p>
                ) : (
                    <div style={styles.grid}>
                        {campaigns.map(c => (
                            <div key={c.id} style={styles.card}>
                                <h3 style={styles.cardTitle}>{c.name}</h3>
                                <p style={styles.cardDetails}>Budżet: {c.campaign_amount} | Stawka za klik: {c.bid_amount}</p>
                                <button
                                    style={styles.clickButton}
                                    onClick={() => handleCampaignClick(c.id)}
                                    disabled={clickFeedback[c.id]?.loading}
                                >
                                    Click!
                                </button>
                                {clickFeedback[c.id]?.success && <p style={styles.feedbackSuccess}>{clickFeedback[c.id].success}</p>}
                                {clickFeedback[c.id]?.error && <p style={styles.feedbackError}>{clickFeedback[c.id].error}</p>}
                            </div>
                        ))}
                    </div>
                )}
            </main>
        </div>
    );
};

const styles = {
    page: { background: '#f4f7f6', minHeight: '100vh', fontFamily: 'Arial, sans-serif' },
    header: { background: 'white', padding: '20px 40px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' },
    backLink: { textDecoration: 'none', color: '#0275d8', fontWeight: '500' },
    main: { padding: '40px' },
    centered: { textAlign: 'center', fontSize: '1.2rem', color: '#666', marginTop: '50px' },
    grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '20px', marginTop: '20px' },
    card: { background: 'white', borderRadius: '8px', padding: '20px', boxShadow: '0 2px 5px rgba(0,0,0,0.08)', textAlign: 'center' },
    cardTitle: { margin: '0 0 15px 0' },
    cardDetails: { margin: '5px 0', color: '#555' },
    clickButton: { width: '100%', padding: '12px', marginTop: '15px', borderRadius: '6px', border: 'none', background: '#5cb85c', color: 'white', cursor: 'pointer', fontSize: '1rem', fontWeight: 'bold' },
    feedbackSuccess: { color: '#28a745', marginTop: '10px', fontSize: '14px', fontWeight: '500' },
    feedbackError: { color: '#d9534f', marginTop: '10px', fontSize: '14px', fontWeight: '500' },
};

export default SimulationPage;