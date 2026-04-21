import { useState } from "react";
import { Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import SearchPage from "./pages/SearchPage.jsx";
// import CalendarPage from "./pages/CalendarPage.jsx";
import ProfilePage from "./pages/ProfilePage.jsx"
import LoginPage from "./pages/LoginPage.jsx";
import SignupPage from "./pages/SignupPage.jsx"
import ProtectedRoute from  "./firebase/ProtectedRoute.jsx"
import "./App.css";

export default function App() {
  const [isOpen, setIsOpen] = useState(false);
  return (
    <>
      <Sidebar isOpen={isOpen} toggle={() => setIsOpen(!isOpen)} />
      <div
        className="main-content"
        style={{
          marginLeft: isOpen ? "240px" : "100px",
          transition: "margin-left 0.3s ease",
          width: "100%",
        }}
      >
        <Routes>
          <Route path="/" element={
              <SearchPage />
              }/>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route
            path="/profile"
            //Profile needs to be under a ProtectedRoute so that it can check if logged in or not
            element={
                <ProtectedRoute>
                <ProfilePage />
                </ProtectedRoute>
            }
          />
        </Routes>
      </div>
    </>
  );
}