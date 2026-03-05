import axios from 'axios';

const axiosClient = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
    headers: {
        'Content-Type': 'application/json',
    },
});

// Interceptor: Can thiệp vào TRƯỚC KHI request được gửi đi
axiosClient.interceptors.request.use(
    (config) => {
        // Lấy token từ Local Storage
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Interceptor: Can thiệp vào SAU KHI nhận response (Để bắt lỗi 401, 403)
axiosClient.interceptors.response.use(
    (response) => {
        return response.data;
    },
    (error) => {
        if (error.response?.status === 401) {
            // Token hết hạn hoặc sai -> Xóa token và đá về trang Login
            localStorage.removeItem('accessToken');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default axiosClient;