import { useState } from "react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { motion } from "framer-motion";
import axios from '../../api/axiosInstance';

export const LoginForm = () => {
  const [credentials, setCredentials] = useState({ username: "", password: "" });

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("/auth/login", credentials);
      localStorage.setItem("token", response.data.token);
      localStorage.setItem("login", credentials.username);
      localStorage.setItem("role", response.data.role);
      window.location.href = "/";
    } catch (error) {
      // useError({ type: 'SET_ERROR', payload: 'Не удалось одобрить пользователя. Пожалуйста, попробуйте еще раз.' })
      console.error("Ошибка при входе:", error);
    }
  };

  return (
    <motion.form
      className="space-y-4"
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.3 }}
      onSubmit={handleLogin}
    >
      <div>
        <label htmlFor="login" className="block text-sm font-medium text-gray-700">
          Логин
        </label>
        <Input
          id="login"
          type="text"
          placeholder="some"
          className="mt-1"
          value={credentials.username}
          onChange={(e) => setCredentials({ ...credentials, username: e.target.value })}
        />
      </div>
      <div>
        <label htmlFor="password" className="block text-sm font-medium text-gray-700">
          Пароль
        </label>
        <Input
          id="password"
          type="password"
          placeholder="********"
          className="mt-1"
          value={credentials.password}
          onChange={(e) => setCredentials({ ...credentials, password: e.target.value })}
        />
      </div>
      <Button
        type="submit"
        className="w-full bg-gradient-to-r from-indigo-500 to-purple-600 dark:from-indigo-400 dark:to-purple-500 transition-all duration-200">
        Войти
      </Button>
    </motion.form>
  );
};
