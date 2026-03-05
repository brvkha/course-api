import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { loginApi } from '../services/authService';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await loginApi(username, password);
            // Lưu token vào kho của trình duyệt
            localStorage.setItem('accessToken', response.accessToken);
            // Chuyển hướng sang trang Dashboard
            navigate('/dashboard');
        } catch (err) {
            setError('Sai tài khoản hoặc mật khẩu!');
        }
    };

    return (
        <div style={{ maxWidth: '300px', margin: '100px auto' }}>
            <h2>Đăng nhập Hệ thống</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <form onSubmit={handleLogin}>
                <div>
                    <label>Username</label>
                    <input 
                        type="text" 
                        value={username} 
                        onChange={(e) => setUsername(e.target.value)} 
                        required 
                        style={{ width: '100%', marginBottom: '10px' }}
                    />
                </div>
                <div>
                    <label>Password</label>
                    <input 
                        type="password" 
                        value={password} 
                        onChange={(e) => setPassword(e.target.value)} 
                        required 
                        style={{ width: '100%', marginBottom: '10px' }}
                    />
                </div>
                <button type="submit" style={{ width: '100%', padding: '10px' }}>Login</button>
            </form>
        </div>
    );
};

export default Login;