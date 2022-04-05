import React from 'react';
import './write.css';

const Write = () => {
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
          ></textarea>
        </div>
        <div className="bt_se">
          <button className="btn-post" type="submit">
            글 작성
          </button>
        </div>
      </div>
    </div>
  );
};

export default Write;
