import { useState, useEffect } from 'react';

const Cart = ({ items, onUpdateQuantity, onRemoveItem, onCheckout }) => {
  const [total, setTotal] = useState(0);

  useEffect(() => {
    const newTotal = items.reduce((sum, item) => {
      return sum + (item.attributes.price * item.quantity);
    }, 0);
    setTotal(newTotal);
  }, [items]);

  return (
    <div className="cart-container">
      <h2>Shopping Cart</h2>
      {items.length === 0 ? (
        <p>Your cart is empty</p>
      ) : (
        <>
          <div className="cart-items">
            {items.map(item => (
              <div key={item.pk} className="cart-item">
                <div className="item-details">
                  <h3>{item.attributes.name}</h3>
                  <p>${item.attributes.price}</p>
                </div>
                <div className="item-actions">
                  <button 
                    onClick={() => onUpdateQuantity(item, item.quantity - 1)}
                    disabled={item.quantity <= 1}
                  >
                    -
                  </button>
                  <span>{item.quantity}</span>
                  <button 
                    onClick={() => onUpdateQuantity(item, item.quantity + 1)}
                    disabled={item.quantity >= item.attributes.stock}
                  >
                    +
                  </button>
                  <button 
                    onClick={() => onRemoveItem(item)}
                    className="remove-btn"
                  >
                    Remove
                  </button>
                </div>
              </div>
            ))}
          </div>
          <div className="cart-summary">
            <h3>Total: ${total.toFixed(2)}</h3>
            <button 
              onClick={() => onCheckout(items, total)}
              className="checkout-btn"
            >
              Proceed to Checkout
            </button>
          </div>
        </>
      )}
    </div>
  );
};

export default Cart;
