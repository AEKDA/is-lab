import { useEffect, useState } from 'react'
import { motion } from 'framer-motion'
import { Button } from "../components/forms/ui/button"
import { Card, CardContent } from "../components/forms/ui/card"
import { Check } from 'lucide-react'
import axios from '../api/axiosInstance';

export default function UsersTable() {
    const [users, setUsers] = useState([])

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await axios.get('/auth/register/accept')
                if (Array.isArray(response.data)) {
                    setUsers(response.data)
                } else {
                    console.error('Received invalid data format from API')
                }
            } catch (error) {
                console.error('Error fetching users:', error)
            }
        }

        fetchUsers()
        const interval = setInterval(fetchUsers, 1000)

        return () => clearInterval(interval)
    }, [])

    const handleAccept = async (userId) => {
        try {
            const response = await axios.post(`/auth/register/accept/${userId}`)
            if (response.data.success) {
                setUsers(users.filter(user => user.id !== userId))
            }
        } catch (error) {
            console.error('Error approving user:', error)
        }
    }
    const unassignedUsers = users.filter(user => user.reviewerId === null)

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
                    <h1 className="text-3xl font-bold text-center mb-6 bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 to-purple-600 dark:from-indigo-400 dark:to-purple-500">Список пользователей</h1>
                        <div className='flex flex-col gap-4'>
                            {unassignedUsers.length === 0 ? (
                                <p className="text-center text-gray-500">Нет пользователей для просмотра</p>
                            ) : (
                                unassignedUsers.map((user) => (
                                    <UserCard key={user.id} user={user} onAccept={handleAccept} />
                                ))
                            )}
                        </div>
                    </div>
                </motion.div>
            </main>
        </div>)
    )
}

function UserCard({ user, onAccept }) {
    return (
        <Card className="w-full">
            <CardContent className="flex items-center justify-between p-4">
                <div className="flex items-center space-x-4">
                    <div>
                        <h3 className="font-medium">{user.username}</h3>
                        <p className="text-sm text-gray-500">id: {user.id}</p>
                    </div>
                </div>
                <Button onClick={() => onAccept(user.id)} className="w-full bg-gradient-to-r from-indigo-500 to-purple-600 dark:from-indigo-400 dark:to-purple-500 transition-all duration-200">
                    <Check className="mr-2 h-4 w-4" /> Принять
                </Button>
            </CardContent>
        </Card>
    )
}