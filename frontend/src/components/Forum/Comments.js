import React, { useState, useEffect } from 'react';
import { useLocation, Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import ab from '../../img/a.png';
import more from '../../img/more.png';

const Comments = (props) => {
  let now = new Date();
  let hours = now.getHours() - 9;
  let minutes = now.getMinutes();
  let seconds = now.getSeconds();
  let year = now.getFullYear();
  let month = now.getMonth() + 1;
  let date = now.getDate();

  const username = localStorage.getItem('username');
  const navigate = useNavigate();
  const location = useLocation();
  let pathname = location.pathname;
  let [a, b, postId] = pathname.split('/');

  const [modifycomment, setModifyComment] = useState('');
  const [showDropdownContent, setShowDropdownContent] = useState(0);
  const [clickedComment, setClickedComment] = useState(0);
  const [showModifyContent, setShowModifyContent] = useState(0);
  const [comment, setComment] = useState('');
  const [recomment, setRecomment] = useState('');

  const onChangeComment = (e) => {
    setComment(e.target.value);
  };

  const handleComment = async () => {
    if (comment !== '') {
      const data = {
        username: username,
        postId: postId,
        contents: comment,
      };
      await axios
        .post(
          process.env.REACT_APP_DB_HOST + `/api/comments`,
          JSON.stringify(data),
          {
            headers: {
              'Content-Type': 'application/json',
            },
          }
        )
        .then(() => navigate(0))
        .catch((e) => console.log(e));
    } else alert('댓글을 작성해주세요!');
  };

  const handleCommentDelete = async (id) => {
    if (window.confirm('정말 삭제하시겠습니까?')) {
      await axios
        .delete(process.env.REACT_APP_DB_HOST + `/api/comments/${id}`)
        .then(() => navigate(0))
        .catch((e) => console.log(e));
    } else alert('취소하였습니다!');
  };

  const handleRecommentDelete = async (id) => {
    if (window.confirm('정말 삭제하시겠습니까?')) {
      await axios
        .delete(process.env.REACT_APP_DB_HOST + `/api/reComments/${id}`)
        .then(() => navigate(0))
        .catch((e) => console.log(e));
    } else alert('취소하였습니다!');
  };

  const onChangeRecomment = (e) => {
    setRecomment(e.target.value);
  };

  const onChangeModifyComment = (e) => {
    setModifyComment(e.target.value);
  };

  const handleCommentModify = async (id) => {
    if (modifycomment !== '') {
      const data = {
        contents: modifycomment,
      };
      await axios
        .patch(
          process.env.REACT_APP_DB_HOST + `/api/comments/${id}`,
          JSON.stringify(data),
          {
            headers: {
              'Content-Type': 'application/json',
              Authorization: `${localStorage.getItem('accessToken')}`,
            },
          }
        )
        .then(() => navigate(0))
        .catch((e) => console.log(e));
    } else alert('댓글을 작성해주세요!');
  };

  const handleRecomment = async (commentId, commentUserName) => {
    if (recomment !== '') {
      const data = {
        username: username,
        postId: postId,
        contents: recomment,
        parent: commentUserName,
        group: commentId,
      };
      await axios
        .post(
          process.env.REACT_APP_DB_HOST + `/api/reComments`,
          JSON.stringify(data),
          {
            headers: {
              'Content-Type': 'application/json',
            },
          }
        )
        .then(() => navigate(0))
        .catch((e) => console.log(e));
    } else alert('대댓글을 작성해주세요!');
  };

  const handleKeyPressComment = (e) => {
    if (e.key === 'Enter') {
      handleComment();
    }
  };

  const handleKeyPressRecomment = (e, commentId, username) => {
    if (e.key === 'Enter') {
      handleRecomment(commentId, username);
    }
  };

  return (
    <div>
      <div className="studies-write-comments">
        <input
          className="comment"
          id="comment"
          name="comment"
          value={comment}
          onChange={(e) => onChangeComment(e)}
          onKeyPress={handleKeyPressComment}
          placeholder="댓글을 입력해주세요."
        />
        <button
          className="btn-comment"
          onClick={() => {
            handleComment();
          }}
        >
          댓글달기
        </button>
      </div>
      {props.comments ? (
        <div className="studies-comments-all">
          <div className="number-comments">
            <h6 className="number-comments-text">
              {props.comments.length}개의 답글
            </h6>
          </div>
          <div div className="studies-comments">
            {props.comments &&
              props.comments.map((comment) =>
                comment.commentId === comment.group ? (
                  <div className="container-comments">
                    {comment.deleted === true ? (
                      <div>
                        <div>삭제된 댓글입니다</div>
                        {props.comments &&
                          props.comments.map((recomment) =>
                            comment.group === recomment.group &&
                            comment.group !== recomment.commentId ? (
                              <div className="container-comments">
                                <div className="comment-detail">
                                  <div className="comments-top">
                                    <div>
                                      <img
                                        className="comment-photo"
                                        src={ab}
                                        alt=""
                                      />
                                    </div>
                                    <div className="comment-top">
                                      <div className="comment-owner">
                                        {recomment.username}
                                      </div>

                                      {recomment.username === username &&
                                      recomment.commentId !==
                                        showModifyContent ? (
                                        <button className="btn-more">
                                          <img
                                            className="img-more"
                                            alt=""
                                            src={more}
                                            onClick={() => {
                                              console.log(recomment.commentId);
                                              if (
                                                showDropdownContent ===
                                                recomment.commentId
                                              )
                                                setShowDropdownContent(0);
                                              else
                                                setShowDropdownContent(
                                                  recomment.commentId
                                                );
                                            }}
                                          />

                                          {recomment.commentId ===
                                          showDropdownContent ? (
                                            recomment.commentId ===
                                            recomment.group ? (
                                              <div>
                                                <button
                                                  onClick={() => {
                                                    setShowModifyContent(
                                                      recomment.commentId
                                                    );
                                                    setShowDropdownContent(0);
                                                  }}
                                                >
                                                  수정
                                                </button>
                                                <button
                                                  onClick={() => {
                                                    handleCommentDelete(
                                                      recomment.commentId
                                                    );
                                                  }}
                                                >
                                                  삭제
                                                </button>
                                              </div>
                                            ) : (
                                              <div>
                                                <button
                                                  onClick={() => {
                                                    setShowModifyContent(
                                                      recomment.commentId
                                                    );
                                                    setShowDropdownContent(0);
                                                  }}
                                                >
                                                  수정
                                                </button>
                                                <button
                                                  onClick={() => {
                                                    handleRecommentDelete(
                                                      recomment.commentId
                                                    );
                                                  }}
                                                >
                                                  삭제
                                                </button>
                                              </div>
                                            )
                                          ) : null}
                                        </button>
                                      ) : null}
                                    </div>
                                  </div>
                                  {recomment.commentId === showModifyContent ? (
                                    <div className="questions-write-comments">
                                      <input
                                        id="comment"
                                        name="comment"
                                        defaultValue={recomment.contents}
                                        onChange={(e) =>
                                          onChangeModifyComment(e)
                                        }
                                      />
                                      <button
                                        className="btn-comment"
                                        onClick={() => {
                                          handleCommentModify(
                                            recomment.commentId
                                          );
                                        }}
                                      >
                                        수정하기
                                      </button>
                                      <button
                                        className="btn-comment"
                                        onClick={() => {
                                          setShowModifyContent(0);
                                        }}
                                      >
                                        취소
                                      </button>
                                    </div>
                                  ) : (
                                    <div className="comment-content">
                                      {recomment.contents}
                                    </div>
                                  )}
                                  <div className="comment-date">
                                    {recomment.createAt.slice(0, 4) == year
                                      ? recomment.createAt.slice(5, 7) ==
                                          month &&
                                        recomment.createAt.slice(8, 10) == date
                                        ? recomment.createAt.slice(11, 13) ==
                                          hours
                                          ? recomment.createAt.slice(14, 16) ==
                                            minutes
                                            ? seconds -
                                              recomment.createAt.slice(17, 19) +
                                              '초 전'
                                            : minutes -
                                                recomment.createAt.slice(
                                                  14,
                                                  16
                                                ) ==
                                                1 &&
                                              seconds <
                                                recomment.createAt.slice(17, 19)
                                            ? 60 -
                                              recomment.createAt.slice(17, 19) +
                                              seconds +
                                              '초 전'
                                            : minutes -
                                              recomment.createAt.slice(14, 16) +
                                              '분 전'
                                          : hours -
                                            recomment.createAt.slice(11, 13) +
                                            '시간 전'
                                        : recomment.createAt.slice(5, 7) +
                                          '.' +
                                          recomment.createAt.slice(8, 10)
                                      : recomment.createAt.slice(2, 4) +
                                        '.' +
                                        recomment.createAt.slice(5, 7) +
                                        '.' +
                                        recomment.createAt.slice(8, 10)}
                                  </div>
                                </div>
                              </div>
                            ) : null
                          )}
                      </div>
                    ) : (
                      <div className="comment-detail">
                        <div className="comments-top">
                          <div>
                            <img className="comment-photo" src={ab} alt="" />
                          </div>
                          <div className="comment-top">
                            <div className="comment-owner">
                              {comment.username}
                            </div>
                            {comment.commentId === comment.group ? (
                              <div
                                onClick={() => {
                                  console.log(
                                    comment.commentId +
                                      ', ' +
                                      comment.username +
                                      ', ' +
                                      comment.group
                                  );
                                  if (clickedComment === comment.commentId)
                                    setClickedComment(0);
                                  else setClickedComment(comment.commentId);
                                }}
                              >
                                대댓글
                              </div>
                            ) : null}

                            {comment.username === username &&
                            comment.commentId !== showModifyContent ? (
                              <button className="btn-more">
                                <img
                                  className="img-more"
                                  alt=""
                                  src={more}
                                  onClick={() => {
                                    console.log(comment.commentId);
                                    if (
                                      showDropdownContent === comment.commentId
                                    )
                                      setShowDropdownContent(0);
                                    else
                                      setShowDropdownContent(comment.commentId);
                                  }}
                                />

                                {comment.commentId === showDropdownContent ? (
                                  comment.commentId === comment.group ? (
                                    <div>
                                      <button
                                        onClick={() => {
                                          setShowModifyContent(
                                            comment.commentId
                                          );
                                          setShowDropdownContent(0);
                                        }}
                                      >
                                        수정
                                      </button>
                                      <button
                                        onClick={() => {
                                          handleCommentDelete(
                                            comment.commentId
                                          );
                                        }}
                                      >
                                        삭제
                                      </button>
                                    </div>
                                  ) : (
                                    <div>
                                      <button
                                        onClick={() => {
                                          setShowModifyContent(
                                            comment.commentId
                                          );
                                          setShowDropdownContent(0);
                                        }}
                                      >
                                        수정
                                      </button>
                                      <button
                                        onClick={() => {
                                          handleRecommentDelete(
                                            comment.commentId
                                          );
                                        }}
                                      >
                                        삭제
                                      </button>
                                    </div>
                                  )
                                ) : null}
                              </button>
                            ) : null}
                          </div>
                        </div>
                        {comment.commentId === showModifyContent ? (
                          <div className="questions-write-comments">
                            <input
                              id="comment"
                              name="comment"
                              defaultValue={comment.contents}
                              onChange={(e) => onChangeModifyComment(e)}
                            />
                            <button
                              className="btn-comment"
                              onClick={() => {
                                handleCommentModify(comment.commentId);
                              }}
                            >
                              수정하기
                            </button>
                            <button
                              className="btn-comment"
                              onClick={() => {
                                setShowModifyContent(0);
                              }}
                            >
                              취소
                            </button>
                          </div>
                        ) : (
                          <div className="comment-content">
                            {comment.contents}
                          </div>
                        )}
                        <div className="comment-date">
                          {comment.createAt.slice(0, 4) == year
                            ? comment.createAt.slice(5, 7) == month &&
                              comment.createAt.slice(8, 10) == date
                              ? comment.createAt.slice(11, 13) == hours
                                ? comment.createAt.slice(14, 16) == minutes
                                  ? seconds -
                                    comment.createAt.slice(17, 19) +
                                    '초 전'
                                  : minutes - comment.createAt.slice(14, 16) ==
                                      1 &&
                                    seconds < comment.createAt.slice(17, 19)
                                  ? 60 -
                                    comment.createAt.slice(17, 19) +
                                    seconds +
                                    '초 전'
                                  : minutes -
                                    comment.createAt.slice(14, 16) +
                                    '분 전'
                                : hours -
                                  comment.createAt.slice(11, 13) +
                                  '시간 전'
                              : comment.createAt.slice(5, 7) +
                                '.' +
                                comment.createAt.slice(8, 10)
                            : comment.createAt.slice(2, 4) +
                              '.' +
                              comment.createAt.slice(5, 7) +
                              '.' +
                              comment.createAt.slice(8, 10)}
                        </div>
                        {comment.commentId === clickedComment ? (
                          <div style={{ backgroundColor: '#F2F2F2' }}>
                            <input
                              className="comment"
                              id="recomment"
                              name="recomment"
                              value={recomment}
                              onChange={(e) => onChangeRecomment(e)}
                              onKeyPress={(e) =>
                                handleKeyPressRecomment(
                                  e,
                                  comment.commentId,
                                  comment.username
                                )
                              }
                              placeholder="대댓글을 입력해주세요."
                            />
                            <button
                              className="btn-comment"
                              onClick={() => {
                                handleRecomment(
                                  comment.commentId,
                                  comment.username
                                );
                              }}
                            >
                              댓글달기
                            </button>
                          </div>
                        ) : null}

                        {props.comments &&
                          props.comments.map((recomment) =>
                            comment.group === recomment.group &&
                            comment.group !== recomment.commentId ? (
                              <div className="container-comments">
                                <div className="comment-detail">
                                  <div className="comments-top">
                                    <div>
                                      <img
                                        className="comment-photo"
                                        src={ab}
                                        alt=""
                                      />
                                    </div>
                                    <div className="comment-top">
                                      <div className="comment-owner">
                                        {recomment.username}
                                      </div>

                                      {recomment.username === username &&
                                      recomment.commentId !==
                                        showModifyContent ? (
                                        <button className="btn-more">
                                          <img
                                            className="img-more"
                                            alt=""
                                            src={more}
                                            onClick={() => {
                                              console.log(recomment.commentId);
                                              if (
                                                showDropdownContent ===
                                                recomment.commentId
                                              )
                                                setShowDropdownContent(0);
                                              else
                                                setShowDropdownContent(
                                                  recomment.commentId
                                                );
                                            }}
                                          />

                                          {recomment.commentId ===
                                          showDropdownContent ? (
                                            recomment.commentId ===
                                            recomment.group ? (
                                              <div>
                                                <button
                                                  onClick={() => {
                                                    setShowModifyContent(
                                                      recomment.commentId
                                                    );
                                                    setShowDropdownContent(0);
                                                  }}
                                                >
                                                  수정
                                                </button>
                                                <button
                                                  onClick={() => {
                                                    handleCommentDelete(
                                                      recomment.commentId
                                                    );
                                                  }}
                                                >
                                                  삭제
                                                </button>
                                              </div>
                                            ) : (
                                              <div>
                                                <button
                                                  onClick={() => {
                                                    setShowModifyContent(
                                                      recomment.commentId
                                                    );
                                                    setShowDropdownContent(0);
                                                  }}
                                                >
                                                  수정
                                                </button>
                                                <button
                                                  onClick={() => {
                                                    handleRecommentDelete(
                                                      recomment.commentId
                                                    );
                                                  }}
                                                >
                                                  삭제
                                                </button>
                                              </div>
                                            )
                                          ) : null}
                                        </button>
                                      ) : null}
                                    </div>
                                  </div>
                                  {recomment.commentId === showModifyContent ? (
                                    <div className="questions-write-comments">
                                      <input
                                        id="comment"
                                        name="comment"
                                        defaultValue={recomment.contents}
                                        onChange={(e) =>
                                          onChangeModifyComment(e)
                                        }
                                      />
                                      <button
                                        className="btn-comment"
                                        onClick={() => {
                                          handleCommentModify(
                                            recomment.commentId
                                          );
                                        }}
                                      >
                                        수정하기
                                      </button>
                                      <button
                                        className="btn-comment"
                                        onClick={() => {
                                          setShowModifyContent(0);
                                        }}
                                      >
                                        취소
                                      </button>
                                    </div>
                                  ) : (
                                    <div className="comment-content">
                                      {recomment.contents}
                                    </div>
                                  )}
                                  <div className="comment-date">
                                    {recomment.createAt.slice(0, 4) == year
                                      ? recomment.createAt.slice(5, 7) ==
                                          month &&
                                        recomment.createAt.slice(8, 10) == date
                                        ? recomment.createAt.slice(11, 13) ==
                                          hours
                                          ? recomment.createAt.slice(14, 16) ==
                                            minutes
                                            ? seconds -
                                              recomment.createAt.slice(17, 19) +
                                              '초 전'
                                            : minutes -
                                                recomment.createAt.slice(
                                                  14,
                                                  16
                                                ) ==
                                                1 &&
                                              seconds <
                                                recomment.createAt.slice(17, 19)
                                            ? 60 -
                                              recomment.createAt.slice(17, 19) +
                                              seconds +
                                              '초 전'
                                            : minutes -
                                              recomment.createAt.slice(14, 16) +
                                              '분 전'
                                          : hours -
                                            recomment.createAt.slice(11, 13) +
                                            '시간 전'
                                        : recomment.createAt.slice(5, 7) +
                                          '.' +
                                          recomment.createAt.slice(8, 10)
                                      : recomment.createAt.slice(2, 4) +
                                        '.' +
                                        recomment.createAt.slice(5, 7) +
                                        '.' +
                                        recomment.createAt.slice(8, 10)}
                                  </div>
                                </div>
                              </div>
                            ) : null
                          )}
                      </div>
                    )}
                  </div>
                ) : null
              )}
          </div>
        </div>
      ) : null}
    </div>
  );
};
export default Comments;
