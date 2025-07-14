import React, { useState, useEffect, useCallback } from 'react';
import { useAuth } from '../hooks/useAuth';
import { getAllProducts, getAllCampaigns, getAllCities } from '../api/campaignService';
import { getAllUsers } from '../api/authService';

import ProductList from '../components/ProductList';
import CampaignList from '../components/CampaignList';
import UserManagement from '../components/UserManagement';
import AddProductForm from '../components/AddProductForm';
import AddCampaignForm from '../components/AddCampaignForm';
import CityManagement from '../components/CityManagement';
import styles from './DashboardStyles';

const DashboardPage = () => {
    const { user, logout } = useAuth();

    const [users, setUsers] = useState([]);
    const [products, setProducts] = useState([]);
    const [campaigns, setCampaigns] = useState([]);
    const [cities, setCities] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isRefreshing, setIsRefreshing] = useState(false);
    const [error, setError] = useState('');

    const fetchData = useCallback(async () => {
        setIsRefreshing(true);
        try {
            const promises = [
                getAllProducts(),
                getAllCampaigns(),
                getAllCities(),
            ];
            if (user && user.role === 'ADMIN') {
                promises.push(getAllUsers());
            }
            const [productsRes, campaignsRes, citiesRes, usersRes] = await Promise.all(promises);
            setProducts(productsRes.data);
            setCampaigns(campaignsRes.data);
            setCities(citiesRes.data);
            if (usersRes) {
                setUsers(usersRes.data);
            }
            setError('');
        } catch (err) {
            setError('Nie udało się pobrać danych. Spróbuj odświeżyć stronę.');
            console.error(err);
        } finally {
            setLoading(false);
            setIsRefreshing(false);
        }
    }, [user]);

    useEffect(() => {
        if (user) {
            fetchData();
        }
    }, [fetchData, user]);

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
                    <button onClick={fetchData} disabled={isRefreshing} style={styles.refreshButton}>
                        {isRefreshing ? 'Odświeżanie...' : 'Odśwież'}
                    </button>
                    <button onClick={logout} style={styles.logoutButton}>Wyloguj</button>
                </div>
            </header>

            <main style={styles.main}>
                {user && user.role === 'ADMIN' && (
                    <>
                        <section style={styles.section}>
                            <h2>Zarządzanie Użytkownikami</h2>
                            <UserManagement users={users} onUserUpdate={fetchData} />
                        </section>

                        {}
                        <section style={styles.section}>
                            <h2>Zarządzanie Miastami</h2>
                            <CityManagement cities={cities} onDataChange={fetchData} />
                        </section>
                    </>
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

const extraStyles = {
    refreshButton: {
        padding: '8px 16px',
        fontSize: '14px',
        borderRadius: '6px',
        border: '1px solid #0275d8',
        backgroundColor: 'transparent',
        color: '#0275d8',
        cursor: 'pointer',
        marginRight: '10px'
    }
}
Object.assign(styles, extraStyles);

export default DashboardPage;