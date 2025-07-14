import React, { useState } from 'react';
import { addCity } from '../api/campaignService';

const CityManagement = ({ cities, onDataChange }) => {
    const initialFormState = { name: '', latitude: '', longitude: '' };
    const [formData, setFormData] = useState(initialFormState);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsSubmitting(true);
        try {
            // Backend oczekuje liczb, więc parsujemy stringi z formularza
            await addCity({
                name: formData.name,
                latitude: parseFloat(formData.latitude),
                longitude: parseFloat(formData.longitude)
            });
            setFormData(initialFormState); // Resetuj formularz po sukcesie
            onDataChange(); // Odśwież dane w całym dashboardzie
        } catch (err) {
            setError(err.response?.data?.message || "Nie udało się dodać miasta. Sprawdź, czy miasto o tej nazwie już nie istnieje.");
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div style={styles.container}>
            {/* Formularz do dodawania nowego miasta */}
            <form onSubmit={handleSubmit} style={styles.form}>
                <h3 style={styles.heading}>Dodaj nowe miasto</h3>
                {error && <p style={{ color: 'red', width: '100%', textAlign: 'center' }}>{error}</p>}
                <div className="form-group" style={{ flex: 2, minWidth: '200px' }}>
                    <label className="form-label" htmlFor="cityName">Nazwa miasta</label>
                    <input
                        id="cityName" name="name" className="form-input"
                        value={formData.name} onChange={handleChange} required
                    />
                </div>
                <div className="form-group" style={{ flex: 1, minWidth: '150px' }}>
                    <label className="form-label" htmlFor="latitude">Szerokość geograficzna</label>
                    <input
                        id="latitude" name="latitude" type="number" step="any"
                        className="form-input" value={formData.latitude}
                        onChange={handleChange} placeholder="np. 52.2297" required
                    />
                </div>
                <div className="form-group" style={{ flex: 1, minWidth: '150px' }}>
                    <label className="form-label" htmlFor="longitude">Długość geograficzna</label>
                    <input
                        id="longitude" name="longitude" type="number" step="any"
                        className="form-input" value={formData.longitude}
                        onChange={handleChange} placeholder="np. 21.0122" required
                    />
                </div>
                <button type="submit" style={styles.button} disabled={isSubmitting}>
                    {isSubmitting ? 'Dodawanie...' : 'Dodaj miasto'}
                </button>
            </form>

            {/* Lista istniejących miast */}
            <div style={styles.listContainer}>
                <h3 style={styles.heading}>Istniejące miasta ({cities.length})</h3>
                <div style={styles.tableWrapper}>
                    <table style={styles.table}>
                        <thead>
                            <tr>
                                <th style={styles.th}>Nazwa</th>
                                <th style={styles.th}>Szerokość geo.</th>
                                <th style={styles.th}>Długość geo.</th>
                            </tr>
                        </thead>
                        <tbody>
                            {cities.length > 0 ? (
                                cities.map(city => (
                                    <tr key={city.id}>
                                        <td style={styles.td}>{city.name}</td>
                                        <td style={styles.td}>{city.latitude}</td>
                                        <td style={styles.td}>{city.longitude}</td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="3" style={styles.tdEmpty}>Brak dodanych miast.</td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

const styles = {
    container: { display: 'flex', flexDirection: 'column', gap: '30px' },
    form: { display: 'flex', gap: '15px', alignItems: 'flex-end', flexWrap: 'wrap' },
    heading: { width: '100%', margin: '0 0 10px 0', borderBottom: '1px solid #eee', paddingBottom: '10px', fontWeight: '500', fontSize: '1.1rem' },
    button: { height: '45px', padding: '10px 15px', borderRadius: '6px', border: 'none', backgroundColor: '#0275d8', color: 'white', cursor: 'pointer', fontWeight: '500' },
    listContainer: { marginTop: '20px' },
    tableWrapper: { maxHeight: '300px', overflowY: 'auto', border: '1px solid #ddd', borderRadius: '6px' },
    table: { width: '100%', borderCollapse: 'collapse' },
    th: { position: 'sticky', top: 0, borderBottom: '2px solid #ddd', padding: '12px', backgroundColor: '#f9f9f9', textAlign: 'left', color: '#333' },
    td: { borderBottom: '1px solid #ddd', padding: '12px' },
    tdEmpty: { textAlign: 'center', padding: '20px', color: '#888' },
};

export default CityManagement;