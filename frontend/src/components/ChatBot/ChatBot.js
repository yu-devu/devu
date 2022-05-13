import React, { useState } from 'react'
import man from '../../img/man.png'
import './chatbot.css'
import remove from '../../img/remove.png'

const ChatBot = () => {
    let [modal, modalChange] = useState(true)

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

    const handleInput = () => {
        const botMessage = document.querySelector('#message1');
        const userMessage = document.querySelector('#message2');

        let food = ['학식|밥'];
        let words = new RegExp(food);
        if (words.test(document.querySelector('#input').value)) {
            botMessage.innerHTML = '입력 중...'
            setTimeout(() => {
                botMessage.innerHTML = "오늘 학식은 돈까스입니다!"
                document.querySelector('#input').value = "";
            }, 2000);
        }
        userMessage.innerHTML = document.querySelector('#input').value;
    }

    return (
        <div>
            <div id='chatbot' style={{ display: "none" }} className={modal ? "" : "remove-class"}>
                <div className='chatbot-all' onLoad={welcomeMsg}>
                    <div className='chatbot-wrapper'>
                        <div className='chatbot-content'>
                            <div className='top-chatbot'>
                                <div className='img-chatbot'>
                                    <img className='img-chatbot' src={man} alt="" />
                                </div>
                                <div className='right-chatbot'>
                                    <div className='name-chatbot'>DEVU 도우미</div>
                                </div>
                                <button className='btn-close' onClick={closeModal}><img className='img-remove' src={remove} alt="" /></button>
                            </div>
                            <div className='main-chatbot'>
                                <div className='main-chatbot-content'>
                                    <div className='chatbot-messages'>
                                        <div className='bot-message' id='message1'></div>
                                        <div className='human-message' id="message2"></div>
                                    </div>
                                </div>
                            </div>
                            <div className='bottom-chatbot'>
                                <div className='btm'>
                                    <input className='input-chatbot' type="text" id="input" placeholder="전송할 내용을 입력하세요!" />
                                    <button className='btn-chatbot' onClick={handleInput}>전송</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <button className='btn-mini-chatbot' onClick={showChatbot}></button>
        </div>
    )
}

export default ChatBot