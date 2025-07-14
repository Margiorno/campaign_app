import React from 'react';
import { useAuth } from '../hooks/useAuth';

const DashboardPage = () => {
    const { user, logout } = useAuth();

    return (
        <div>
            <h1>Dashboard</h1>
            {}
            {user && <p>Jesteś zalogowany jako: {user.email}</p>}
            <button onClick={logout}>Wyloguj</button>
        </div>
    );
};

export default DashboardPage;