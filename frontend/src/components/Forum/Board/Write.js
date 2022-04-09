import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './write.css';

const Write = () => {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  const handleTitle = (e) => setTitle(e.target.value);
  const handleContent = (e) => setContent(e.target.value);
  const handleWrite = () => {
    if (title === '' || content === '') {
      alert('글을 작성해주세요!');
      return
    }
    console.log("title: ", title);
    console.log("content: ", content);
    alert('글이 성공적으로 등록되었습니다!');
    navigate(-1);
  }

  return (
    <div className="container-write">
      <div className="write-area">
        <h1>글을 작성하세요</h1>
        <div className="in_title">
          <textarea
            name="title"
            id="title"
            rows="1"
            cols="55"
            placeholder="제목"
            maxlength="100"
            required
            onChange={(e) => handleTitle(e)}
          ></textarea>
        </div>
        <div className="in_content">
          <textarea
            name="content"
            id="content"
            rows="1"
            cols="55"
            placeholder="내용"
            required
            onChange={(e) => handleContent(e)}
          ></textarea>
        </div>
        <div className="bt_se">
          <button className="btn-post" onClick={() => handleWrite()}>
            글 작성
          </button>
        </div>
      </div>
    </div >
  );
};

export default Write;
