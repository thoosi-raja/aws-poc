import { useState } from "react";
import "./App.css";
import ProductList from "./components/ProductList";
import Cart from "./components/Cart";
import UserProfile from "./components/UserProfile";

function App() {
  const [user, setUser] = useState(null);
  const [cartItems, setCartItems] = useState([]);
  const [view, setView] = useState('products'); // products, cart, profile

  const handleAddToCart = (product) => {
    setCartItems(prevItems => {
      const existingItem = prevItems.find(item => item.pk === product.pk);
      if (existingItem) {
        return prevItems.map(item =>
          item.pk === product.pk
            ? { ...item, quantity: item.quantity + 1 }
            : item
        );
      }
      return [...prevItems, { ...product, quantity: 1 }];
    });
  };

  const handleUpdateQuantity = (item, newQuantity) => {
    if (newQuantity === 0) {
      handleRemoveFromCart(item);
      return;
    }
    setCartItems(prevItems =>
      prevItems.map(cartItem =>
        cartItem.pk === item.pk
          ? { ...cartItem, quantity: newQuantity }
          : cartItem
      )
    );
  };

  const handleRemoveFromCart = (item) => {
    setCartItems(prevItems => prevItems.filter(cartItem => cartItem.pk !== item.pk));
  };

  const handleCheckout = async (items, total) => {
    if (!user) {
      setView('profile');
      return;
    }

    try {
      const productQuantities = items.reduce((acc, item) => {
        acc[item.pk] = item.quantity;
        return acc;
      }, {});

      const response = await fetch(`${import.meta.env.VITE_API_URL}/orders`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          userId: user.attributes.userId,
          products: productQuantities,
          totalAmount: total
        }),
      });

      if (response.ok) {
        setCartItems([]);
        alert('Order placed successfully!');
      } else {
        alert('Failed to place order. Please try again.');
      }
    } catch (error) {
      console.error('Error during checkout:', error);
      alert('Error during checkout. Please try again.');
    }
  };

  const handleLogin = async (email) => {
    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL}/users?email=${email}`);
      if (response.ok) {
        const userData = await response.json();
        setUser(userData);
        setView('products');
      } else {
        alert('User not found. Please register.');
      }
    } catch (error) {
      console.error('Error during login:', error);
      alert('Error during login. Please try again.');
    }
  };

  const handleRegister = async (userData) => {
    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL}/users`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
      });

      if (response.ok) {
        const newUser = await response.json();
        setUser(newUser);
        setView('products');
      } else {
        alert('Registration failed. Please try again.');
      }
    } catch (error) {
      console.error('Error during registration:', error);
      alert('Error during registration. Please try again.');
    }
  };

  return (
    <div className="app-container">
      <header className="app-header">
        <h1>E-Commerce Store</h1>
        <nav>
          <button onClick={() => setView('products')}>Products</button>
          <button onClick={() => setView('cart')}>
            Cart ({cartItems.length})
          </button>
          <button onClick={() => setView('profile')}>
            {user ? 'Profile' : 'Login'}
          </button>
        </nav>
      </header>

      <main className="app-main">
        {view === 'products' && (
          <ProductList onAddToCart={handleAddToCart} />
        )}
        {view === 'cart' && (
          <Cart
            items={cartItems}
            onUpdateQuantity={handleUpdateQuantity}
            onRemoveItem={handleRemoveFromCart}
            onCheckout={handleCheckout}
          />
        )}
        {view === 'profile' && (
          <UserProfile
            user={user}
            onLogin={handleLogin}
            onRegister={handleRegister}
          />
        )}
      </main>
    </div>
  );
}

export default App;
