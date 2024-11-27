import { Link, useNavigate } from "react-router-dom"
import { LockIcon, UserIcon, HomeIcon, SettingsIcon, UsersIcon, BarChartIcon } from 'lucide-react'
import React from "react"


const Navbar = (ъ) => {
  const navigate = useNavigate();
  const isLoggedIn = !!localStorage.getItem('token');
  const userRole = localStorage.getItem('role');

  // Функция для выхода из системы
  const onLogout = () => {
    localStorage.clear();
    navigate('/login');
  };


  return (
    <header className="bg-white/80 dark:bg-gray-800/80 backdrop-blur-md shadow-lg">
      {/* <header> */}
      <div className="max-w-8xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16 ">
          <div className="flex">
            <div className="flex-shrink-0 flex items-center">
              <span className="ml-2 text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 to-purple-600 dark:from-indigo-400 dark:to-purple-500">Worker System</span>
            </div>
          </div>
          {isLoggedIn && (
            <nav className="hidden md:flex items-center mx-14">
              <Link to="/" className="text-gray-700 hover:text-indigo-600 dark:text-gray-300 dark:hover:text-indigo-400 px-3 py-2 rounded-md text-sm font-medium">
                <HomeIcon className="w-4 h-4 inline-block mr-1" />
                Главная
              </Link>
              {userRole === "admin" && (
                <>
                  <Link to="/users" className="text-gray-700 hover:text-indigo-600 dark:text-gray-300 dark:hover:text-indigo-400 px-3 py-2 rounded-md text-sm font-medium">
                    <UsersIcon className="w-4 h-4 inline-block mr-1" />
                    Пользователи
                  </Link>
                  <Link to="/analytics" className="text-gray-700 hover:text-indigo-600 dark:text-gray-300 dark:hover:text-indigo-400 px-3 py-2 rounded-md text-sm font-medium">
                    <BarChartIcon className="w-4 h-4 inline-block mr-1" />
                    Аналитика
                  </Link>
                </>
              )}
              <Link to="/profile" className="text-gray-700 hover:text-indigo-600 dark:text-gray-300 dark:hover:text-indigo-400 px-3 py-2 rounded-md text-sm font-medium">
                <UserIcon className="w-4 h-4 inline-block mr-1" />
                Профиль
              </Link>
            </nav>
          )}
          {isLoggedIn && (
            <div className="flex items-center">
              <span className="text-gray-700 dark:text-gray-300 mr-4">Добро пожаловать, {userRole === "admin" ? "Администратор" : "Пользователь"}</span>
              <button onClick={onLogout} variant="outline" className="hover:bg-red-100 hover:text-red-600 dark:hover:bg-red-900 dark:hover:text-red-400 transition-colors">
                Выйти
              </button>
            </div>
          )}

        </div>
      </div>
    </header>
  )
}

export default Navbar;