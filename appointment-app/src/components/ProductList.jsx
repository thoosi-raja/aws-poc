import React, { useState, useEffect } from 'react';

function ProductList() {
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Initialize products
    fetch('http://localhost:8080/api/products/initialize', {
      method: 'POST'
    }).then(() => {
      fetchProducts();
    }).catch(err => {
      console.error('Error initializing products:', err);
      fetchProducts();
    });

    // Load cart from localStorage
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
      setCart(JSON.parse(savedCart));
    }
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/products');
      if (!response.ok) {
        throw new Error('Failed to fetch products');
      }
      const data = await response.json();
      setProducts(data);
      setLoading(false);
    } catch (err) {
      setError('Failed to load products');
      setLoading(false);
      console.error('Error fetching products:', err);
    }
  };

  const addToCart = (product) => {
    const updatedCart = [...cart, product];
    setCart(updatedCart);
    localStorage.setItem('cart', JSON.stringify(updatedCart));
  };

  const goToCheckout = () => {
    window.location.href = '/checkout';
  };

  if (loading) return (
    <div className="container" style={{ marginTop: '2rem', textAlign: 'center' }}>
      Loading products...
    </div>
  );
  
  if (error) return (
    <div className="container" style={{ marginTop: '2rem', color: 'red', textAlign: 'center' }}>
      {error}
    </div>
  );

  return (
    <div>
      <nav className="nav">
        <div className="container nav-content">
          <h1 style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>Products</h1>
          <button
            onClick={goToCheckout}
            className="btn btn-primary"
            style={{ backgroundColor: '#10b981' }}
          >
            Cart ({cart.length})
          </button>
        </div>
      </nav>

      <div className="container">
        <div className="grid">
          {products.map((product) => (
            <div key={product.id} className="card">
              <img
                src={product.imageUrl}
                alt={product.name}
                className="product-image"
              />
              <h2 className="product-title">{product.name}</h2>
              <p className="product-price">${product.price.toFixed(2)}</p>
              {product.description && (
                <p style={{ color: '#666', marginBottom: '1rem' }}>{product.description}</p>
              )}
              <button
                onClick={() => addToCart(product)}
                className="btn btn-primary w-100"
              >
                Add to Cart
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default ProductList;
