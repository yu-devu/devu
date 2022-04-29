import React, { useState, useEffect } from "react";
import axios from "axios";
import { useLocation, Link } from 'react-router-dom';
import ReactHtmlParser from "react-html-parser";
import "./postView.css"

const PostView = () => {
  const location = useLocation();
  const [postData, setPostData] = useState([]);
  let pathname = location.pathname;
  let [a, b, postId] = pathname.split('/');
  // useLocation으로 pathname을 추출한 후, '/'를 기준으로 parameter를 분리함

  useEffect(() => {
    fetchData();
    console.log(location);
  }, [location]);

  const fetchData = async () => {
    const res = await axios.get(`/community/chats/${postId}`);
    console.log(res.data);
    const _postData = {
      id: res.data.id,
      title: res.data.title,
      content: res.data.content,
      hit: res.data.hit,
      like: res.data.like,
      username: res.data.username,
    };
    setPostData(_postData);
    console.log(postData);
  };

  return (
    <div className="post-view">
      {postData ? (
        <div className="container-read">
          <h2 className="chat-detail-title">{postData.title}</h2>
          <div className="chat-detail-head">
            <label className="chat-post-owner">작성자 : {postData.username}</label>
            <label className="chat-detail-hit">조회수 : {postData.hit}</label>
          </div>
          <label className="chat-detail-content">{postData.content}</label>
          <div class="read-btns">
            <Link to="modify">
              <a className="btn-modify">
                수정하기
              </a>
            </Link>
            <a className="btn-delete">
              삭제하기
            </a>
          </div>
        </div>) : (
        "해당 게시글을 찾을 수 없습니다."
      )}
    </div>
  );
};

export default PostView;