import React, { useEffect, useState } from 'react'
import axios from 'axios'
import './myPage.css'
import a from '../../img/a.png'
import FooterGray from '../Home/FooterGray'

const MyPage = () => {
    useEffect(() => {
        fetchPostData();
        fetchLikeData();
    }, [])

    let now = new Date();
    let hours = now.getHours() - 9;
    let minutes = now.getMinutes();
    let seconds = now.getSeconds();
    let year = now.getFullYear();
    let month = now.getMonth() + 1;
    let date = now.getDate();

    const [postData, setPostData] = useState([]);
    const [likeData, setLikeData] = useState([]);
    const [postDataSize, setPostDataSize] = useState(0);
    // const [lastIdx, setLastIdx] = useState(0);
    const [isPost, setIsPost] = useState(true); // 마이페이지 처음 렌더링 했을 때 작성한 글 먼저 보이도록 설정
    const [isStudy, setIsStudy] = useState(false); // 마이페이지 처음 렌더링 했을 때 작성한 글 - 스터디구인 먼저 보이도록 설정
    const [isQuestion, setIsQuestion] = useState(false);
    const [isChat, setIsChat] = useState(false);
    const username = localStorage.getItem('username');

    function filterStudy(element) {
        if (element.studyStatus !== null) {
            return true;
        }
    }

    function filterQuestion(element) {
        if (element.questionStatus !== null) {
            return true;
        }
    }


    const fetchPostData = async () => {
        await axios.get(
            process.env.REACT_APP_DB_HOST + `/api/myPosts`)
            .then((res) => {
                console.log(res);
                const _postData = res.data.map(
                    (rowData) => (
                        {
                            id: rowData.id,
                            title: rowData.title,
                            content: rowData.content,
                            hit: rowData.hit,
                            like: rowData.like,
                            username: rowData.username,
                            postYear: rowData.createAt.substr(0, 4),
                            postMonth: rowData.createAt.substr(5, 2),
                            postDay: rowData.createAt.substr(8, 2),
                            postHour: rowData.createAt.substr(11, 2),
                            postMinute: rowData.createAt.substr(14, 2),
                            postSecond: rowData.createAt.substr(17, 2),
                            date: rowData.createAt.substr(0, 10),
                            time: rowData.createAt.substr(11, 8),
                            tags: rowData.tags,
                            studyStatus: rowData.studyStatus,
                            questionStatus: rowData.questionStatus,
                            commentsSize: rowData.commentsSize,
                        }
                    )
                );
                setPostData(_postData);
                console.log(_postData);

                //StduyFilter
                function filterStudy(element) {
                    if (element.studyStatus !== null) {
                        return true;
                    }
                }
                const _isStudy = _postData.filter(filterStudy);
                console.log("isStudy", _isStudy);

                //QuestionFilter
                function filterQuestion(element) {
                    if (element.questionStatus !== null) {
                        return true;
                    }
                }
                const _isQuestion = _postData.filter(filterQuestion);
                console.log("isQuestion", _isQuestion);

                //ChatFilter
                function filterChat(element) {
                    if (element.questionStatus === null && element.studyStatus === null) {
                        return true;
                    }
                }
                const _isChat = _postData.filter(filterChat);
                console.log("isChat", _isChat);

            })
            .catch((err) => console.log(err));


    };

    const fetchLikeData = async () => {
        await axios.get(
            process.env.REACT_APP_DB_HOST + `/api/myLikes`)
            .then((res) => {
                console.log(res);
                const _likeData = res.data.map(
                    (rowData) => (
                        // setLastIdx(lastIdx + 1),
                        {
                            id: rowData.id,
                            title: rowData.title,
                            content: rowData.content,
                            hit: rowData.hit,
                            like: rowData.like,
                            username: rowData.username,
                            postYear: rowData.createAt.substr(0, 4),
                            postMonth: rowData.createAt.substr(5, 2),
                            postDay: rowData.createAt.substr(8, 2),
                            postHour: rowData.createAt.substr(11, 2),
                            postMinute: rowData.createAt.substr(14, 2),
                            postSecond: rowData.createAt.substr(17, 2),
                            date: rowData.createAt.substr(0, 10),
                            time: rowData.createAt.substr(11, 8),
                            tags: rowData.tags,
                            studyStatus: rowData.studyStatus,
                            questionStatus: rowData.questionStatus,
                            commentsSize: rowData.commentsSize,
                        }
                    )
                );
                setLikeData(_likeData);
            })
            .catch((err) => console.log(err));
    }

    return (
        <div>
            <div className='mypage-top'>
                <div className='profile-mypage'>
                    <img className='img-mypage' src={a} alt="" />
                </div>
                <div className='text-mypage'>
                    <h1>{username}님 안녕하세요</h1>
                    {/* <h2>중국언어문화학과 19학번</h2> */}
                </div>
                <button className='btn-change-info'>정보변경</button>
            </div>
            <div className='tag-mypage'>
                <button className='btn-mypage-tag' onClick={() => { setIsPost(true) }}>작성한글</button>
                <button className='btn-mypage-tag' onClick={() => { setIsPost(false) }}>좋아요</button>
            </div>
            <div className='submenu-mypage'>
                <button className='btn-mypage-submenu' onClick={() => { setIsStudy(true) }}>스터디구인란</button>
                <button className='btn-mypage-submenu' onClick={() => { setIsQuestion(true) }}>Q&A</button>
                <button className='btn-mypage-submenu' onClick={() => { setIsChat(true) }}>자유게시판</button>
            </div>
            <hr className="line-mypage" />
            {/* <div className='bottom-mypage'>
                <div className='cate-mypage'>
                    <h2 className='text-cate'>제목</h2>
                    <h2 className='text-cate'>게시일자</h2>
                    <h2 className='text-cate'>댓글</h2>
                    <h2 className='text-cate'>상태</h2>
                </div>
            </div> */}
            <table className='bottom-mypage'>
                <tr className='cate-mypage'>
                    <th width="300">제목</th>
                    <th width="150">게시일자</th>
                    <th width="150">댓글</th>
                    <th width="150">상태</th>
                </tr>
                {isPost ? ( // 작성한 글 / 좋아요한 글 구분

                    postData.slice(0, postData.length).map((post) => (
                        <tr className='cate-mypage-detail'>
                            <td width="300">{post.title}</td>
                            <td width="150">{post.postYear == year
                                ? post.postMonth == month && post.postDay == date
                                    ? post.postHour == hours
                                        ? post.postMinute == minutes
                                            ? seconds - post.postSecond + '초 전'
                                            : minutes - post.postMinute == 1 &&
                                                seconds < post.postSecond
                                                ? 60 - post.postSecond + seconds + '초 전'
                                                : minutes - post.postMinute + '분 전'
                                        : hours - post.postHour + '시간 전'
                                    : post.postMonth + '.' + post.postDay
                                : post.postYear.slice(2, 4) +
                                '.' +
                                post.postMonth +
                                '.' +
                                post.postDay}</td>
                            <td width="150">{post.commentsSize}</td>
                            <td width="150">{post.studyStatus}</td>
                        </tr>
                    ))) : (
                    likeData.slice(0, likeData.length).map((post) => (
                        <tr className='cate-mypage-detail'>
                            <td width="300">{post.title}</td>
                            <td width="150">{post.postYear == year
                                ? post.postMonth == month && post.postDay == date
                                    ? post.postHour == hours
                                        ? post.postMinute == minutes
                                            ? seconds - post.postSecond + '초 전'
                                            : minutes - post.postMinute == 1 &&
                                                seconds < post.postSecond
                                                ? 60 - post.postSecond + seconds + '초 전'
                                                : minutes - post.postMinute + '분 전'
                                        : hours - post.postHour + '시간 전'
                                    : post.postMonth + '.' + post.postDay
                                : post.postYear.slice(2, 4) +
                                '.' +
                                post.postMonth +
                                '.' +
                                post.postDay}</td>
                            <td width="150">{post.commentsSize}</td>
                            <td width="150">{post.studyStatus}</td>
                        </tr>
                    ))
                )
                }
            </table>
            <FooterGray />
        </div>
    )
}

export default MyPage