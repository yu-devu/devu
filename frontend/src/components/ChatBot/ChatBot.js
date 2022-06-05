import axios from 'axios';
import React, { useState } from 'react';
import chatbot from '../../img/chatbot.png';
import './chatbot.css';

const ChatBot = () => {
  const [weatherData, setWeatherData] = useState([]);
  let [modal, modalChange] = useState(true);
  let [restaurant, restaurantChange] = useState(false);

  const closeModal = () => {
    modalChange(false);
  };

  const showChatbot = () => {
    document.getElementById('chatbot').style.removeProperty('display');
    modalChange(true);
  };

  const welcomeMsg = () => {
    const botMessage = document.querySelector('#message1');
    botMessage.innerHTML = '무엇을 도와드릴까요?';
  };

  const handleFood = () => {
    const botMessage = document.querySelector('#message1');
    const userMessage = document.querySelector('#message2');
    botMessage.innerHTML = '입력 중...';
    setTimeout(() => {
      restaurantChange(true);
      document.getElementById('restaurant').style.removeProperty('display');
      botMessage.innerHTML = '어떤 식당의 메뉴가 궁금하신가요?';
      document.querySelector('#input').value = '';
    }, 2000);
    userMessage.innerHTML = document.querySelector('#food').value;
  };

  const handleHumanities = () => {
    const botMessage = document.querySelector('#message1');
    const userMessage = document.querySelector('#message2');
    botMessage.innerHTML = '입력 중...';
    setTimeout(() => {
      restaurantChange(false);
      botMessage.innerHTML = '인문계 학식은 영대돈까스입니다!';
      document.querySelector('#input').value = '';
    }, 2000);
    userMessage.innerHTML = document.querySelector('#humanities').value;
  };

  const handleNature = () => {
    const botMessage = document.querySelector('#message1');
    const userMessage = document.querySelector('#message2');
    botMessage.innerHTML = '입력 중...';
    setTimeout(() => {
      restaurantChange(false);
      botMessage.innerHTML = '자연계 학식은 불야돈까스입니다!';
      document.querySelector('#input').value = '';
    }, 2000);
    userMessage.innerHTML = document.querySelector('#nature').value;
  };

  const handleStaff = () => {
    const botMessage = document.querySelector('#message1');
    const userMessage = document.querySelector('#message2');
    botMessage.innerHTML = '입력 중...';
    setTimeout(() => {
      restaurantChange(false);
      botMessage.innerHTML = '교직원 학식은 된장찌개입니다!';
      document.querySelector('#input').value = '';
    }, 2000);
    userMessage.innerHTML = document.querySelector('#staff').value;
  };

  const handleWeather = async () => {
    const botMessage = document.querySelector('#message1');
    const userMessage = document.querySelector('#message2');
    botMessage.innerHTML = '입력 중...';

    let now = new Date();
    let hours = now.getHours();
    let minutes = now.getMinutes();
    let seconds = now.getSeconds();
    let year = now.getFullYear();
    let month = now.getMonth() + 1;
    let date = now.getDate();

    const baseDate = year * 10000 + month * 100 + date;
    const bt = hours * 100 + minutes;

    const baseTime = 200 < bt < 500 ? 200 : 500 < bt < 800 ? 500 : 800 < bt < 1100 ? 800 : 1100 < bt < 1400 ? 1100 : 1400 < bt < 1700 ? 1400 : 1700 < bt < 2000 ? 1700 : 2000 < bt < 2300 ? 2000 : 2300

    console.log(baseTime)
    const res = await axios.get(
      process.env.REACT_APP_DB_HOST + '/api/weather',
      {
        params: {
          baseDate: baseDate,
          baseTime: baseTime,
        },
      });
    const _weatherData = {
      sky: res.data.reformed.SKY, // 하늘 상태
      pop: res.data.reformed.POP, // 강수 확률
      uuu: res.data.reformed.UUU, // 풍속(동서)
      pty: res.data.reformed.PTY, // 강수 형태
      reh: res.data.reformed.REH, // 습도
      vec: res.data.reformed.VEC, // 풍향
      tmp: res.data.reformed.TMP, // 기온
      vvv: res.data.reformed.VVV, // 풍속(남북)
      wsd: res.data.reformed.WSD, // 풍속
      pcp: res.data.reformed.PCP, // 강수량
      wav: res.data.reformed.WAV, // 파고
    };
    setWeatherData(_weatherData)
    setTimeout(() => {
      botMessage.innerHTML = "현재 날씨는 " + (weatherData.sky == 4 ? "흐림" : weatherData.sky == 3 ? "구름 많음" : "맑음") + "입니다! \n기온은 " + weatherData.tmp + "°C이며 습도는 " + weatherData.reh + "%입니다!";
      // document.querySelector('#input').value = '';
    }, 2000);
    userMessage.innerHTML = document.querySelector('#weather').value;
  };

  const handleSubway = async () => {
    const botMessage = document.querySelector('#message1');
    const userMessage = document.querySelector('#message2');
    botMessage.innerHTML = '입력 중...';
    const today = new Date();
    const DateH = today.getHours();
    const DateM = today.getMinutes();
    let i = 0;
    // const DateS = today.getSeconds();
    if (
      DateH < 5 ||
      (DateH === 5 && DateM < 30) ||
      (DateH === 23 && DateM > 18)
    ) {
      setTimeout(() => {
        console.log(DateH);
        botMessage.innerHTML = '첫차는 5:30에 들어옵니다!';
        document.querySelector('#input').value = '';
      }, 1000);
    } else {
      const DateD = today.getDay();
      let leftTime = 0;
      let arrInfo = [];

      if (DateD === 0)
        await axios
          .get(process.env.REACT_APP_DB_HOST + '/api/holidaySubway') // 일요일
          .then((e) => (arrInfo = e.data))
          .catch((res) => console.log('false'));
      else if (DateD > 0 && DateD < 6)
        await axios
          .get(process.env.REACT_APP_DB_HOST + '/api/weekdaySubway ') // 평일
          .then((e) => (arrInfo = e.data))
          .catch((res) => console.log('false'));
      else
        await axios
          .get(process.env.REACT_APP_DB_HOST + '/api/weekendSubway') // 토요일
          .then((e) => (arrInfo = e.data))
          .catch((res) => console.log('false'));

      for (i = 0; i < arrInfo.length; i++) {
        const candiDate = arrInfo[i].split(':');
        if (candiDate[0] == DateH) {
          if (candiDate[1] > DateM) {
            leftTime = Number(candiDate[1]) - DateM;
            break;
          }
        } else if (candiDate[0] > DateH) break;
      }
      if (leftTime === 0) {
        const candiDate = arrInfo[i].split(':');
        leftTime = Number(candiDate[1]) - DateM + 60;
      }

      setTimeout(() => {
        botMessage.innerHTML = leftTime + '분 뒤에 지하철이 들어옵니다!';
        document.querySelector('#input').value = '';
      }, 1000);
    }

    userMessage.innerHTML = document.querySelector('#subway').value;
  };

  return (
    <div>
      <div
        id="chatbot"
        style={{ display: 'none' }}
        className={modal ? '' : 'remove-class'}
      >
        <div className="chatbot-all" onLoad={welcomeMsg}>
          <div className="chatbot-wrapper">
            <div className="chatbot-content">
              <div className="top-chatbot">
                <div className="profile-chatbot">
                  <img className="img-chatbot" src={chatbot} alt="" />
                </div>
                <div className="right-chatbot">
                  <div className="name-chatbot">DEVU 도우미</div>
                </div>
                <button className="btn-close" onClick={closeModal}>
                  <div className="img-close">X</div>
                </button>
              </div>
              <hr className="line-chatbot" />
              <div className="main-chatbot">
                <div className="main-chatbot-content">
                  <div className="chatbot-messages">
                    <div className="bot-message" id="message1"></div>
                    <div className="human-message" id="message2">
                      ...
                    </div>
                  </div>
                </div>
              </div>
              <div className="chatbot-options">
                <div
                  id="restaurant"
                  style={{ display: 'none' }}
                  className={restaurant ? '' : 'remove-class'}
                >
                  <div id="restaurant" className="bottom-chatbot-food">
                    <button
                      className="humanities"
                      id="humanities"
                      onClick={handleHumanities}
                      value="인문계 식당 메뉴 알려줘!"
                    >
                      인문계 식당
                    </button>
                    <button
                      className="nature"
                      id="nature"
                      onClick={handleNature}
                      value="자연계 식당 메뉴 알려줘!"
                    >
                      자연계 식당
                    </button>
                    <button
                      className="staff"
                      id="staff"
                      onClick={handleStaff}
                      value="교직원 식당 메뉴 알려줘!"
                    >
                      교직원 식당
                    </button>
                  </div>
                </div>
                <div className="bottom-chatbot">
                  <button
                    className="food"
                    id="food"
                    onClick={handleFood}
                    value="오늘 학식은 뭐야?"
                  >
                    오늘의 학식
                  </button>
                  <button
                    className="weather"
                    id="weather"
                    onClick={handleWeather}
                    value="현재 날씨 어때?"
                  >
                    현재 날씨
                  </button>
                  <button
                    className="subway"
                    id="subway"
                    onClick={handleSubway}
                    value="지하철 현황 알려줘"
                  >
                    지하철 현황
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <button className="btn-mini-chatbot" onClick={showChatbot}>
        <img className="img-mini-chatbot" src={chatbot} alt="" />
      </button>
    </div>
  );
};

export default ChatBot;
