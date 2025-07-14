import React from 'react';
import ReactDOM from 'react-dom';

const Modal = ({ children, onClose }) => {
    return ReactDOM.createPortal(
        <div style={styles.overlay} onClick={onClose}>
            <div style={styles.modal} onClick={(e) => e.stopPropagation()}>
                <button style={styles.closeButton} onClick={onClose}>×</button>
                {children}
            </div>
        </div>,
        document.getElementById('modal-root')
    );
};

// Utwórz div w public/index.html: <div id="modal-root"></div>
const styles = {
    overlay: {
        position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
        backgroundColor: 'rgba(0, 0, 0, 0.7)', display: 'flex',
        justifyContent: 'center', alignItems: 'center', zIndex: 1000
    },
    modal: {
        background: 'white', padding: '25px', borderRadius: '8px',
        minWidth: '400px', maxWidth: '90%', position: 'relative'
    },
    closeButton: {
        position: 'absolute', top: '10px', right: '10px',
        background: 'transparent', border: 'none', fontSize: '1.5rem', cursor: 'pointer'
    }
};

export default Modal;