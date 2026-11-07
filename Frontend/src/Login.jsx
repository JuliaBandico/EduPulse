import React, { useState } from 'react';
import './Login.css'; // Import the CSS styles below

export default function Login({ onBackToSplash, onLoginSuccess }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await fetch('http://localhost:8012/edupulse-api/login.php', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();

      if (data.status === 'success') {
        if (onLoginSuccess) onLoginSuccess(data.user);
      } else {
        setError(data.message || 'Invalid email or password');
      }
    } catch (err) {
      setError('Unable to connect to the server. Please check XAMPP.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        {/* Brand Header */}
        <div className="login-header">
          <div className="brand-badge">
            <span className="brand-icon">🎓</span>
            <span className="brand-name">EduPulse</span>
          </div>
          <h2>Sign In</h2>
          <p>Enter your credentials to access your portal</p>
        </div>

        {/* Error Alert */}
        {error && <div className="error-message">{error}</div>}

        {/* Login Form */}
        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="email">Email Address</label>
            <input
              id="email"
              type="email"
              placeholder="e.g. admin@edupulse.edu"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="login-btn" disabled={loading}>
            {loading ? 'Signing In...' : 'Log In'}
          </button>
        </form>

        {/* Back Link */}
        <button onClick={onBackToSplash} className="back-btn">
          ← Back to Splash
        </button>
      </div>
    </div>
  );
}