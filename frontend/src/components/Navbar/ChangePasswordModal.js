import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import logo from '../../img/logo_main.png';
import axios from 'axios';
import './changePasswordModal.css';

function ChangePasswordModal() {
  const [showModal, setShowModal] = useState(false);
  const [email, setEmail] = useState('');
  const referUrl = 'http://54.180.29.69/';
  // 백엔드 ChangePasswordApiController 27번째 줄에 http://localhost:3000도 추가해야 할 듯함
  const closePasswordModal = () => setShowModal(false);
  const openPasswordModal = () => setShowModal(true);
  const onChangeEmail = (e) => setEmail(e.target.value);
  const [clickAuthkey, setClickAuthkey] = useState(false);

  const passwordUrlEmail = async () => {
    if (email !== '') {
      if (email.includes('@ynu.ac.kr') || email.includes('@yu.ac.kr')) {
        const data = {
          referUrl: referUrl,
          email: email,
        };
        await axios
          .post(
            process.env.REACT_APP_DB_HOST + `/api/password_url_email`,
            JSON.stringify(data),
            {
              headers: {
                'Content-Type': 'application/json',
              },
            }
          )
          .then(() => {
            alert(
              '이메일 전송 완료! \n이메일이 오지 않을 경우 스팸 이메일함을 확인해보세요.'
            );
            setClickAuthkey(true);
          })
          .catch((e) => alert(JSON.parse(e.request.response).error));
        // axios 통신 실패시, '정상적인 접근 경로가 아닙니다.'
      } else alert('이메일 형식을 확인해주세요!');
    } else alert('이메일을 입력해주세요!');
  };

  return (
    <>
      <button
        className="btn-findpw"
        onClick={() => {
          openPasswordModal();
        }}
      >
        비밀번호 찾기
      </button>
      {showModal ? (
        <div>
          <div className="background">
            <div className="container-modal-pw">
              <img className="logo-login" src={logo} alt="" />
              <button className="closeIcon" onClick={closePasswordModal}>
                X
              </button>
              <div className="login-input">
                <h2>Devu에 가입한 이메일을 정확히 입력해주세요.</h2>
                <input
                  className="input-email"
                  id="email"
                  name="email"
                  value={email}
                  onChange={(e) => onChangeEmail(e)}
                  placeholder="이메일"
                />
                {!clickAuthkey ? (
                  <button
                    className="btn-getemail"
                    onClick={() => {
                      passwordUrlEmail();
                    }}
                  >
                    비밀번호 찾기
                  </button>
                ) : (
                  <div>
                    <button
                      className="btn-getemail"
                      onClick={() => {
                        passwordUrlEmail();
                      }}
                    >
                      재전송
                    </button>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      ) : null}
    </>
  );
}

export default ChangePasswordModal;
