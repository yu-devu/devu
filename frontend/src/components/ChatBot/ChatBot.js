import React, { useState } from 'react'
import chatbot from '../../img/chatbot.png'
import './chatbot.css'

const ChatBot = () => {
    let [modal, modalChange] = useState(true)
    let [restaurant, restaurantChange] = useState(false)

    const closeModal = () => {
        modalChange(false)
    }

    const showChatbot = () => {
        document.getElementById('chatbot').style.removeProperty('display')
        modalChange(true)
    }

    const welcomeMsg = () => {
        const botMessage = document.querySelector('#message1');
        botMessage.innerHTML = '무엇을 도와드릴까요?';
    }

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
        botMessage.innerHTML = '입력 중...'
        setTimeout(() => {
            restaurantChange(true)
            document.getElementById('restaurant').style.removeProperty('display')
            botMessage.innerHTML = "어떤 식당의 메뉴가 궁금하신가요?"
            document.querySelector('#input').value = "";
        }, 2000);
        userMessage.innerHTML = document.querySelector('#food').value;
    }

    const handleHumanities = () => {
        const botMessage = document.querySelector('#message1');
        const userMessage = document.querySelector('#message2');
        botMessage.innerHTML = '입력 중...'
        setTimeout(() => {
            restaurantChange(false)
            botMessage.innerHTML = "인문계 학식은 영대돈까스입니다!"
            document.querySelector('#input').value = "";
        }, 2000);
        userMessage.innerHTML = document.querySelector('#humanities').value;
    }

    const handleNature = () => {
        const botMessage = document.querySelector('#message1');
        const userMessage = document.querySelector('#message2');
        botMessage.innerHTML = '입력 중...'
        setTimeout(() => {
            restaurantChange(false)
            botMessage.innerHTML = "자연계 학식은 불야돈까스입니다!"
            document.querySelector('#input').value = "";
        }, 2000);
        userMessage.innerHTML = document.querySelector('#nature').value;
    }

    const handleStaff = () => {
        const botMessage = document.querySelector('#message1');
        const userMessage = document.querySelector('#message2');
        botMessage.innerHTML = '입력 중...'
        setTimeout(() => {
            restaurantChange(false)
            botMessage.innerHTML = "교직원 학식은 된장찌개입니다!"
            document.querySelector('#input').value = "";
        }, 2000);
        userMessage.innerHTML = document.querySelector('#staff').value;
    }

    const handleWeather = () => {
        const botMessage = document.querySelector('#message1');
        const userMessage = document.querySelector('#message2');
        botMessage.innerHTML = '입력 중...'
        setTimeout(() => {
            botMessage.innerHTML = "현재 날씨는 맑음입니다!"
            document.querySelector('#input').value = "";
        }, 2000);
        userMessage.innerHTML = document.querySelector('#weather').value;
    }

    const handleSubway = () => {
        const botMessage = document.querySelector('#message1');
        const userMessage = document.querySelector('#message2');
        botMessage.innerHTML = '입력 중...'
        setTimeout(() => {
            botMessage.innerHTML = "3분 뒤에 지하철이 들어옵니다!"
            document.querySelector('#input').value = "";
        }, 2000);
        userMessage.innerHTML = document.querySelector('#subway').value;
    }

    return (
        <div>
            <div id='chatbot' style={{ display: "none" }} className={modal ? "" : "remove-class"}>
                <div className='chatbot-all' onLoad={welcomeMsg}>
                    <div className='chatbot-wrapper'>
                        <div className='chatbot-content'>
                            <div className='top-chatbot'>
                                <div className='profile-chatbot'>
                                    <img className='img-chatbot' src={chatbot} alt="" />
                                </div>
                                <div className='right-chatbot'>
                                    <div className='name-chatbot'>DEVU 도우미</div>
                                </div>
                                <button className='btn-close' onClick={closeModal}><div className='img-close'>X</div></button>
                            </div>
                            <hr className="line-chatbot" />
                            <div className='main-chatbot'>
                                <div className='main-chatbot-content'>
                                    <div className='chatbot-messages'>
                                        <div className='bot-message' id='message1'></div>
                                        <div className='human-message' id="message2">...</div>
                                    </div>
                                </div>
                            </div>
                            <div className='chatbot-options'>
                                <div id='restaurant' style={{ display: "none" }} className={restaurant ? "" : "remove-class"}>
                                    <div id="restaurant" className="bottom-chatbot-food">
                                        <button className='humanities' id='humanities' onClick={handleHumanities} value="인문계 식당 메뉴 알려줘!">인문계 식당</button>
                                        <button className='nature' id='nature' onClick={handleNature} value="자연계 식당 메뉴 알려줘!">자연계 식당</button>
                                        <button className='staff' id='staff' onClick={handleStaff} value="교직원 식당 메뉴 알려줘!">교직원 식당</button>
                                    </div>
                                </div>
                                <div className='bottom-chatbot'>
                                    <button className='food' id='food' onClick={handleFood} value="오늘 학식은 뭐야?">오늘의 학식</button>
                                    <button className='weather' id='weather' onClick={handleWeather} value="현재 날씨 어때?">현재 날씨</button>
                                    <button className='subway' id='subway' onClick={handleSubway} value="지하철 현황 알려줘">지하철 현황</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <button className='btn-mini-chatbot' onClick={showChatbot}>
                <img className='img-mini-chatbot' src={chatbot} alt="" />
            </button>
        </div>
    )
}

export default ChatBot