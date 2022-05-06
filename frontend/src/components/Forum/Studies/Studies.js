import React from 'react'
import './studies.css'
import Submenu from './Submenu'
import a from "../../../img/a.png"
import b from "../../../img/b.png"
import c from "../../../img/c.png"
import Category from './Category'

const Studies = () => {
    return (
        <div>
            <Submenu />
            <div>
                <div className='top-studies'>
                    <h3 className='top-msg'>따끈따끈한 구인란이에요!</h3>
                    <h4 className='top-msg-gray'>함께 성장할 스터디를 모집해보세요</h4>
                    <div className='top-cards'>
                        <div className='top-card'>
                            <div className='top-circle'>
                                <div className='top-profile'>
                                    <img className="top-photo" src={a} />
                                </div>
                            </div>
                            <div className='top-detail'>
                                <div className='top-title'>스터디그룹 A</div>
                                <div className='top-people'>멤버 3명</div>
                                <div className='top-content'>프론트엔트 인터뷰 스터디 2분 모집합니다.</div>
                                <div className='top-date'>방금 전</div>
                            </div>
                        </div>
                        <div className='top-card'>
                            <div className='top-circle'>
                                <div className='top-profile'>
                                    <img className="top-photo" src={c} />
                                </div>
                            </div>
                            <div className='top-detail'>
                                <div className='top-title'>스터디그룹 A</div>
                                <div className='top-people'>멤버 3명</div>
                                <div className='top-content'>프론트엔트 인터뷰 스터디 2분 모집합니다.</div>
                                <div className='top-date'>방금 전</div>
                            </div>
                        </div>
                        <div className='top-card'>
                            <div className='top-circle'>
                                <div className='top-profile'>
                                    <img className="top-photo" src={b} />
                                </div>
                            </div>
                            <div className='top-detail'>
                                <div className='top-title'>스터디그룹 A</div>
                                <div className='top-people'>멤버 3명</div>
                                <div className='top-content'>프론트엔트 인터뷰 스터디 2분 모집합니다.</div>
                                <div className='top-date'>방금 전</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className='body-studies'>
                    <Category />
                    <div className='search-and-write'>
                        <div className='studies-search'>
                            <input type='text' placeholder='   맞춤 스터디그룹을 찾아보세요' className='search-input' />
                            <img></img>
                        </div>
                        <button className='studies-write-btn'>글 쓰기</button>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Studies