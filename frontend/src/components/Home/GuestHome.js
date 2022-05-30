import React from "react";
import ChatBot from "../ChatBot/ChatBot";
import "./guesthome.css";
import Main from "../../img/main_guest.png"
import Footer from "./Footer";

const GuestHome = () => {
  return (
    <div>
      <ChatBot />
      <div className="main-all">
        <div className="intro">
          <div className="intro-content">
            <h1 className="top-word">나누는 지식만큼</h1>
            <h1>커지는 보람과 보상</h1>
            <br></br>
            <h2>
              데뷰 지식공유자로
            </h2>
            <h2>
              높은 수익과 가치를 만들어보세요.
            </h2>
          </div>
          <img className="img-main" alt="" src={Main} />
        </div>
      </div>
      <Footer />
    </div>

  );
};

export default GuestHome;