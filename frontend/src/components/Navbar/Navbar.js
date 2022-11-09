import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./navbar.css";
import { navItems, communityDropdown } from "./NavItems";
import Dropdown from "./Dropdown";
import LoginButton from "./LoginButton";
import RegisterButton from "./RegisterButton";
import LogoutButton from "./LogoutButton";
import MyPageButton from "./MyPageButton";
import MainLogo from "../../img/logo_main.png";
import * as FaIcons from 'react-icons/fa';
import * as AiIcons from 'react-icons/ai';
import { useMediaQuery } from 'react-responsive'

function Navbar() {
  const [dropdown, setDropdown] = useState(false);
  const [changing, setChanging] = useState(false);
  const [scrolling, setScrolling] = useState(false);
  const isToken = localStorage.getItem("accessToken");
  const username = localStorage.getItem("username");
  const [sidebar, setSidebar] = useState(false);
  const showSidebar = () => setSidebar(!sidebar);
  const isTabletOrMobile = useMediaQuery({ maxWidth: 1224 })

  const onMouseOverOut = () => {
    if (scrolling) return;
    setChanging((current) => !current);
  };

  return (
    <div className="nav">
      <nav
        className="navbar"
        onMouseOver={onMouseOverOut}
        onMouseOut={onMouseOverOut}
        style={
          changing
            ? { backgroundColor: "#white" }
            : { backgroundColor: "#white" }
        }
      >
        {isToken && username ? (
          <Link to="/main" className="navbar-logo">
            <img className="nav-main" alt="" src={MainLogo} />
          </Link>
        ) : (
          <Link to="/" className="navbar-logo">
            <img className="nav-main" alt="" src={MainLogo} />
          </Link>
        )}
        {isTabletOrMobile && <Link to='#' className='menu-bars'>
          <FaIcons.FaBars onClick={showSidebar} />
        </Link>}

        <div>
          {isTabletOrMobile ? (<nav className={sidebar ? 'nav-menu active' : 'nav-menu'}>
            <ul className="nav-items">
              <li className='navbar-toggle'>
                <Link to='#' className='menu-out' onClick={showSidebar}>
                  <AiIcons.AiOutlineClose />
                </Link>
              </li>
              {navItems.map((item) => {
                // if (item.title === "커뮤니티") {
                //   return (
                //     <li
                //       key={item.id}
                //       className={item.cName}
                //       onMouseEnter={() => setDropdown(true)}
                //       onMouseLeave={() => setDropdown(false)}
                //     >
                //       <Link to={item.path}>{item.title}</Link>
                //       {}
                //     </li>
                //   );
                // }
                if (item.title === "커뮤니티") {
                  return (
                    <ul>
                      <li key="1" className="nav-item">
                        <Link to="./studies" onClick={showSidebar}>커뮤니티</Link>
                      </li>
                      <div className="nav-sub">
                        <li key="1" className="submenu-item-m">
                          <Link to="./studies" onClick={showSidebar}>스터디구인란</Link>
                        </li>
                        <li key="2" className="submenu-item-m">
                          <Link to="./questions" onClick={showSidebar}>Q&A</Link>
                        </li>
                        <li key="3" className="submenu-item-m">
                          <Link to="./chats" onClick={showSidebar}>자유게시판</Link>
                        </li>
                      </div>
                    </ul>
                  );
                }
                return (
                  <li key={item.id} className={item.cName}>
                    <Link to={item.path} onClick={showSidebar}>{item.title}</Link>
                  </li>
                );
              })}
              {isToken && username ? (
                <div className="nav-right">
                  <MyPageButton /> <LogoutButton />
                </div>
              ) : (
                <div className="nav-right">
                  <LoginButton /> <RegisterButton />
                </div>
              )}
            </ul>
          </nav>) : (<nav className="nav-menu">
            <ul className="nav-items">
              {navItems.map((item) => {
                if (item.title === "커뮤니티") {
                  return (
                    <li
                      key={item.id}
                      className={item.cName}
                      onMouseEnter={() => setDropdown(true)}
                      onMouseLeave={() => setDropdown(false)}
                    >
                      <Link to={item.path}>{item.title}</Link>
                      {dropdown && <Dropdown />}
                    </li>
                  );
                }
                return (
                  <li key={item.id} className={item.cName}>
                    <Link to={item.path}>{item.title}</Link>
                  </li>
                );
              })}
            </ul>
            {isToken && username ? (
              <div className="nav-right">
                <MyPageButton /> <LogoutButton />
              </div>
            ) : (
              <div className="nav-right">
                <LoginButton /> <RegisterButton />
              </div>
            )}
          </nav>)}
        </div>
      </nav >
    </div >
  );
}

export default Navbar;
