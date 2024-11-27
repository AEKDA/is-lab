import { Tab } from "./auth-tabs";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { motion } from "framer-motion";

export const InfoForm = () => {


    return (
        (<div
            className="flex flex-col   bg-gradient-to-br from-indigo-100 via-white to-blue-100" style={{minHeight:'calc(100vh - 64px)'}}>
            <main className="flex-grow flex items-center justify-center px-4">
                <motion.div
                    className="w-full max-w-md"
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5, delay: 0.2 }}>
                    <div className="bg-white/80 backdrop-blur-sm rounded-2xl shadow-xl p-8">
                        <h1 className="text-3xl font-bold text-center mb-6 bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 to-purple-600 dark:from-indigo-400 dark:to-purple-500">Ваши данные   </h1>

                        < motion.form
                            className="space-y-4"
                            initial={{ opacity: 0 }
                            }
                            animate={{ opacity: 1 }}
                            transition={{ duration: 0.3 }}
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
                                    value={localStorage.getItem('login')}
                                    readOnly
                                />
                            </div>
                            <div>
                                <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                                    Роль
                                </label>
                                <Input
                                    id="role"
                                    type="text"
                                    placeholder="role"
                                    className="mt-1"
                                    value={localStorage.getItem('role')}
                                    readOnly
                                />
                            </div>
                        </motion.form >


                    </div>
                </motion.div>
            </main>
        </div>)

    );
};
