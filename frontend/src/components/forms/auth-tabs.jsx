"use client";
import { useState } from 'react'
import { motion } from 'framer-motion'

export const Tab = ({ label, isActive, onClick }) => (
  <button
    onClick={onClick}
    className={`relative px-4 py-2 text-sm font-medium transition-colors ${
      isActive ? 'text-indigo-600' : 'text-gray-600 hover:text-gray-800'
    }`}>
    {label}
    {isActive && (
      <motion.div
        className="absolute bottom-0 left-0 right-0 h-0.5 bg-indigo-600"
        layoutId="activeTab"
        transition={{ type: "spring", stiffness: 500, damping: 30 }} />
    )}
  </button>
)

export const AuthTabs = ({ onTabChange }) => {
  const [activeTab, setActiveTab] = useState('login')

  const handleTabChange = (tab) => {
    setActiveTab(tab)
    onTabChange(tab)
  }

  return (
    (<div className="flex justify-center space-x-4 border-b border-gray-200">
      <Tab
        label="Войти"
        value="login"
        isActive={activeTab === 'login'}
        onClick={() => handleTabChange('login')} />
      <Tab
        label="Регистрация пользователя"
        value="client-register"
        isActive={activeTab === 'client-register'}
        onClick={() => handleTabChange('client-register')} />
      <Tab
        label="Регистрация администратора"
        value="clinic-register"
        isActive={activeTab === 'clinic-register'}
        onClick={() => handleTabChange('clinic-register')} />
    </div>)
  );
}

