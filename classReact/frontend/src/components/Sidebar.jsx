import { Link } from "react-router-dom";
import "./Sidebar.css";
import vbash from '../assets/vbash-logo.png';
import calendar from '../assets/calendar.png';
import search from '../assets/search.png';
import account from '../assets/account.png';


export default function Sidebar({ isOpen, toggle }) {
  return (
     <div className={`sidebar ${isOpen ? "open" : "closed"}`}>
      <img
          className={`sidebar-image ${isOpen ? "open" : "closed"}`}
          src={vbash}
          alt="VBASH"
          onClick={toggle}
          style={{ cursor: "pointer" }}
      />

      <nav className="sidebar-links">
        <Link to="/" className="sidebar-link">
            <img className="search" src={search} alt="SEARCH"  />
            {isOpen && <span className="link-text">Search</span>}
        </Link>
        <Link to="/calendar" className="sidebar-link">
            <img className="calendar" src={calendar} alt="CALENDAR"  />
            {isOpen && <span className="link-text">Calendar</span>}
        </Link>
       <Link to="profile" className="sidebar-link">
            <img className="account" src={account} alt="ACCOUNT" />
            {isOpen && <span className="link-text">Account</span>}
       </Link>
      </nav>
    </div>
  );
}