import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  const { login } = useAuth();

  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError(null);

    try {
      await login(email, password);
      navigate('/dashboard');
    } catch (err) {
      console.error("Błąd logowania:", err);
      setError(err.response?.data?.message || 'Nieprawidłowy email lub hasło.');
    }
  };

  return (
    <div style={pageStyle}>
      <form onSubmit={handleSubmit} style={formStyle}>
        <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>Logowanie</h2>

        <label htmlFor="email">Email</label>
        <input
          id="email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          style={inputStyle}
        />

        <label htmlFor="password">Hasło</label>
        <input
          id="password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          style={inputStyle}
        />

        {}
        {error && <p style={{ color: 'red', textAlign: 'center' }}>{error}</p>}

        <button type="submit" style={buttonStyle}>
          Zaloguj się
        </button>

        {}
        <div style={linkContainerStyle}>
            Nie masz konta? <Link to="/register">Zarejestruj się</Link>
        </div>
        {}
      </form>
    </div>
  );
};

const pageStyle = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    height: '100vh',
    backgroundColor: '#f0f2f5',
};

const formStyle = {
    display: 'flex',
    flexDirection: 'column',
    padding: '30px',
    border: '1px solid #ddd',
    borderRadius: '8px',
    backgroundColor: 'white',
    boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
    width: '350px',
};

const inputStyle = {
    marginBottom: '15px',
    padding: '10px',
    fontSize: '16px',
    borderRadius: '5px',
    border: '1px solid #ccc',
};

const buttonStyle = {
    padding: '12px',
    fontSize: '16px',
    borderRadius: '5px',
    border: 'none',
    backgroundColor: '#007bff',
    color: 'white',
    cursor: 'pointer',
};

const linkContainerStyle = {
    textAlign: 'center',
    marginTop: '15px'
};


export default LoginPage;