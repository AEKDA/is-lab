import { useState } from "react"
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { NavBar } from './navbar'

export function AdminLoginInterface() {
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [userRole, setUserRole] = useState(null)
  const [mode, setMode] = useState('login')

  const handleSubmit = (e) => {
    e.preventDefault()
    if (mode === 'login') {
      setIsLoggedIn(true)
    } else {
      console.log('Регистрация нового пользователя')
      setIsLoggedIn(true)
    }
  }

  const handleLogout = () => {
    setIsLoggedIn(false)
    setUserRole(null)
  }

  return (
    (<Router>
      <div
        className="min-h-screen bg-gradient-to-br from-indigo-100 to-purple-200 dark:from-gray-900 dark:to-indigo-950">
        <NavBar isLoggedIn={isLoggedIn} userRole={userRole} onLogout={handleLogout} />
        <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
          <Routes>
            <Route
              path="/login"
              element={
                isLoggedIn ? <Navigate to="/" /> : (
                  <div
                    className="bg-white/80 dark:bg-gray-800/80 backdrop-blur-md shadow-lg sm:rounded-lg overflow-hidden transition-all duration-300 ease-in-out transform hover:scale-105">
                    <div className="px-4 py-5 sm:p-6">
                      <form onSubmit={handleSubmit} className="space-y-4">
                        <div className="flex justify-between items-center mb-4">
                          <h2
                            className="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 to-purple-600 dark:from-indigo-400 dark:to-purple-500">
                            {mode === 'login' ? 'Вход в аккаунт' : 'Регистрация'}
                          </h2>
                          <Button
                            type="button"
                            variant="link"
                            onClick={() => setMode(mode === 'login' ? 'register' : 'login')}
                            className="text-indigo-600 hover:text-purple-600 dark:text-indigo-400 dark:hover:text-purple-400 transition-colors">
                            {mode === 'login' ? 'Регистрация' : 'Вход'}
                          </Button>
                        </div>
                        <div className="space-y-2">
                          <Label
                            htmlFor="email"
                            className="text-sm font-medium text-gray-700 dark:text-gray-300">Email</Label>
                          <Input
                            id="email"
                            type="email"
                            placeholder="you@example.com"
                            required
                            className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white" />
                        </div>
                        <div className="space-y-2">
                          <Label
                            htmlFor="password"
                            className="text-sm font-medium text-gray-700 dark:text-gray-300">Пароль</Label>
                          <Input
                            id="password"
                            type="password"
                            required
                            className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white" />
                        </div>
                        {mode === 'register' && (
                          <div className="space-y-2">
                            <Label
                              htmlFor="confirmPassword"
                              className="text-sm font-medium text-gray-700 dark:text-gray-300">Подтвердите пароль</Label>
                            <Input
                              id="confirmPassword"
                              type="password"
                              required
                              className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white" />
                          </div>
                        )}
                        <RadioGroup
                          defaultValue="user"
                          className="flex space-x-4"
                          onValueChange={(value) => setUserRole(value)}>
                          <div className="flex items-center space-x-2">
                            <RadioGroupItem value="user" id="user" />
                            <Label
                              htmlFor="user"
                              className="text-sm font-medium text-gray-700 dark:text-gray-300">Обычный пользователь</Label>
                          </div>
                          <div className="flex items-center space-x-2">
                            <RadioGroupItem value="admin" id="admin" />
                            <Label
                              htmlFor="admin"
                              className="text-sm font-medium text-gray-700 dark:text-gray-300">Администратор</Label>
                          </div>
                        </RadioGroup>
                        <Button
                          type="submit"
                          className="w-full bg-gradient-to-r from-indigo-500 to-purple-600 hover:from-indigo-600 hover:to-purple-700 text-white font-bold py-2 px-4 rounded-md transition-all duration-300 ease-in-out transform hover:scale-105">
                          {mode === 'login' ? 'Войти' : 'Зарегистрироваться'}
                        </Button>
                      </form>
                    </div>
                  </div>
                )
              } />
            <Route
              path="/"
              element={
                isLoggedIn ? (
                  <div
                    className="bg-white/80 dark:bg-gray-800/80 backdrop-blur-md p-6 rounded-lg shadow-lg">
                    <h3
                      className="text-2xl font-bold mb-4 bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 to-purple-600 dark:from-indigo-400 dark:to-purple-500">Добро пожаловать в вашу панель управления</h3>
                    <p className="text-gray-700 dark:text-gray-300">Это ваша персонализированная панель управления. Здесь вы можете просматривать вашу недавнюю активность и важные уведомления.</p>
                  </div>
                ) : <Navigate to="/login" />
              } />
            {/* Добавьте дополнительные маршруты здесь */}
          </Routes>
        </main>
      </div>
    </Router>)
  );
}