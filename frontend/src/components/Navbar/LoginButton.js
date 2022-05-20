/* eslint-disable react-hooks/rules-of-hooks */
import React, { useState } from 'react';
import axios from 'axios';
import './loginButton.css';
import './loginModal.css';
import ChangePasswordModal from './ChangePasswordModal.js'

function LoginButton() {
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
  }

  const handleLogin = async () => {
    if (email === '' || password === '') {
      alert('아이디와 비밀번호를 입력해주세요.');
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
          'X-AUTH-ACCESS-TOKEN': `${localStorage.getItem('accessToken')}`,
        },
      })
      .then((res) =>
        onLoginSuccess(res)
      )
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
    axios.post(process.env.REACT_APP_DB_HOST + '/silent-refresh', data)
      .then(onLoginSuccess)
      .catch((res) => {
        console.log(res);
        alert(JSON.parse(res.request.response).error); // 이메일, 비밀번호 오류 출력
      });
  }

  const onLoginSuccess = response => {
    console.log(response);
    alert('로그인에 성공했습니다!');
    localStorage.setItem('username', response.data.username);
    localStorage.setItem('accessToken', response.headers["x-auth-access-token"]);
    window.location.reload(false);
    // axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
    // // accessToken 만료하기 1분 전에 로그인 연장
    setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000);
  }

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
              value={email}
              onChange={(e) => handleEmail(e)}
              placeholder="이메일"
            />
            <input
              className="register-input"
              id="password"
              name="password"
              value={password}
              onChange={(e) => handlePassword(e)}
              type="password"
              placeholder="비밀번호"
              onKeyPress={handleKeyPress}
            />
            <button className="btn-validate" onClick={() => handleLogin()}>
              로그인
            </button>
            <ChangePasswordModal />
          </div>
        </div>
      ) : null}
    </>
  );
}

export default LoginButton;
