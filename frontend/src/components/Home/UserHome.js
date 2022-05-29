import React from "react";
import ChatBot from "../ChatBot/ChatBot";
import "./userhome.css";
import Main from "../../img/main_user.png"
import Footer from "./Footer";
import { useNavigate } from 'react-router-dom';

const UserHome = () => {
    const navigate = useNavigate();
    const username = localStorage.getItem('username');
    return (
        <div>
            <ChatBot />
            <div className="main-all-user">
                <div className="intro">
                    <div className="intro-content-user">
                        <h1 className="top-word">안녕하세요 {username}님!</h1>
                        <h1>기말고사가 8일 남았어요</h1>
                        <br></br>
                        <h2 className="text-userhome">
                            과목별 시험정보를 확인해보세요.
                        </h2>
                        <a className="btn-urp" href="https://portal.yu.ac.kr">강의포털 바로가기 &gt;</a>
                    </div>
                    <img className="img-main" alt="" src={Main} />
                </div>
            </div>
            <Footer />
        </div>

    );
};

export default UserHome;