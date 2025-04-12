import { useState } from 'react';

const UserProfile = ({ user, onLogin, onRegister }) => {
  const [isRegistering, setIsRegistering] = useState(false);
  const [formData, setFormData] = useState({
    email: '',
    name: '',
    address: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (isRegistering) {
      await onRegister(formData);
    } else {
      await onLogin(formData.email);
    }
  };

  if (user) {
    return (
      <div className="user-profile">
        <h2>Profile</h2>
        <div className="profile-details">
          <p><strong>Name:</strong> {user.attributes.name}</p>
          <p><strong>Email:</strong> {user.attributes.email}</p>
          <p><strong>Address:</strong> {user.attributes.address}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="auth-container">
      <h2>{isRegistering ? 'Register' : 'Login'}</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        {isRegistering && (
          <>
            <div className="form-group">
              <label>Name:</label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group">
              <label>Address:</label>
              <textarea
                name="address"
                value={formData.address}
                onChange={handleChange}
                required
              />
            </div>
          </>
        )}

        <button type="submit">
          {isRegistering ? 'Register' : 'Login'}
        </button>
      </form>

      <button 
        className="toggle-auth"
        onClick={() => setIsRegistering(!isRegistering)}
      >
        {isRegistering 
          ? 'Already have an account? Login' 
          : 'Need an account? Register'}
      </button>
    </div>
  );
};

export default UserProfile;
