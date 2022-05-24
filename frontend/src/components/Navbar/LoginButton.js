/* eslint-disable react-hooks/rules-of-hooks */
import React, { useState } from 'react';
import axios from 'axios';
import './loginButton.css';
import './loginModal.css';
import ChangePasswordModal from './ChangePasswordModal.js';
import logo from '../../img/logo_main.png';
import { useNavigate } from 'react-router-dom';

function LoginButton() {
  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const JWT_EXPIRY_TIME = 30 * 60 * 1000; // 만료 시간 (30분)
  const handleEmail = (e) => setEmail(e.target.value);
  const handlePassword = (e) => setPassword(e.target.value);
  const closeModal = () => setShowModal(false);
  const openModal = () => setShowModal(true);

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleLogin();
    }
  };

  const handleLogin = async () => {
    if (email === '' || password === '') {
      alert('이메일과 비밀번호를 입력해주세요.');
      return;
    }
    const data = {
      email: email,
      password: password,
    };

    await axios
      .post(process.env.REACT_APP_DB_HOST + '/signin', JSON.stringify(data), {
        headers: {
          'Content-Type': 'application/json',
        },
      })
      .then((res) => onLoginSuccess(res))
      .catch((res) => {
        console.log(res);
        alert(JSON.parse(res.request.response).error); // 이메일, 비밀번호 오류 출력
      });
  };

  const onSilentRefresh = () => {
    const data = {
      email: email,
      password: password,
    };
    axios
      .post(process.env.REACT_APP_DB_HOST + '/silent-refresh', data)
      .then(onLoginSuccess)
      .catch((res) => {
        console.log(res);
        alert(JSON.parse(res.request.response).error); // 이메일, 비밀번호 오류 출력
      });
  };

  const onLoginSuccess = (response) => {
    console.log(response);
    alert('로그인에 성공했습니다!');
    localStorage.setItem('username', response.data.username);
    localStorage.setItem(
      'accessToken',
      response.headers['x-auth-access-token']
    );
    window.location.reload(false);
    // axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
    // // accessToken 만료하기 1분 전에 로그인 연장
    setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000);
  };

  return (
    <>
      <button className="btn-login-nav" onClick={openModal}>
        로그인
      </button>
      {showModal ? (
        <div className="background">
          <div className="container-modal">
            <img className="logo-login" src={logo} alt="" />
            <button className="closeIcon" onClick={closeModal}>
              X
            </button>
            <div className="login-input">
              <input
                className="login-email-input"
                id="email"
                name="email"
                value={email}
                onChange={(e) => handleEmail(e)}
                placeholder="아이디를 입력하세요"
              />
              <input
                className="login-password-input"
                id="password"
                name="password"
                value={password}
                onChange={(e) => handlePassword(e)}
                type="password"
                placeholder="비밀번호를 입력하세요"
                onKeyPress={handleKeyPress}
              />
            </div>
            <div className="login-modal-middle">
              <div className="keep-login">
                <input className="switch" type="checkbox" id="switch" />
                <label for="switch" className="switch-label">
                  <span className="btn-onoff"></span>
                </label>
                <div className="login-text">로그인 상태 유지</div>
              </div>
              <ChangePasswordModal />
            </div>
            <div className="btns-login">
              <button className="btn-login" onClick={() => handleLogin()}>
                로그인
              </button>
              <button
                className="btn-login-register"
                onClick={() => {
                  navigate('/register');
                  closeModal();
                }}
              >
                회원가입
              </button>
            </div>
          </div>
        </div>
      ) : null}
    </>
  );
}

export default LoginButton;
