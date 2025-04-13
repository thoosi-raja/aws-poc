import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/Login';
import ProductList from './components/ProductList';
import Checkout from './components/Checkout';

function App() {
  const isAuthenticated = () => {
    return localStorage.getItem('isLoggedIn') === 'true';
  };

  const PrivateRoute = ({ children }) => {
    return isAuthenticated() ? children : <Navigate to="/" />;
  };

  return (
    <Router>
      <div style={{ minHeight: '100vh', backgroundColor: '#f3f4f6' }}>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route
            path="/products"
            element={
              <PrivateRoute>
                <ProductList />
              </PrivateRoute>
            }
          />
          <Route
            path="/checkout"
            element={
              <PrivateRoute>
                <Checkout />
              </PrivateRoute>
            }
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
