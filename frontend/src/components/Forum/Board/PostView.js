import React, { useState, useEffect } from "react";
import axios from "axios";
import { useLocation, Link } from 'react-router-dom';
import ReactHtmlParser from "react-html-parser";
import "./postView.css"

const url = "http://54.180.29.69:8080";

const PostView = () => {
  const location = useLocation();
  const [postData, setPostData] = useState([]);
  let pathname = location.pathname;
  let [a, b, postId] = pathname.split('/');

  useEffect(() => {
    fetchData();
    console.log(location);
  }, [location]);

  const fetchData = async () => {
    const res = await axios.get(url + `/community/chats/${postId}`);
    console.log(res.data);
    const _postData = {
      id: res.data.id,
      title: res.data.title,
      content: ReactHtmlParser(res.data.content),
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
          <h2 className="chat-title">{postData.title}</h2>
          <label className="user-info">작성자 : {postData.username}</label>
          <div className="bo-line"></div>
          <label className="chat-content">{postData.content}</label>
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