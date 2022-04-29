import React from "react";
import ChatBot from "../ChatBot/ChatBot";
import "./home.css";
import { footerItems } from "./FooterItems";
import { Link } from "react-router-dom";
import Main from "../../img/main.png"
import Footer from "../../img/logo_black.png"

const Home = () => {
  return (
    <div>
      <ChatBot />
      <div className="main-all">
        <div className="intro">
          <div className="intro-content">
            <h1 class="top-word">나누는 지식만큼</h1>
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
        <div className="footer">
          <ul className="footer-item">
            {footerItems.map((item) => {
              return (
                <li key={item.id} className={item.cName}>
                  <Link to={item.path}>{item.title}</Link>
                </li>
              );
            })}
          </ul>
          <hr className="line" />
          <div className="footer-intro">
            <img className="img-footer" alt="" src={Footer} />
            <h3>(주)데뷰   대표자 : 홍길동  | 사업자등록번호 : 123-456-789</h3>
            <h3>주소 : 경상북도 경산시 대학로 280</h3>
            <br></br>
            <h3 className="copyright">@DEVU.ALL.RIGHTS RESERVED</h3>
          </div>
        </div>
      </div>
    </div>

  );
};

export default Home;