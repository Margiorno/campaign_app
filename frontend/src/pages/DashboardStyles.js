const styles = {
    dashboard: { fontFamily: 'Arial, sans-serif', color: '#333', background: '#f4f7f6', minHeight: '100vh' },
    header: {
        backgroundColor: '#fff', color: '#333', padding: '15px 30px',
        display: 'flex', justifyContent: 'space-between', alignItems: 'center',
        boxShadow: '0 2px 4px rgba(0,0,0,0.05)', borderBottom: '1px solid #ddd'
    },
    userInfo: { marginRight: '20px', color: '#555' },
    logoutButton: {
        padding: '8px 16px', fontSize: '14px', borderRadius: '5px', border: 'none',
        backgroundColor: '#d9534f', color: 'white', cursor: 'pointer',
    },
    main: { padding: '20px' },
    section: {
        backgroundColor: '#fff', padding: '25px', margin: '20px 0',
        borderRadius: '8px', boxShadow: '0 2px 5px rgba(0,0,0,0.08)'
    },
    centered: {
        display: 'flex', justifyContent: 'center', alignItems: 'center',
        height: '100vh', fontSize: '1.2rem', color: '#555'
    }
};
export default styles;