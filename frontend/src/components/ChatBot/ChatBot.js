import axios from 'axios';
import React, { useState } from 'react';
import chatbot from '../../img/chatbot.png';
import './chatbot.css';

const ChatBot = () => {
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

  // const handleInput = () => {
  //     const botMessage = document.querySelector('#message1');
  //     const userMessage = document.querySelector('#message2');

  //     let food = ['학식|밥'];
  //     let words = new RegExp(food);
  //     if (words.test(document.querySelector('#input').value)) {
  //         botMessage.innerHTML = '입력 중...'
  //         setTimeout(() => {
  //             botMessage.innerHTML = "오늘 학식은 돈까스입니다!"
  //             document.querySelector('#input').value = "";
  //         }, 2000);
  //     }
  //     userMessage.innerHTML = document.querySelector('#input').value;
  // }

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
    const baseDate = '20220523';
    const baseTime = '2300';

    const formData = new FormData();
    formData.append('baseDate', baseDate);
    formData.append('baseTime', baseTime);

    // console.log(formData);

    const res = await axios
      .get(process.env.REACT_APP_DB_HOST + '/api/weather', {
        params: {
          baseDate: baseDate,
          baseTime: baseTime,
        },
      })
      //   .get(
      //     'http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=PyNY0qeRt39Rj07xn2QCs%2BokqrxCfg%2FJkw0RaONPBX3GybvBQjznoLKfITYrjhDTz7bKwND%2BozBbbqHwS89T7Q%3D%3D&numOfRows=10&pageNo=1&base_date=20220523&base_time=2300&nx=92&ny=90&dataType=JSON',
      //     {
      //       headers: {
      //         // 'Content-Type': 'application/json',
      //         // 'Access-Control-Allow-Credentials': true,
      //         // Authorization: `${localStorage.getItem('accessToken')}`,
      //         // 'X-AUTH-ACCESS-TOKEN': `${localStorage.getItem('accessToken')}`,
      //       },
      //     }
      //     // 'https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=PyNY0qeRt39Rj07xn2QCs%2BokqrxCfg%2FJkw0RaONPBX3GybvBQjznoLKfITYrjhDTz7bKwND%2BozBbbqHwS89T7Q%3D%3D&numOfRows=10&pageNo=1&base_date=20220523&base_time=2300&nx=92&ny=90&dataType=JSON'
      //   )
      //   .get(process.env.REACT_APP_DB_HOST + '/api/weather', formData, {
      //     headers: {
      //       'Content-Type': 'application/json',
      //       Authorization: `${localStorage.getItem('accessToken')}`,
      //       'X-AUTH-ACCESS-TOKEN': `${localStorage.getItem('accessToken')}`,
      //     },
      //   })
      .then(() => {
        console.log('success');
      })
      .catch((res) => {
        console.log('false');
      });
    console.log(res);
    setTimeout(() => {
      botMessage.innerHTML = '현재 날씨는 맑음입니다!';
      document.querySelector('#input').value = '';
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
      }, 2000);
    } else {
      const DateD = today.getDay();
      let leftTime = 0;

      if (DateD === 0) {
        // 일요일
        await axios
          .get(process.env.REACT_APP_DB_HOST + '/api/holidaySubway')
          .then((e) => {
            for (i = 0; i < e.data.length; i++) {
              const candiDate = e.data[i].split(':');
              if (candiDate[0] == DateH) {
                if (candiDate[1] > DateM) {
                  leftTime = Number(candiDate[1]) - DateM;
                  break;
                }
              } else if (candiDate[0] > DateH) break;
            }
            if (leftTime === 0) {
              const candiDate = e.data[i].split(':');
              leftTime = Number(candiDate[1]) - DateM + 60;
            }
            console.log('일요일 leftTime = ' + leftTime);
          })
          .catch((res) => {
            console.log('false');
          });
      } else if (DateD > 0 && DateD < 6) {
        // 평일
        await axios
          .get(process.env.REACT_APP_DB_HOST + '/api/weekdaySubway ')
          .then((e) => {
            for (i = 0; i < e.data.length; i++) {
              const candiDate = e.data[i].split(':');
              if (candiDate[0] == DateH) {
                if (candiDate[1] > DateM) {
                  leftTime = Number(candiDate[1]) - DateM;
                  break;
                }
              } else if (candiDate[0] > DateH) break;
            }
            if (leftTime === 0) {
              const candiDate = e.data[i].split(':');
              leftTime = Number(candiDate[1]) - DateM + 60;
            }
            console.log('평일 leftTime = ' + leftTime);
          })
          .catch((res) => {
            console.log('false');
          });
      } else {
        // 토요일
        await axios
          .get(process.env.REACT_APP_DB_HOST + '/api/weekendSubway')
          .then((e) => {
            for (i = 0; i < e.data.length; i++) {
              const candiDate = e.data[i].split(':');
              if (candiDate[0] == DateH) {
                if (candiDate[1] > DateM) {
                  leftTime = Number(candiDate[1]) - DateM;
                  break;
                }
              } else if (candiDate[0] > DateH) break;
            }
            if (leftTime === 0) {
              const candiDate = e.data[i].split(':');
              leftTime = Number(candiDate[1]) - DateM + 60;
            }
            console.log('토요일 leftTime = ' + leftTime);
          })
          .catch((res) => {
            console.log('false');
          });
      }

      setTimeout(() => {
        botMessage.innerHTML = leftTime + '분 뒤에 지하철이 들어옵니다!';
        document.querySelector('#input').value = '';
      }, 2000);
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
