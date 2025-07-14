import React, { createContext, useState, useEffect } from 'react';
import { login as apiLogin, register as apiRegister } from '../api/authService';
import { jwtDecode } from 'jwt-decode';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('authToken'));

  useEffect(() => {
    if (token) {
      try {
        const decodedToken = jwtDecode(token);
        setUser({ email: decodedToken.sub, role: decodedToken.role });
        localStorage.setItem('authToken', token);
      } catch (error) {
        console.error("Nie udało się zdekodować tokena", error);
        setUser(null);
        localStorage.removeItem('authToken');
      }
    } else {
        setUser(null);
        localStorage.removeItem('authToken');
    }
  }, [token]);

  const login = async (email, password) => {
    const response = await apiLogin(email, password);
    setToken(response.data.token);
  };

  const register = async (email, password) => {
    const response = await apiRegister(email, password);
    setToken(response.data.token);
};

  const logout = () => {
    setToken(null);
  };

  const value = { user, token, login, register, logout };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export default AuthContext;