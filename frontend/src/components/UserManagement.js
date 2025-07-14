import React, { useState, useEffect } from 'react';
import { getAllUsers, editUser } from '../api/authService';
import Modal from './Modal';

const UserManagement = ({ onUserUpdate }) => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingUser, setEditingUser] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                setLoading(true);
                const response = await getAllUsers();
                setUsers(response.data);
            } catch (err) {
                setError('Nie udało się pobrać listy użytkowników.');
            } finally {
                setLoading(false);
            }
        };
        fetchUsers();
    }, []);

    const handleEditClick = (user) => {
        setEditingUser({ ...user, password: '' });
        setIsModalOpen(true);
    };

    const handleFormChange = (e) => {
        const { name, value } = e.target;
        setEditingUser(prev => ({ ...prev, [name]: value }));
    };

    const handleUpdateSubmit = async (e) => {
        e.preventDefault();
        if (!editingUser) return;
        setIsSubmitting(true);
        try {
            await editUser(editingUser.id, editingUser);
            setIsModalOpen(false);
            onUserUpdate(); // Wywołaj funkcję z Dashboard, aby odświeżyć dane
        } catch (err) {
            alert(`Błąd: ${err.response?.data?.message || 'Wystąpił błąd.'}`);
        } finally {
            setIsSubmitting(false);
        }
    };

    if (loading) return <p>Ładowanie użytkowników...</p>;
    if (error) return <p style={{ color: 'red' }}>{error}</p>;

    return (
        <>
            <table style={styles.table}>
                <thead>
                    <tr>
                        <th style={styles.th}>Email</th>
                        <th style={styles.th}>Rola</th>
                        <th style={styles.th}>Akcje</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map(user => (
                        <tr key={user.id}>
                            <td style={styles.td}>{user.email}</td>
                            <td style={styles.td}>{user.role}</td>
                            <td style={styles.td}>
                                <button onClick={() => handleEditClick(user)} style={styles.editButton}>
                                    Edytuj
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {isModalOpen && editingUser && (
                <Modal onClose={() => setIsModalOpen(false)}>
                    <h2>Edytuj Użytkownika</h2>
                    <form onSubmit={handleUpdateSubmit}>
                        <div style={styles.formGroup}>
                            <label>Email</label>
                            <input
                                type="email"
                                value={editingUser.email}
                                disabled
                                style={{ ...styles.input, background: '#f2f2f2' }}
                            />
                        </div>
                        <div style={styles.formGroup}>
                            <label htmlFor="role">Rola</label>
                            <select
                                id="role" name="role" value={editingUser.role}
                                onChange={handleFormChange} style={styles.input}
                            >
                                <option value="USER">USER</option>
                                <option value="ADMIN">ADMIN</option>
                            </select>
                        </div>
                        <div style={styles.formGroup}>
                            <label htmlFor="password">Nowe Hasło</label>
                            <input
                                id="password" name="password" type="password"
                                value={editingUser.password} onChange={handleFormChange}
                                style={styles.input} placeholder="Zostaw puste, aby nie zmieniać"
                            />
                        </div>
                        <button type="submit" style={styles.submitButton} disabled={isSubmitting}>
                            {isSubmitting ? 'Zapisywanie...' : 'Zapisz zmiany'}
                        </button>
                    </form>
                </Modal>
            )}
        </>
    );
};

const styles = {
    table: { width: '100%', borderCollapse: 'collapse', marginTop: '15px' },
    th: { borderBottom: '2px solid #ddd', padding: '12px', backgroundColor: '#f9f9f9', textAlign: 'left', color: '#333' },
    td: { borderBottom: '1px solid #ddd', padding: '12px' },
    editButton: { padding: '6px 12px', borderRadius: '5px', border: '1px solid #0275d8', background: 'transparent', color: '#0275d8', cursor: 'pointer' },
    formGroup: { marginBottom: '15px' },
    input: { width: '100%', padding: '10px', marginTop: '5px', borderRadius: '4px', border: '1px solid #ccc', boxSizing: 'border-box' },
    submitButton: { width: '100%', padding: '12px', borderRadius: '5px', border: 'none', background: '#5cb85c', color: 'white', cursor: 'pointer', fontSize: '1rem', fontWeight: 'bold' },
};

export default UserManagement;