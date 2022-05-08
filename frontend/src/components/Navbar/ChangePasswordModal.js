import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

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
                    alert('이메일 전송 완료!');
                    setClickAuthkey(true);
                })
                .catch((res) => alert(JSON.parse(res.request.response).error));
            // axios 통신 실패시, '정상적인 접근 경로가 아닙니다.'
        } else {
            alert('이메일 형식을 확인해주세요!');
        }
    };

    return (
        <>
            <button
                onClick={() => {
                    openPasswordModal();
                    // console.log(showModal)
                }}
            >
                비밀번호 찾기
            </button>
            {showModal ? (
                <div>
                    <p>
                        비밀번호를 잃어버리셨나요? devU에 가입한 이메일을 정확히 입력해
                        주세요. 이메일을 통해 비밀번호 수정 링크가 전송됩니다.
                    </p>
                    <input
                        id="email"
                        name="email"
                        value={email}
                        onChange={(e) => onChangeEmail(e)}
                        placeholder="이메일"
                    />
                    {!clickAuthkey ? (
                        <button
                            onClick={() => {
                                passwordUrlEmail();
                            }}
                        >
                            비밀번호 찾기
                        </button>
                    ) : (
                        <div>
                            <button
                                onClick={() => {
                                    passwordUrlEmail();
                                }}
                            >
                                재전송
                            </button>
                            <p>만약 메일이 오지 않는다면, 스팸메일함을 확인해주세요</p>
                        </div>
                    )}
                </div>
            ) : null}
        </>
    );
}

export default ChangePasswordModal;
