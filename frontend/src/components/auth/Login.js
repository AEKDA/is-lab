import React, { useState } from 'react';
import axios from '../../api/axiosInstance';
import './auth.css';

function Login() {
    const [credentials, setCredentials] = useState({ username: '', password: '' });

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/auth/login', credentials);
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('login', credentials.username);
            localStorage.setItem('role', response.data.role);
            window.location.href = '/';
        } catch (error) {
            console.error('Ошибка при входе:', error);
        }
    };

    return (
        <form onSubmit={handleLogin}>
            <h2 className='auth-title'>Вход</h2>
            <div>Логин</div>
            <input
                type="text"
                placeholder="Username"
                onChange={(e) => setCredentials({ ...credentials, username: e.target.value })}
                required
            />
            <div>Пароль</div>
            <input
                type="password"
                placeholder="Password"
                onChange={(e) => setCredentials({ ...credentials, password: e.target.value })}
                required
            />
            <button type="submit">Login</button>
        </form>
    );
}

export default Login;
