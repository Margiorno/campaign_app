import React, { useState } from 'react';
import { deleteProduct, updateProduct } from '../api/campaignService';
import Modal from './Modal';

const ProductList = ({ products, onDataChange }) => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingProduct, setEditingProduct] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleDelete = async (productId) => {
        if (isSubmitting) return;
        if (window.confirm('Czy na pewno chcesz usunąć ten produkt?')) {
            setIsSubmitting(true);
            try {
                await deleteProduct(productId);
                onDataChange();
            } catch (error) {
                console.error("Błąd usuwania produktu:", error);
                alert('Nie udało się usunąć produktu. Sprawdź, czy nie jest używany przez istniejącą kampanię.');
            } finally {
                setIsSubmitting(false);
            }
        }
    };

    const handleEdit = (product) => {
        setEditingProduct({ ...product });
        setIsModalOpen(true);
    };

    const handleUpdate = async (e) => {
        e.preventDefault();
        if (isSubmitting) return;
        setIsSubmitting(true);
        try {
            await updateProduct(editingProduct.id, {
                name: editingProduct.name,
                description: editingProduct.description,
            });
            onDataChange();
            setIsModalOpen(false);
            setEditingProduct(null);
        } catch (error) {
            alert('Błąd podczas aktualizacji produktu.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div style={styles.grid}>
            {products.map(product => (
                <div key={product.id} style={styles.card}>
                    <h3 style={styles.cardTitle}>{product.name}</h3>
                    <p style={styles.cardDescription}>{product.description}</p>
                    <div style={styles.cardActions}>
                        <button onClick={() => handleEdit(product)} style={styles.button} disabled={isSubmitting}>Edytuj</button>
                        <button onClick={() => handleDelete(product.id)} style={styles.deleteButton} disabled={isSubmitting}>
                            {isSubmitting ? 'Usuwanie...' : 'Usuń'}
                        </button>
                    </div>
                </div>
            ))}
            {isModalOpen && (
                <Modal onClose={() => setIsModalOpen(false)}>
                    <h2>Edytuj Produkt</h2>
                    <form onSubmit={handleUpdate}>
                        <input
                            type="text"
                            value={editingProduct.name}
                            onChange={(e) => setEditingProduct({ ...editingProduct, name: e.target.value })}
                            style={styles.input}
                            placeholder="Nazwa produktu"
                        />
                        <textarea
                            value={editingProduct.description}
                            onChange={(e) => setEditingProduct({ ...editingProduct, description: e.target.value })}
                            style={{ ...styles.input, height: '80px', resize: 'vertical' }}
                            placeholder="Opis produktu"
                        />
                        <button type="submit" style={styles.submitButton} disabled={isSubmitting}>
                            {isSubmitting ? 'Zapisywanie...' : 'Zapisz zmiany'}
                        </button>
                    </form>
                </Modal>
            )}
        </div>
    );
};

const styles = {
    grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '20px', marginTop: '20px' },
    card: { border: '1px solid #e0e0e0', borderRadius: '8px', padding: '15px', boxShadow: '0 1px 3px rgba(0,0,0,0.04)', display: 'flex', flexDirection: 'column', background: 'white' },
    cardTitle: { margin: '0 0 10px 0', fontSize: '1.1rem' },
    cardDescription: { flexGrow: 1, color: '#666', fontSize: '0.9rem', lineHeight: '1.4' },
    cardActions: { display: 'flex', gap: '10px', marginTop: '15px', borderTop: '1px solid #f0f0f0', paddingTop: '15px' },
    button: { padding: '8px 12px', borderRadius: '5px', border: '1px solid #ccc', background: '#f7f7f7', cursor: 'pointer' },
    deleteButton: { padding: '8px 12px', borderRadius: '5px', border: 'none', background: '#d9534f', color: 'white', cursor: 'pointer' },
    input: { width: '100%', padding: '10px', marginBottom: '10px', borderRadius: '4px', border: '1px solid #ccc', boxSizing: 'border-box' },
    submitButton: { width: '100%', padding: '10px', borderRadius: '5px', border: 'none', background: '#0275d8', color: 'white', cursor: 'pointer', fontSize: '1rem' },
};

export default ProductList;