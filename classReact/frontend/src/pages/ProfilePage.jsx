import React, { useState, useEffect } from "react";
import { auth } from "../firebase/firebase";
import { signOut } from "firebase/auth";
import { themes } from "../themes";
import "./ProfilePage.css";

export default function ProfilePage() {
  const user = auth.currentUser;

  const handleLogout = async () => {
    await signOut(auth);
  };

  const [selectedTheme, setSelectedTheme] = useState(() => {
    return localStorage.getItem("theme") || "light";
  });


    const applyTheme = (themeName) => {
      const theme = themes[themeName];
      if (!theme) return;

      const root = document.documentElement;

      // Set all variables for this theme
      Object.entries(theme).forEach(([key, value]) => {
        root.style.setProperty(key, value);
      });
    };

    const handleThemeChange = (e) => {
      const theme = e.target.value;
      setSelectedTheme(theme);
      applyTheme(theme);
      localStorage.setItem("theme", theme);
    };



  return (
    <div style={{ padding: "20px" }}>
     <div className="profilePage" style={{ padding: "20px" }}>
       <h1>Your Profile</h1>
       <p>Email: {user.email}</p>
       <p>Welcome to your profile page.</p>

       <button onClick={handleLogout}>Log Out</button>

       <label htmlFor="theme">Choose Theme: </label>
       <select id="theme" value={selectedTheme} onChange={handleThemeChange}>
         <option value="light">Light</option>
         <option value="dark">Dark</option>
         <option value="gcc">GCC Colors</option>
         <option value="forest">Forest</option>
         <option value="midnight">Midnight</option>
         <option value="peachCream">Peach & Cream</option>
         <option value="cosmicVoid">Cosmic Void</option>
         <option value="sunset">Sunset</option>
         <option value="bubblegum">Bubblegum</option>
       </select>
     </div>
    </div>
  );
}