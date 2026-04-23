import { useState } from "react";
import { Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import SearchPage from "./pages/SearchPage.jsx";
import CalendarPage from "./pages/CalendarPage.jsx";
import "./App.css";

export default function App() {
  const [isOpen, setIsOpen] = useState(false);

  return (
     <>
          {/* Sidebar ALWAYS visible */}
          <Sidebar isOpen={isOpen} toggle={() => setIsOpen(!isOpen)} />

          {/* Main content wrapper ONLY affects SearchPage */}
          <div
            className="main-content"
            style={{
              marginLeft: isOpen ? "240px" : "100px",
              transition: "margin-left 0.3s ease",
              width: "100%",
            }}
          >
            <Routes>
              <Route path="/" element={<SearchPage />} />

              {/* CalendarPage renders WITHOUT the main-content wrapper */}
              <Route
                path="/calendar"
                element={
                  <div style={{ marginLeft: 0, width: "90vw" }}>
                    <CalendarPage />
                  </div>
                }
              />
            </Routes>
          </div>
        </>
  );
}
