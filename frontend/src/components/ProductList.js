import React, { useState } from 'react';
import { deleteProduct, updateProduct } from '../api/campaignService';
import Modal from './Modal';
import buttonStyles from '../styles/buttonStyles';
import { ReactComponent as EditIcon } from '../assets/icons/edit.svg';
import { ReactComponent as TrashIcon } from '../assets/icons/trash.svg';

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
                alert('Nie udało się usunąć produktu. Sprawdź, czy nie jest używany przez kampanię.');
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
                        <button onClick={() => handleEdit(product)} style={{ ...buttonStyles.base, ...buttonStyles.secondary }}>
                            <EditIcon width="16" height="16" />
                            <span>Edytuj</span>
                        </button>
                        <button onClick={() => handleDelete(product.id)} style={{ ...buttonStyles.base, ...buttonStyles.danger }} disabled={isSubmitting}>
                            <TrashIcon width="16" height="16" />
                            <span>{isSubmitting ? 'Usuwanie...' : 'Usuń'}</span>
                        </button>
                    </div>
                </div>
            ))}
            {isModalOpen && editingProduct && (
                <Modal onClose={() => setIsModalOpen(false)}>
                    <h2>Edytuj Produkt</h2>
                    <form onSubmit={handleUpdate}>
                        <div className="form-group">
                            <label htmlFor="editProductName" className="form-label">Nazwa produktu</label>
                            <input
                                id="editProductName"
                                type="text"
                                value={editingProduct.name}
                                onChange={(e) => setEditingProduct({ ...editingProduct, name: e.target.value })}
                                className="form-input"
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="editProductDesc" className="form-label">Opis</label>
                            <textarea
                                id="editProductDesc"
                                value={editingProduct.description}
                                onChange={(e) => setEditingProduct({ ...editingProduct, description: e.target.value })}
                                className="form-input"
                            />
                        </div>
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
    card: { border: '1px solid #e0e0e0', borderRadius: '8px', padding: '20px', boxShadow: '0 2px 5px rgba(0,0,0,0.05)', display: 'flex', flexDirection: 'column', background: 'white' },
    cardTitle: { margin: '0 0 10px 0', fontSize: '1.2rem' },
    cardDescription: { flexGrow: 1, color: '#666', fontSize: '0.95rem', lineHeight: '1.5' },
    cardActions: { display: 'flex', gap: '10px', marginTop: '20px', borderTop: '1px solid #f0f0f0', paddingTop: '20px' },
    submitButton: { width: '100%', padding: '12px', borderRadius: '6px', border: 'none', background: '#0275d8', color: 'white', cursor: 'pointer', fontSize: '1rem', fontWeight: 'bold' },
};

export default ProductList;