import { Link } from "react-router-dom";
import "./Sidebar.css";
import vbashIcon from '../assets/vbash-logoIcon.png';
import calendarIcon from '../assets/calendarIcon.png';
import searchIcon from '../assets/searchIcon.png';
import accountIcon from '../assets/accountIcon.png';


export default function Sidebar({ isOpen, toggle }) {
  return (
     <div className={`sidebar ${isOpen ? "open" : "closed"}`}>
      <img
          className={`sidebar-image ${isOpen ? "open" : "closed"}`}
          src={vbashIcon}
          alt="VBASH"
          onClick={toggle}
          style={{ cursor: "pointer" }}
      />

      <nav className="sidebar-links">
        <Link to="/" className="sidebar-link">
            <img className="searchIcon" src={searchIcon} alt="SEARCH"  />
            {isOpen && <span className="link-text">Search</span>}
        </Link>
        <Link to="/calendar" className="sidebar-link">
            <img className="calendarIcon" src={calendarIcon} alt="CALENDAR"  />
            {isOpen && <span className="link-text">Calendar</span>}
        </Link>
       <Link to="profile" className="sidebar-link">
            <img className="accountIcon" src={accountIcon} alt="ACCOUNT" />
            {isOpen && <span className="link-text">Account</span>}
       </Link>
      </nav>
    </div>
  );
}