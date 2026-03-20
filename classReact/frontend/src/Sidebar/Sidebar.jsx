import "./Sidebar.css";
import { Link } from "react-router-dom";
import { getButtonStyle } from "./sidebarUtils";

export default function Sidebar({ isOpen, toggle }) {
  return (
    <div className={isOpen ? "sidebar open" : "sidebar"}>
      <button className="toggle-btn" onClick={toggle}>
        {isOpen ? "Close" : "Menu"}
      </button>

      {isOpen && (
        <div className="sidebar-buttons">
          <Link className="sidebar-link" style={getButtonStyle()} to="/">
            Search
          </Link>

          <Link className="sidebar-link" style={getButtonStyle()} to="/calendar">
            Calendar
          </Link>

          <Link className="sidebar-link" style={getButtonStyle()} to="/saved">
            Saved Schedule
          </Link>
        </div>
      )}
    </div>
  );
}
