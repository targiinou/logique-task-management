import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { LoginSignup  }from './Components/LoginSignup/LoginSignup';
import { Home } from './Components/Home/Home';
import { ArchivedTasksList } from './Components/ArchivedTasksList/ArchivedTasksList';

function ProtectedRoute({ element }) {

  const access_token = localStorage.getItem('access_token');

  if (!access_token) {
    return <Navigate to="/login" />;
  }
 
  return element;
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginSignup initialAction="Login" />} />
        <Route path="/registrar" element={<LoginSignup initialAction="Sign Up" />} />
        <Route path="/" element={<Navigate to="/login" />} />
        <Route
          path="/home"
          element={<ProtectedRoute element={<Home />} />}
        />
        <Route
          path="/tarefas-arquivadas"
          element={<ProtectedRoute element={<ArchivedTasksList />} />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
