import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Workers from './pages/Workers';
import Navbar from './components/Navbar';
import AuthPage from './components/forms/auth';
import { InfoForm } from './components/forms/info-form';

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Workers />} />
        <Route path="/profile" element={<InfoForm />} />
        <Route path="/login" element={<AuthPage />} />
      </Routes>
    </Router>
  );
}

export default App;
