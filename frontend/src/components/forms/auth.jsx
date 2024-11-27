"use client";
import { useState } from 'react'
import { motion } from 'framer-motion'
import { AuthTabs } from './auth-tabs'
import { LoginForm } from './login-form'
import { ClientRegisterForm } from './client-register-form'
import { ClinicRegisterForm } from './admin-register-form'

export default function AuthPage() {
  const [currentTab, setCurrentTab] = useState('login')

  const renderForm = () => {
    switch (currentTab) {
      case 'login':
        return <LoginForm />;
      case 'client-register':
        return <ClientRegisterForm />;
      case 'clinic-register':
        return <ClinicRegisterForm />;
      default:
        return null
    }
  }

  return (
    (<div
      className="flex flex-col  bg-gradient-to-br from-indigo-100 via-white to-blue-100" style={{minHeight:'calc(100vh - 64px)'}}>
      <main className="flex-grow flex items-center justify-center px-4">
        <motion.div
          className="w-full max-w-md"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.2 }}>
          <div className="bg-white/80 backdrop-blur-sm rounded-2xl shadow-xl p-8">
            <h1 className="text-3xl font-bold text-center mb-6 text-gray-800">Добро пожаловать</h1>
            <AuthTabs onTabChange={setCurrentTab} />
            <div className="mt-8">
              {renderForm()}
            </div>
          </div>
        </motion.div>
      </main>
    </div>)
  );
}

