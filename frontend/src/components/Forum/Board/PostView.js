import React from "react";
import { useState } from "react/cjs/react.production.min";
import { useEffect } from "react/cjs/react.production.min";
import axios from "axios";
import ReactHtmlParser from "react-html-parser";

const url = "http://54.180.29.69:8080";

const PostView = ({ postData }) => {
  //   const [postData, setPostData] = useState([]);
  //   useEffect(() => {
  //     fetchData();
  //   }, []);

  //   const fetchData = async () => {
  //     const res = await axios.get(url + `/community/chats`, { props });

  //     const _postData = await res.data.map((rowData) => ({
  //       id: rowData.id,
  //       title: rowData.title,
  //       content: ReactHtmlParser(rowData.content),
  //       hit: rowData.hit,
  //       like: rowData.like,
  //       username: rowData.username,
  //     }));
  //     setPostData(_postData);

  //     console.log(postData);
  //   };

  return (
    <>
      <h2>게시글 상세 정보</h2>
      <div className="post-view">
        {postData ? (
          <>
            <div>
              <label>제목</label>
              <label>{postData.title}</label>
            </div>
            <div>
              <label>작성자</label>
              <label>{postData.username}</label>
            </div>
            <div>
              <label>내용</label>
              <label>{postData.content}</label>
            </div>
          </>
        ) : (
          "해당 게시글을 찾을 수 없습니다."
        )}
      </div>
    </>
  );
};

export default PostView;
