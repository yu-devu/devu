import axios from 'axios';
import React, { useState, useEffect } from 'react';
import Select from 'react-select'
import { useNavigate } from 'react-router-dom';
import { CKEditor } from '@ckeditor/ckeditor5-react';
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import './write.css';
import { options } from '../data';
import ReactHtmlParser from 'html-react-parser';

const Write = () => {
  const navigate = useNavigate();
  const [tags, setTags] = useState([]); // Select에서 담은 tags
  const [postTags, setPostTags] = useState([]); // tags를 가공한 것 => axios.post할 때 쓸 수 있도록 한 것임.
  const [postContent, setPostContent] = useState({
    title: '',
    content: '',
  });
  const username = localStorage.getItem('username');

  useEffect(() => {
    console.log(postTags);
  }, [postTags]); // postTags의 동기처리를 위해 useEffect 사용함

  const handleTitle = (e) => {
    const { name, value } = e.target;
    setPostContent({
      ...postContent,
      [name]: value,
    });
  };

  const onChangeTags = (e) => {
    console.log('You\'ve selected:', e);
    setTags(e);
  };

  const organizeTags = () => {
    let array = [];
    for (let i = 0; i < tags.length; i++) {
      array.push(tags[i].value);
    }
    setPostTags(array);
  }

  const handleWrite = async () => {
    if (postContent.title !== '' && postContent.content !== '' && tags !== '') {
      const formData = new FormData();
      formData.append('title', postContent.title);
      formData.append('username', username);
      // formData.append('content', postContent.content);
      formData.append('content', ReactHtmlParser(postContent.content));
      formData.append('tags', postTags);

      await axios
        .post(process.env.REACT_APP_DB_HOST + `/community/study`, formData, {
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
    } else alert('글을 작성해주세요!');
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
            value={tags}
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
            // console.log(postContent);
          }}
          onBlur={(event, editor) => { }}
          onFocus={(event, editor) => { }}
        />
        <div className="bt_se">
          <button className="btn-post" onClick={() => { organizeTags(); handleWrite(); }}>
            글 작성
          </button>
        </div>
      </div>
    </div>
  );
};

export default Write;
