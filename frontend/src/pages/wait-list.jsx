import { useEffect, useState } from 'react'
import { motion } from 'framer-motion'
import axios from '../api/axiosInstance';

const WaitList = () => {
    useEffect(() => {
        const loginReq = async () => {
            try {
                const response = await axios.post('/auth/login', { username: localStorage.getItem("login"), password: localStorage.getItem("password") })
                localStorage.setItem("token", response.data.token);
                localStorage.setItem("role", response.data.role);
                window.location.href = "/";
            } catch (error) {
                console.error('Error fetching users:', error)
            }
        }

        loginReq()
        const interval = setInterval(loginReq, 1000)

        return () => clearInterval(interval)
    }, [])

    return (
        (<div
            className="flex flex-col  bg-gradient-to-br from-indigo-100 via-white to-blue-100" style={{ minHeight: 'calc(100vh - 64px)' }}>
            <main className="flex-grow flex items-center justify-center px-4">
                <motion.div
                    className="w-full max-w-md"
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5, delay: 0.2 }}>
                    <div className='bg-white/80 backdrop-blur-sm rounded-2xl shadow-xl p-8'>
                        <h1 className="text-3xl font-bold text-center mb-6 bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 to-purple-600 dark:from-indigo-400 dark:to-purple-500">
                            Ждите когда вашу регистрацию одобрят
                        </h1>
                    </div>
                </motion.div>
            </main>
        </div>)
    );
}

export default WaitList;