import { useEffect, useState } from 'react'
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

    return (
        <div className="w-full max-w-md mx-auto space-y-4">
            <h2 className="text-2xl font text-center mb-6">Список пользователей</h2>
            {users.map((user) => (
                user.reviewerId === null && <UserCard key={user.id} user={user} onAccept={handleAccept} />
            ))}
        </div>
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