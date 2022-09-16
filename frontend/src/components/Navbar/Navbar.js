import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./navbar.css";
import { navItems } from "./NavItems";
import Dropdown from "./Dropdown";
import LoginButton from "./LoginButton";
import RegisterButton from "./RegisterButton";
import LogoutButton from "./LogoutButton";
import MyPageButton from "./MyPageButton";
import Service from "./Service";
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
            <ul className="nav-items" onClick={showSidebar}>
              <li className='navbar-toggle'>
                <Link to='#' className='menu-out'>
                  <AiIcons.AiOutlineClose />
                </Link>
              </li>
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

              {isToken && username ? (
                <div className="nav-right">
                  <MyPageButton /> <LogoutButton /> <Service />
                </div>
              ) : (
                <div className="nav-right">
                  <LoginButton /> <RegisterButton /> <Service />
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
                <MyPageButton /> <LogoutButton /> <Service />
              </div>
            ) : (
              <div className="nav-right">
                <LoginButton /> <RegisterButton /> <Service />
              </div>
            )}
          </nav>)}
        </div>
      </nav>
    </div>
  );
}

export default Navbar;
