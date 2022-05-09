import axios from 'axios';
import React, { useState } from 'react';
import Select from 'react-select'
import { useNavigate } from 'react-router-dom';
import { CKEditor } from '@ckeditor/ckeditor5-react';
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import './write.css';
import { options } from '../data';
import ReactHtmlParser from 'html-react-parser';

const Write = () => {
  const navigate = useNavigate();
  const [tags, setTags] = useState([]);
  const [postContent, setPostContent] = useState({
    title: '',
    content: '',
    tags: '',
  });
  const username = localStorage.getItem('username');

  const handleTitle = (e) => {
    const { name, value } = e.target;
    setPostContent({
      ...postContent,
      [name]: value,
    });
  };

  const onChangeTags = (e) => {
    setTags(e.target.value);
    console.log(tags);
  };

  const handleWrite = async () => {
    if (postContent === '') {
      alert('글을 작성해주세요!');
      return;
    }

    const formData = new FormData();
    formData.append('title', postContent.title);
    formData.append('username', username);
    // formData.append('content', postContent.content);
    formData.append('content', ReactHtmlParser(postContent.content));
    formData.append('tags', postContent.tags);
    await axios
      .post(process.env.REACT_APP_DB_HOST + `/community/chat`, formData, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `${localStorage.getItem('accessToken')}`,
          'X-AUTH-ACCESS-TOKEN': `${localStorage.getItem('accessToken')}`,
        },
      })
      .then(() => {
        alert('글이 성공적으로 등록되었습니다!');
        navigate(-1);
      })
      .catch(() => {
        alert('글 등록 실패..');
      });
  };

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
        <div>
          <Select
            isMulti
            options={options}
            // value={tags}
            name="tags"
            placeholder="태그를 선택해주세요!"
            onChange={(e) => onChangeTags(e)}
          />
        </div>
        <CKEditor
          editor={ClassicEditor}
          data=""
          onChange={(event, editor) => {
            const data = editor.getData();
            setPostContent({
              ...postContent,
              content: data,
            });
          }}
          onBlur={(event, editor) => { }}
          onFocus={(event, editor) => { }}
        />
        <div className="bt_se">
          <button className="btn-post" onClick={() => handleWrite()}>
            글 작성
          </button>
        </div>
      </div>
    </div>
  );
};

export default Write;
