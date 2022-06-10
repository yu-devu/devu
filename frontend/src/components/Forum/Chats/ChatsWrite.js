import axios from 'axios';
import React, { useState, useEffect } from 'react';
import Select from 'react-select';
import { useNavigate } from 'react-router-dom';
import { CKEditor } from '@ckeditor/ckeditor5-react';
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import './chatsWrite.css';
import { options } from '../data';
import Submenu from '../Submenu';
import FooterGray from '../../Home/FooterGray';

const ChatsWrite = () => {
  const navigate = useNavigate();
  const [postContent, setPostContent] = useState({
    title: '',
    content: '',
  });
  const username = localStorage.getItem('username');

  const handleTitle = (e) => {
    const { name, value } = e.target;
    setPostContent({
      ...postContent,
      [name]: value,
    });
  };

  const handleWrite = async () => {
    if (postContent.title !== '' && postContent.content !== '') {
      const formData = new FormData();
      formData.append('title', postContent.title);
      formData.append('username', username);
      formData.append('content', postContent.content);

      await axios
        .post(process.env.REACT_APP_DB_HOST + `/community/chat`, formData, {
          headers: {
            'Content-Type': 'application/json',
            Authorization: `${localStorage.getItem('accessToken')}`,
            // 'X-AUTH-ACCESS-TOKEN': `${localStorage.getItem('accessToken')}`,
          },
        })
        .then(() => {
          alert('글이 성공적으로 등록되었습니다!');
          navigate(-1);
        })
        .catch(() => alert('글 등록 실패..'));
    } else {
      alert('글을 작성해주세요!');
    }
  };

  return (
    <div>
      <Submenu />
      <div className="container-chats-write">
        <div className="write-area">
          <div className="in-title">
            <h8 className="in-title-text">제목</h8>
            <textarea
              name="title"
              id="title"
              rows="1"
              cols="55"
              placeholder="제목을 입력해주세요"
              maxLength="100"
              required
              onChange={(e) => handleTitle(e)}
            ></textarea>
          </div>
          <CKEditor
            editor={ClassicEditor}
            config={{
              placeholder: '- 자유롭게 수다를 떨어보세요!',
            }}
            data=""
            onChange={(event, editor) => {
              const data = editor.getData();
              setPostContent({
                ...postContent,
                content: data
                  .replace('<p>', '')
                  .replace('</p>', '')
                  .replace('</strong>', '')
                  .replace('<strong>', ''),
              });
            }}
            onBlur={(event, editor) => {}}
            onFocus={(event, editor) => {}}
          />
          <div className="bt-se">
            <button
              className="btn-cancel"
              onClick={() => {
                navigate(-1);
              }}
            >
              취소
            </button>
            <button
              className="btn-post"
              onClick={() => {
                handleWrite();
              }}
            >
              저장
            </button>
          </div>
        </div>
      </div>
      <FooterGray />
    </div>
  );
};

export default ChatsWrite;
