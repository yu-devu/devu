/* eslint-disable react-hooks/rules-of-hooks */
import React, { useState } from 'react';
import axios from 'axios';
// import { useNavigate } from 'react-router-dom';
import './loginButton.css';
import './loginModal.css';
import { useRecoilState } from 'recoil';

const url = 'http://54.180.29.69:8080';

function LoginButton() {
  // const navigate = useNavigate();
  const [showModal, setShowModal] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [tokenData, setTokenData] = useState('');

  const handleEmail = (e) => setEmail(e.target.value);
  const handlePassword = (e) => setPassword(e.target.value);
  const closeModal = () => setShowModal(false);
  const openModal = () => setShowModal(true);

  const handleLogin = async () => {
    if (email === '' || password === '') {
      alert('아이디와 비밀번호를 입력해주세요.');
      return;
    }
    const data = {
      email: email,
      password: password,
    };

    console.log(JSON.stringify(data));
    await axios
      .post(url + '/signin', JSON.stringify(data), {
        headers: {
          'Content-Type': 'application/json',
        },
      })
      .then((res) => {
        alert('로그인에 성공했습니다!');
        console.log(res);
        localStorage.setItem('token', res.data.accessToken);
        window.location.reload(false);
      })
      .catch((error) => console.log(error));
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
            />
            <button className="btn-validate" onClick={() => handleLogin()}>
              로그인
            </button>
          </div>
        </div>
      ) : null}
    </>
  );
}

export default LoginButton;
