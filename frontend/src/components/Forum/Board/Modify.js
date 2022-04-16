import axios from "axios";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { CKEditor } from "@ckeditor/ckeditor5-react";
import ClassicEditor from "@ckeditor/ckeditor5-build-classic";
import "./modify.css";

const url = "http://54.180.29.69:8080";

const Modify = () => {
  const navigate = useNavigate();
  const [postContent, setPostContent] = useState({
    title: "",
    content: "",
  });

  const handleTitle = (e) => {
    const { name, value } = e.target;
    setPostContent({
      ...postContent,
      [name]: value,
    });
  };
  const handleModify = async () => {
    if (postContent === "") {
      alert("글을 작성해주세요!");
      return;
    }
    const data = {
      title: postContent.title,
      content: postContent.content,
    };
    await axios
      .post(url + `/community/chat`, JSON.stringify(data), {
        headers: {
          "Content-Type": "application/json",
          Authorization: `${localStorage.getItem("accessToken")}`,
        },
      })
      .then(() => {
        alert("글이 성공적으로 수정되었습니다!");
        navigate(-1);
      })
      .catch(() => {
        alert("글 수정 실패..");
      });
  };

  return (
    <div className="container-modify">
      <div className="modify-area">
        <h1>수정할 내용을 적으세요</h1>
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
          onBlur={(event, editor) => {}}
          onFocus={(event, editor) => {}}
        />
        <div className="bt_se">
          <button className="btn-modify-check" onClick={() => handleModify()}>
            수정하기
          </button>
        </div>
      </div>
    </div>
  );
};

export default Modify;
