import React, { useState, useEffect } from 'react';
import { getAllUsers, editUser } from '../api/authService';

const UserManagement = ({ onUserUpdate }) => {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);

    const fetchUsers = async () => {
        try {
            const response = await getAllUsers();
            setUsers(response.data);
        } catch (err) {
            setError('Nie udało się pobrać listy użytkowników.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const handleRoleChange = async (user, newRole) => {
        if (user.role === newRole) return;

        const confirmChange = window.confirm(`Czy na pewno chcesz zmienić rolę użytkownika ${user.email} na ${newRole}?`);
        if (!confirmChange) return;

        try {
            await editUser(user.id, { email: user.email, role: newRole });
            onUserUpdate(); // Powiadomienie Dashboard o zmianie
        } catch (err) {
            alert(`Błąd podczas zmiany roli: ${err.response?.data?.message || err.message}`);
        }
    };

    if (loading) return <p>Ładowanie użytkowników...</p>;
    if (error) return <p style={{ color: 'red' }}>{error}</p>;

    return (
        <table style={tableStyle}>
            <thead>
                <tr>
                    <th style={thStyle}>Email</th>
                    <th style={thStyle}>Rola</th>
                    <th style={thStyle}>Akcje</th>
                </tr>
            </thead>
            <tbody>
                {users.map(user => (
                    <tr key={user.id}>
                        <td style={tdStyle}>{user.email}</td>
                        <td style={tdStyle}>{user.role}</td>
                        <td style={tdStyle}>
                            <select
                                value={user.role}
                                onChange={(e) => handleRoleChange(user, e.target.value)}
                                style={{ padding: '5px' }}
                            >
                                <option value="USER">USER</option>
                                <option value="ADMIN">ADMIN</option>
                            </select>
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
};

const tableStyle = { width: '100%', borderCollapse: 'collapse', marginTop: '15px' };
const thStyle = { border: '1px solid #ddd', padding: '12px', backgroundColor: '#f2f2f2', textAlign: 'left' };
const tdStyle = { border: '1px solid #ddd', padding: '12px' };

export default UserManagement;