import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const url = 'http://54.180.29.69:8080';

function ChangePassword() {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [checkPassword, setCheckPassword] = useState('');

    const onChangePassword = (e) => setPassword(e.target.value);
    const onChangeCheckPassword = (e) => setCheckPassword(e.target.value);

    useEffect(() => {
        getChangePassword();
    }, []);
    const getChangePassword = async () => {
        const res = await axios.get(url + `change_password/${email}`);
        console.log(res);
    }
    const postChangePassword = async () => {
        if (password == checkPassword) {
            const data = {
                password: password,
            }
            await axios.post(url + `change_password/${email}`, JSON.stringify(data), {
                header: {
                    'Content-Type': 'application/json',
                }
            }).then((res) => {
                console.log(res);
                alert('비밀번호 변경 완료');
                navigate.go(0);
            }
            ).catch((res) => {
                alert(JSON.parse(res.request.response).error);
            });
        }
        else {
            alert('비밀번호가 일치하지 않습니다.');
        }
    }

    return (
        <>
            <p>변경할 비밀번호를 입력해주세요.</p>
            <div>
                <input
                    id="password"
                    name="password"
                    value={password}
                    type="password"
                    onChange={(e) => onChangePassword(e)}
                    placeholder="비밀번호"
                />
            </div>
            <div>
                <input
                    id="checkPassword"
                    name="checkPassword"
                    value={checkPassword}
                    type="password"
                    onChange={(e) => onChangeCheckPassword(e)}
                    placeholder="비밀번호 확인"
                />
            </div>
            <div>
                <button onClick={() => { postChangePassword(); }}>비밀번호 변경</button>
            </div>
        </>
    );
}

export default ChangePassword;