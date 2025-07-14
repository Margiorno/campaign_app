import React, { useState, useEffect, useCallback } from 'react';
import { useAuth } from '../hooks/useAuth';
import { getAllProducts, getAllCampaigns, getAllCities } from '../api/campaignService';

import ProductList from '../components/ProductList';
import CampaignList from '../components/CampaignList';
import UserManagement from '../components/UserManagement';
import AddProductForm from '../components/AddProductForm';
import AddCampaignForm from '../components/AddCampaignForm';
import styles from './DashboardStyles';

const DashboardPage = () => {
    const { user, logout } = useAuth();

    // Centralne zarządzanie stanem
    const [products, setProducts] = useState([]);
    const [campaigns, setCampaigns] = useState([]);
    const [cities, setCities] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    // Funkcja do pobierania wszystkich danych, owinięta w useCallback dla optymalizacji
    const fetchData = useCallback(async () => {
        // Nie ustawiamy ładowania na true, aby uniknąć migotania przy odświeżaniu
        try {
            const [productsRes, campaignsRes, citiesRes] = await Promise.all([
                getAllProducts(),
                getAllCampaigns(),
                getAllCities()
            ]);
            setProducts(productsRes.data);
            setCampaigns(campaignsRes.data);
            setCities(citiesRes.data);
            setError('');
        } catch (err) {
            setError('Nie udało się pobrać danych. Spróbuj odświeżyć stronę.');
            console.error(err);
        } finally {
            setLoading(false); // Wyłącz ładowanie po zakończeniu
        }
    }, []);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    if (loading) {
        return <div style={styles.centered}>Ładowanie danych...</div>;
    }

    if (error) {
        return <div style={{...styles.centered, color: 'red'}}>{error}</div>
    }

    return (
        <div style={styles.dashboard}>
            <header style={styles.header}>
                <h1>Dashboard</h1>
                <div>
                    {user && <span style={styles.userInfo}>Zalogowany jako: {user.email} (Rola: {user.role})</span>}
                    <button onClick={logout} style={styles.logoutButton}>Wyloguj</button>
                </div>
            </header>

            <main style={styles.main}>
                {user && user.role === 'ADMIN' && (
                    <section style={styles.section}>
                        <h2>Zarządzanie Użytkownikami</h2>
                        <UserManagement onUserUpdate={fetchData} />
                    </section>
                )}

                <section style={styles.section}>
                    <h2>Produkty</h2>
                    <AddProductForm onDataChange={fetchData} />
                    <ProductList products={products} onDataChange={fetchData} />
                </section>

                <section style={styles.section}>
                    <h2>Kampanie</h2>
                    <AddCampaignForm products={products} cities={cities} onDataChange={fetchData} />
                    <CampaignList campaigns={campaigns} products={products} cities={cities} onDataChange={fetchData} />
                </section>
            </main>
        </div>
    );
};

export default DashboardPage;