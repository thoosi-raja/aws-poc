import { useState, useEffect } from 'react';

const ProductList = ({ onAddToCart }) => {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState(['Electronics', 'Books', 'Clothing']);
  const [selectedCategory, setSelectedCategory] = useState('Electronics');

  useEffect(() => {
    fetchProducts();
  }, [selectedCategory]);

  const fetchProducts = async () => {
    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL}/products/category/${selectedCategory}`);
      if (response.ok) {
        const data = await response.json();
        setProducts(data);
      }
    } catch (error) {
      console.error('Error fetching products:', error);
    }
  };

  return (
    <div className="products-container">
      <div className="category-filter">
        <h3>Categories</h3>
        <select 
          value={selectedCategory} 
          onChange={(e) => setSelectedCategory(e.target.value)}
        >
          {categories.map(category => (
            <option key={category} value={category}>{category}</option>
          ))}
        </select>
      </div>

      <div className="products-grid">
        {products.map(product => {
          const attrs = product.attributes;
          return (
            <div key={product.pk} className="product-card">
              <h3>{attrs.name}</h3>
              <p className="price">${attrs.price}</p>
              <p>Stock: {attrs.stock}</p>
              <button 
                onClick={() => onAddToCart(product)}
                disabled={attrs.stock <= 0}
              >
                Add to Cart
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default ProductList;
