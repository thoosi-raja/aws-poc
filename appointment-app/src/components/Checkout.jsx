import React, { useState, useEffect } from 'react';

function Checkout() {
  const [cartItems, setCartItems] = useState([]);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    address: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
      setCartItems(JSON.parse(savedCart));
    }
  }, []);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const calculateTotal = () => {
    return cartItems.reduce((total, item) => total + item.price, 0).toFixed(2);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    const orderData = {
      customerName: formData.name,
      customerEmail: formData.email,
      shippingAddress: formData.address,
      totalAmount: parseFloat(calculateTotal()),
      products: cartItems
    };

    try {
      const response = await fetch('http://localhost:8080/api/orders', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(orderData)
      });

      if (!response.ok) {
        throw new Error('Failed to place order');
      }

      // Clear cart and redirect to products page
      localStorage.removeItem('cart');
      alert('Order placed successfully! Check your email for confirmation.');
      window.location.href = '/products';
    } catch (err) {
      setError('Failed to place order. Please try again.');
      console.error('Error placing order:', err);
    } finally {
      setLoading(false);
    }
  };

  if (cartItems.length === 0) {
    return (
      <div className="container" style={{ marginTop: '2rem' }}>
        <h1>Your cart is empty</h1>
        <button 
          onClick={() => window.location.href = '/products'}
          className="btn btn-primary"
        >
          Return to Products
        </button>
      </div>
    );
  }

  return (
    <div>
      <nav className="nav">
        <div className="container nav-content">
          <h1 style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>Checkout</h1>
        </div>
      </nav>

      <div className="container" style={{ marginTop: '2rem' }}>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2rem' }}>
          {/* Cart Summary */}
          <div className="card">
            <h2 style={{ fontSize: '1.25rem', fontWeight: 'bold', marginBottom: '1rem' }}>Cart Summary</h2>
            {cartItems.map((item, index) => (
              <div key={index} style={{ borderBottom: '1px solid #eee', padding: '0.5rem 0', marginBottom: '0.5rem' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <div>
                    <h3 style={{ fontWeight: '500' }}>{item.name}</h3>
                    <p style={{ color: '#666' }}>${item.price.toFixed(2)}</p>
                  </div>
                </div>
              </div>
            ))}
            <div style={{ marginTop: '1rem', fontSize: '1.25rem', fontWeight: 'bold' }}>
              Total: ${calculateTotal()}
            </div>
          </div>

          {/* Checkout Form */}
          <div className="card">
            <h2 style={{ fontSize: '1.25rem', fontWeight: 'bold', marginBottom: '1rem' }}>Shipping Information</h2>
            {error && <div style={{ color: 'red', marginBottom: '1rem' }}>{error}</div>}
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500' }}>
                  Full Name
                </label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  className="form-control"
                  required
                />
              </div>
              <div className="form-group">
                <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500' }}>
                  Email
                </label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  className="form-control"
                  required
                />
              </div>
              <div className="form-group">
                <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500' }}>
                  Shipping Address
                </label>
                <textarea
                  name="address"
                  value={formData.address}
                  onChange={handleChange}
                  className="form-control"
                  rows="3"
                  required
                />
              </div>
              <button
                type="submit"
                className="btn btn-primary w-100"
                style={{ backgroundColor: '#10b981' }}
                disabled={loading}
              >
                {loading ? 'Processing...' : 'Place Order'}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Checkout;
