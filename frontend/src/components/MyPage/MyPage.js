import React from 'react'
import './myPage.css'
import a from '../../img/a.png'
import FooterGray from '../Home/FooterGray'

const MyPage = () => {
    const username = localStorage.getItem('username');
    return (
        <div>
            <div className='mypage-top'>
                <div className='profile-mypage'>
                    <img className='img-mypage' src={a} alt="" />
                </div>
                <div className='text-mypage'>
                    <h1>{username}님 안녕하세요</h1>
                    <h2>중국언어문화학과 19학번</h2>
                </div>
                <button className='btn-change-info' >정보변경</button>
            </div>
            <div className='tag-mypage'>
                <button className='btn-mypage-tag'>작성한글</button>
                <button className='btn-mypage-tag'>좋아요</button>
            </div>
            <div className='submenu-mypage'>
                <button className='btn-mypage-submenu'>스터디구인란</button>
                <button className='btn-mypage-submenu'>Q&A</button>
                <button className='btn-mypage-submenu'>자유게시판</button>
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
                <tr className='cate-mypage-detail'>
                    <td width="300">WebGI 스터디 인원을 모집합니다.</td>
                    <td width="150">2022.05.17</td>
                    <td width="150">0</td>
                    <td width="150">모집중</td>
                </tr>
                <tr className='cate-mypage-detail'>
                    <td width="300">WebGI 스터디 인원을 모집합니다.</td>
                    <td width="150">2022.05.17</td>
                    <td width="150">0</td>
                    <td width="150">모집중</td>
                </tr>
                <tr className='cate-mypage-detail'>
                    <td width="300">WebGI 스터디 인원을 모집합니다.</td>
                    <td width="150">2022.05.17</td>
                    <td width="150">15</td>
                    <td width="150">모집완료</td>
                </tr>
            </table>
            <FooterGray />
        </div>
    )
}

export default MyPage