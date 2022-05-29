import React from 'react';
import axios from 'axios';
import './logoutButton.css';

const LogoutButton = () => {
  const logout = async () => {
    await axios.post(process.env.REACT_APP_DB_HOST + `/logout`)
      .then(() => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('username');
        window.location.reload(false);
      });
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
