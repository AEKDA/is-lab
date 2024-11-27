import { Button } from "./ui/button"
import { Input } from "./ui/input"
import { motion } from "framer-motion"

export const ClinicRegisterForm = () => {
  return (
    (<motion.form
      className="space-y-4"
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.3 }}>
      <div>
        <label htmlFor="login" className="block text-sm font-medium text-gray-700">Логин</label>
        <Input id="login" type="login" placeholder="some" className="mt-1" />
      </div>
      <div>
        <label htmlFor="password" className="block text-sm font-medium text-gray-700">Пароль</label>
        <Input id="password" type="password" placeholder="********" className="mt-1" />
      </div>
      <Button
        type="submit"
        className="w-full bg-gradient-to-r from-indigo-500 to-purple-600 dark:from-indigo-400 dark:to-purple-500 transition-all duration-200">
        Регистрация администратора
      </Button>
    </motion.form>)
  );
}

