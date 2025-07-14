import apiClient from './axiosConfig';

export const login = (email, password) => {
  return apiClient.post('/auth/login', { email, password });
};

export const register = (email, password) => {
  return apiClient.post('/auth/register', { email, password });
};

export const validateToken = () => {
  return apiClient.get('/auth/validate');
};

export const getAllUsers = () => {
    return apiClient.get('/auth/all');
};

export const editUser = (userId, userData) => {
    const payload = {
        email: userData.email,
        password: userData.password || "defaultpassword",
        role: userData.role
    };
    return apiClient.post(`/auth/edit/${userId}`, payload);
}