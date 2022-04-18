import React from 'react';
import './logoutButton.css';

const LogoutButton = () => {
  const logout = () => {
    localStorage.removeItem('accessToken');
    window.location.reload(false);
  };
  return (
    <>
      <button className="btn-logout-nav" onClick={logout}>
        로그아웃
      </button>
    </>
  );
};

export default LogoutButton;
