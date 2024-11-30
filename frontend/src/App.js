import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate, useLocation } from 'react-router-dom';
import Workers from './pages/Workers';
import Navbar from './components/Navbar';
import AuthPage from './components/forms/auth';
import { InfoForm } from './components/forms/info-form';
import UsersTable from './pages/users-table';
import WaitList from './pages/wait-list';

const App = () => {
  return (
    <Router>
      <Navbar />
      <AppContent />
    </Router>
  );
}

const AppContent = () => {
  useAuthCheck();

  return (
    <Routes>
      <Route path="/" element={<Workers />} />
      <Route path="/profile" element={<InfoForm />} />
      <Route path="/users" element={<UsersTable />} />
      <Route path="/waitlist" element={<WaitList />} />
      <Route path="/login" element={<AuthPage />} />
    </Routes>
  );
};

const useAuthCheck = () => {
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const checkAuth = () => {
      const token = localStorage.getItem('token');
      if (!token && location.pathname !== '/login') {
        navigate('/login');
      }
    };

    checkAuth();
  }, [navigate, location]);
};



export default App;
