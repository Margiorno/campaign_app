// src/styles/buttonStyles.js

const buttonStyles = {
    // Podstawowy styl, który dziedziczą wszystkie przyciski
    base: {
        display: 'inline-flex',
        alignItems: 'center',
        gap: '8px', // Odstęp między ikoną a tekstem
        padding: '8px 14px',
        fontSize: '14px',
        fontWeight: '500',
        borderRadius: '6px',
        border: '1px solid transparent',
        cursor: 'pointer',
        transition: 'all 0.2s ease-in-out',
        textDecoration: 'none',
    },
    // Styl dla przycisków drugorzędnych (Edytuj, Statystyki)
    secondary: {
        backgroundColor: '#f0f2f5',
        color: '#333',
        border: '1px solid #d9d9d9',
    },
    secondaryHover: {
        backgroundColor: '#e7e9ec',
        borderColor: '#bfbfbf',
    },
    // Styl dla przycisków destrukcyjnych (Usuń)
    danger: {
        backgroundColor: '#fff1f0',
        color: '#cf1322',
        border: '1px solid #ffa39e',
    },
    dangerHover: {
        backgroundColor: '#ffccc7',
        borderColor: '#ff7875',
    },
    // Styl dla akcji "Start" / "Wznów"
    start: {
        backgroundColor: '#f6ffed',
        color: '#389e0d',
        border: '1px solid #b7eb8f',
    },
    startHover: {
        backgroundColor: '#d9f7be',
        borderColor: '#95de64',
    },
    // Styl dla akcji "Stop"
    stop: {
        backgroundColor: '#fafafa',
        color: '#595959',
        border: '1px solid #d9d9d9',
    },
    stopHover: {
        backgroundColor: '#f0f0f0',
        borderColor: '#bfbfbf',
    }
};

export default buttonStyles;