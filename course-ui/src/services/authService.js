import axiosClient from '../api/axiosClient';

export const loginApi = (username, password) => {
    return axiosClient.post('/auth/login', { username, password });
};