import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const RegisterPage = () => {

  const [email, setEmail] = useState('');
  const [confirmEmail, setConfirmEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  
  const [error, setError] = useState(null);

  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError(null);

    if (email !== confirmEmail) {
      setError('Adresy email nie są takie same.');
      return;
    }

    if (password !== confirmPassword) {
      setError('Hasła nie są takie same.');
      return;
    }

    if (password.length < 8) {
        setError('Hasło musi mieć co najmniej 8 znaków.');
        return;
    }

    try {
      await register(email, password);
      navigate('/dashboard');
    } catch (err) {
      console.error("Błąd rejestracji:", err);
      setError(err.response?.data?.message || 'Wystąpił błąd podczas rejestracji.');
    }
  };

  return (
    <div style={pageStyle}>
      <form onSubmit={handleSubmit} style={formStyle}>
        <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>Rejestracja</h2>

        {}
        <label htmlFor="email">Email</label>
        <input
          id="email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          style={inputStyle}
        />

        <label htmlFor="confirmEmail">Potwierdź Email</label>
        <input
          id="confirmEmail"
          type="email"
          value={confirmEmail}
          onChange={(e) => setConfirmEmail(e.target.value)}
          required
          style={inputStyle}
        />

        <label htmlFor="password">Hasło (min. 8 znaków)</label>
        <input
          id="password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          style={inputStyle}
        />

        <label htmlFor="confirmPassword">Potwierdź Hasło</label>
        <input
          id="confirmPassword"
          type="password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          required
          style={inputStyle}
        />
        {}

        {error && <p style={{ color: 'red', textAlign: 'center' }}>{error}</p>}

        <button type="submit" style={buttonStyle}>
          Zarejestruj się
        </button>

        <div style={linkContainerStyle}>
            Masz już konto? <Link to="/login">Zaloguj się</Link>
        </div>
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
    padding: '20px',
    border: '1px solid #ddd',
    borderRadius: '8px',
    backgroundColor: 'white',
    boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
    width: '380px',
};
const inputStyle = {
    marginBottom: '10px',
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
    backgroundColor: '#28a745',
    color: 'white',
    cursor: 'pointer',
    marginTop: '10px',
};
const linkContainerStyle = {
    textAlign: 'center',
    marginTop: '15px'
};

export default RegisterPage;