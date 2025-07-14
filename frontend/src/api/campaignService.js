import apiClient from './axiosConfig';

// --- Product Endpoints ---
export const getAllProducts = () => apiClient.get('/api/product/get');
export const addProduct = (productData) => apiClient.post('/api/product/new', productData);
export const updateProduct = (id, productData) => apiClient.patch(`/api/product/update/${id}`, productData);
export const deleteProduct = (id) => apiClient.delete(`/api/product/delete/${id}`);

// --- City Endpoints ---
export const getAllCities = () => apiClient.get('/api/city/get');
export const addCity = (cityData) => apiClient.post('/api/city/add', cityData);

// --- Campaign Endpoints ---
export const getAllCampaigns = () => apiClient.get('/api/campaign/all');
export const addCampaign = (campaignData) => apiClient.post('/api/campaign/new', campaignData);
export const updateCampaign = (id, campaignData) => apiClient.patch(`/api/campaign/update/${id}`, campaignData);
export const deleteCampaign = (id) => apiClient.delete(`/api/campaign/delete/${id}`);
export const startCampaign = (id) => apiClient.post(`/api/campaign/${id}/start`);
export const stopCampaign = (id) => apiClient.post(`/api/campaign/${id}/stop`);

// --- Stats Endpoint ---
export const getStatsForCampaign = (id) => apiClient.get(`/api/stats/${id}`);

// --- Simulator ---
export const getPublicActiveCampaigns = () => apiClient.get('/api/campaign/active');
export const registerPublicClick = (id) => apiClient.post(`/api/stats/${id}/click`);