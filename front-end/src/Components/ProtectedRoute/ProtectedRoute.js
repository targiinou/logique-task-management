import React from 'react';
import { Route, Navigate } from 'react-router-dom';

function ProtectedRoute({ element }) {
  const access_token = localStorage.getItem('access_token');
  if (!access_token) {
    return <Navigate to="/login" />;
  }

  return <Route element={element} />;
}

export default ProtectedRoute;
