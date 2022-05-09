import React, { useState, useEffect } from "react";
import axios from "axios";
import { useLocation, Link, useNavigate } from "react-router-dom";
import "./postView.css";


const PostView = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [postData, setPostData] = useState([]);
  const [isLike, setLike] = useState(false);
  const username = localStorage.getItem("username");
  // const [comment, setComment] = useState('');
  // const onChangeComment = (e) => { setComment(e.target.value) }

  let pathname = location.pathname;
  let [a, b, postId] = pathname.split("/");
  // useLocation으로 pathname을 추출한 후, '/'를 기준으로 parameter를 분리함

  useEffect(() => {
    fetchData();
    // console.log(location);
    // console.log(isLike);
  }, [location, isLike]);

  const fetchData = async () => {
    const res = await axios.get(
      process.env.REACT_APP_DB_HOST + `/community/chats/${postId}`
    );
    console.log(res.data);
    const _postData = {
      id: res.data.id,
      title: res.data.title,
      content: res.data.content,
      hit: res.data.hit,
      like: res.data.like,
      username: res.data.username,
      // comment: res.data.comment,
    };
    setPostData(_postData);
    console.log(JSON.stringify(postData.content));
  };

  const handleLike = async () => {
    const data = {
      username: username,
      postId: postData.id,
    };
    await axios.post(process.env.REACT_APP_DB_HOST + `/api/like`, JSON.stringify(data), {
      headers: {
        'Content-Type': 'application/json',
      }
    }).then((res) => {
      console.log("res.data", res.data);
      if (res.data.liked) setLike(true);
      else setLike(false);
    })
      .catch((res) => { console.log(res); });
  };


  const handleDelete = async () => {
    await axios.delete(process.env.REACT_APP_DB_HOST + `/community/chat/${postId}`)
      .then(() => {
        console.log("삭제 성공!");
        navigate(-1);
      })
      .catch((res) => console.log(res));
  };

  // const handleComment = async () => {
  //   const commentData = {
  //     userId: userId,
  //     postId: postId,
  //     contents: comment,
  //     parent: parent,
  //     group: group,
  //   };

  // }

  return (
    <div className="post-view">
      {postData ? (
        <div>
          <div className="container-read">
            <h2 className="chat-detail-title">{postData.title}</h2>
            <div className="chat-detail-head">
              {
                isLike
                  ? (<button onClick={() => { handleLike(); }}>♥︎</button>)
                  : (<button onClick={() => { handleLike(); }}>♡</button>)
              }
              <label className="chat-post-owner">
                작성자 : {postData.username}
              </label>
              <label className="chat-detail-hit">조회수 : {postData.hit}</label>
              <label className="chat-detail-hit">좋아요 : {postData.like}</label>
              {/* 좋아요 개수는 추후에 삭제해도 됨 */}
            </div>
            <label className="chat-detail-content">{postData.content}</label>
            <div class="read-btns">
              <Link to="modify">
                <a className="btn-modify">수정하기</a>
              </Link>
              <a className="btn-delete" onClick={() => handleDelete()}>삭제하기</a>
            </div>
          </div>
          {/* <h1>댓글</h1>
          <p>{postData.comment}</p>
          <input
            id="comment"
            name="comment"
            value={comment}
            onChange={(e) => onChangeComment(e)}
            placeholder="댓글 작성"
          />
          <button onClick={() => handleComment()}>작성</button> */}
        </div>
      ) : (
        "해당 게시글을 찾을 수 없습니다."
      )
      }
    </div >
  );
};

export default PostView;
