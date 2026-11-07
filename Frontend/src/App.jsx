import React, { useState } from 'react';
import Login from './Login';
import './App.css';

export default function App() {
  const [currentView, setCurrentView] = useState('splash'); // 'splash', 'login', or 'dashboard'
  const [loggedInUser, setLoggedInUser] = useState(null);

  const handleLoginSuccess = (user) => {
    setLoggedInUser(user);
    setCurrentView('dashboard');
  };

  return (
    <div className="app-container">
      {currentView === 'splash' && (
        <div className="splash-hero">
          <div className="splash-content-left">
            <div className="splash-pill-badge">
              Academic analytics for universities
            </div>
            
            <h1 className="splash-headline">
              See Every Grade.<br />
              Catch Every Risk.
            </h1>
            
            <p className="splash-description">
              EduPulse replaces paper gradebooks and scattered spreadsheets with one dashboard for grades, attendance, and performance tracking — for administrators, teachers, and students.
            </p>

            <div className="splash-btn-group">
              <button onClick={() => setCurrentView('login')} className="btn-primary-solid">
                Sign in to your portal
              </button>
              <button onClick={() => alert("Explore features coming soon!")} className="btn-secondary-outline">
                Explore the Features
              </button>
            </div>
          </div>

          <div className="splash-image-right"></div>
        </div>
      )}

      {currentView === 'login' && (
        <Login 
          onBackToSplash={() => setCurrentView('splash')} 
          onLoginSuccess={handleLoginSuccess} 
        />
      )}

      {currentView === 'dashboard' && (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', padding: '20px' }}>
          <div className="splash-card" style={{ maxWidth: '520px', padding: '40px', textAlign: 'center', background: 'rgba(43, 8, 16, 0.85)', borderRadius: '20px', color: '#fff', border: '1px solid rgba(255,255,255,0.15)' }}>
            <div style={{ fontSize: '3rem', marginBottom: '15px' }}>🎉</div>
            <h1 style={{ fontSize: '2rem', marginBottom: '10px' }}>Welcome, {loggedInUser?.first_name} {loggedInUser?.last_name}!</h1>
            <p style={{ color: '#d1d5db', marginBottom: '8px' }}>Role: <strong>{loggedInUser?.is_admin == 1 ? 'Administrator' : 'User'}</strong></p>
            <p style={{ color: '#d1d5db', marginBottom: '24px' }}>Email: {loggedInUser?.email}</p>
            <button 
              onClick={() => { setLoggedInUser(null); setCurrentView('splash'); }}
              className="btn-primary-solid"
              style={{ width: '100%', background: '#dc2626', color: '#fff' }}
            >
              Sign Out
            </button>
          </div>
        </div>
      )}
    </div>
  );
}