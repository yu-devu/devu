import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './loginButton.css';
import './loginModal.css';

function LoginButton() {
  const [showModal, setShowModal] = useState(false);

  const closeModal = () => {
    setShowModal(false);
  };

  const openModal = () => {
    setShowModal(true);
  };
  return (
    <>
      <button className="btn-login-nav" onClick={openModal}>
        로그인
      </button>
      {showModal ? (
        <div className="background">
          <div className="container-modal">
            <h1>로그인</h1>
            <button className="closeIcon" onClick={closeModal}>
              X
            </button>
            <input
              className="register-input"
              id="email"
              name="email"
              placeholder="이메일"
            />
            <input
              className="register-input"
              id="nickname"
              name="nickname"
              placeholder="비밀번호"
            />
            <button className="btn-validate">로그인</button>
          </div>
        </div>
      ) : null}
    </>
  );
}

export default LoginButton;
