import React from 'react';
import axios from 'axios';
import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar/Navbar';
import Register from './components/Register/Register';
import Modify from './components/Forum/Chats/Modify';
import ChangePassword from './components/Navbar/ChangePassword';
import Chats from './components/Forum/Chats/Chats';
import Studies from './components/Forum/Studies/Studies';
import StudiesView from './components/Forum/Studies/StudiesView';
import StudyWrite from './components/Forum/Studies/StudiesWrite';
import ChatBot from './components/ChatBot/ChatBot';
import RegisterOver from './components/Register/RegisterOver';
import Question from './components/Forum/Questions/Questions';
import MyPage from './components/MyPage/MyPage';
import GuestHome from './components/Home/GuestHome';
import UserHome from './components/Home/UserHome';
import QuestionsWrite from './components/Forum/Questions/QuestionsWrite';
import Jobs from './components/Jobs/Jobs';
import QuestionsView from './components/Forum/Questions/QuestionsView';
import ChatsWrite from './components/Forum/Chats/ChatsWrite';
import ChatsView from './components/Forum/Chats/ChatsView';

function App() {
  const JWT_EXPIRY_TIME = 30 * 60 * 1000; // 만료 시간 (30분)
  // const onSilentRefresh = async () => {
  //   await axios
  //     .post(process.env.REACT_APP_DB_HOST + '/silent-refresh')
  //     .then((response) => {
  //       console.log(response);
  //       if (response.headers['x-auth-access-token']) {
  //         localStorage.setItem('accessToken', response.headers['x-auth-access-token']);
  //       }
  //       setInterval(onSilentRefresh, JWT_EXPIRY_TIME - 60000); // accessToken 만료하기 1분 전에 로그인 연장
  //       console.log("Timeout-App.js");
  //     })
  //     .catch((res) => {
  //       console.log(res);
  //       alert(JSON.parse(res.request.response).error); // 이메일, 비밀번호 오류 출력
  //     });
  // };

  // if (performance.navigation.type === 1) { //새로고침하면 바로 로그인 연장(토큰 갱신)
  //   onSilentRefresh();
  // }

  return (
    <>
      <Router>
        <Navbar />
        <Routes>
          <Route path="/" exact element={<GuestHome />}></Route>
          <Route path="/main" exact element={<UserHome />}></Route>
          <Route path="/register" exact element={<Register />}></Route>
          <Route path="/registered" exact element={<RegisterOver />}></Route >
          <Route path="/chats" exact element={<Chats />}></Route>
          <Route path="/chats/write" exact element={<ChatsWrite />}></Route>
          <Route path="/chatsDetail/:no" exact element={<ChatsView />}></Route>
          <Route path="/questions" exact element={<Question />}></Route >
          <Route path="/questionsDetail/:no" exact element={<QuestionsView />}></Route>
          <Route path="/questions/write" exact element={<QuestionsWrite />}></Route>
          <Route path="/postDetail/:no/modify" exact element={<Modify />}></Route>
          <Route path="/change_password/*" element={<ChangePassword />}></Route>
          <Route path="/studies" exact element={<Studies />}></Route>
          <Route path="/studies/write" exact element={<StudyWrite />}></Route >
          <Route path="/studiesDetail/:no" exact element={<StudiesView />}></Route>
          <Route path="/chatbot" exact element={<ChatBot />}></Route >
          <Route path="/mypage" exact element={<MyPage />}></Route >
          <Route path="/jobs" exact element={<Jobs />}></Route >
        </Routes>
      </Router>
    </>
  );
}

export default App;
