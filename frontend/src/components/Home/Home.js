import React from "react";
import ChatBot from "../ChatBot/ChatBot";
import "./home.css";

const Home = () => {
  return (
    <div>
      <ChatBot />
      <div className="main-all">
        <div className="intro">
          <h3>Devu란?</h3>
          <hr></hr>
          <br></br>
          <h5>
            Devu는 아주 멋진 서비스입니다. blabalbalbalbalbllbalb Lorem ipsum
            dolor sit amet consectetur, adipisicing elit. Earum architecto et enim
            necessitatibus provident, delectus quia ducimus veritatis
            exercitationem ad expedita quibusdam quam, praesentium odit molestiae
            reprehenderit amet libero at.
          </h5>
        </div>
      </div>
    </div>

  );
};

export default Home;
