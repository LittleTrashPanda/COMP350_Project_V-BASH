import React, { useState, useEffect } from "react";
import { auth } from "../firebase/firebase";
import { signOut } from "firebase/auth";
import { themes } from "../themes";
import "./ProfilePage.css";

export default function ProfilePage() {
  const user = auth.currentUser;

    function refresh() {
        populateMajorRequirements();
        populateTakenCourses();
    }

  const handleLogout = async () => {
    await signOut(auth);
  };

  const [selectedTheme, setSelectedTheme] = useState(() => {
    return localStorage.getItem("theme") || "gcc";
  });

    const [userMajor, setUserMajor] = useState("");

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

    const handleMajorChange = (e) => {
        console.log("hello");
        changeMajor()
    }

    async function changeMajor() {
        const major = document.getElementById("major");
        await fetch("/major", {
            method: "POST",
            headers: { "Content-Type": "text/plain"},
            body: major.value
        });

        populateMajorRequirements();
    }

    async function populateMajorRequirements() {
        const majorRequirementsList = document.getElementById("majorRequirementsList");
        majorRequirementsList.innerHTML = "";
        const data = await fetch("/majorRequirements");
        const majorRequirements = await data.json();

        for (const majorRequirement of majorRequirements) {
            const temp = document.createElement("li");
            temp.textContent = majorRequirement.courseCode;
            majorRequirementsList.appendChild(temp);
        }
    }

    async function populateTakenCourses() {
        const takenCoursesList = document.getElementById("takenCoursesList");
        takenCoursesList.innerHTML = "";
        const data = await fetch("/takenCourses");
        const takenCourses = await data.json();

        for (const takenCourse of takenCourses) {
            const temp = document.createElement("li");
            temp.textContent = takenCourse.courseCode;
            takenCoursesList.appendChild(temp);
        }
    }

    async function addTakenCourse() {
        const userInput = document.getElementById("newTakenCourseInput");
        await fetch("/takenCourse", {
            method: "POST",
            headers: { "Content-Type": "text/plain"},
            body: userInput.value.toUpperCase()
        });

        const takenCoursesList = document.getElementById("takenCoursesList");
        const newTakenCourse = document.createElement("li");
        newTakenCourse.textContent = userInput.value.toUpperCase();
        takenCoursesList.appendChild(newTakenCourse);
    }

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

       <p>DATA:</p>
       <button onClick = { () => refresh() }>Refresh Profile Data</button>

       <p>Major: </p>
       <select id = "major" placeholder = "Major" value = { userMajor } onChange = { handleMajorChange }>
           <option value = "">None</option>
           <option value = "B.S. Computer Science">B.S. Computer Science</option>
           <option value = "B.S. Mechanical Engineering">B.S. Mechanical Engineering</option>
       </select>

       <p>Major Required Courses: </p>
       <ul id = "majorRequirementsList" onLoad = { () => populateMajorRequirements() }></ul>

       <p>Taken Courses: </p>
       <ul id = "takenCoursesList" onLoad = { () => populateTakenCourses() }></ul>

       <p>Add Taken Course: </p>
       <input id = "newTakenCourseInput" placeholder = "Course Code"/>
       <button onClick = { () => addTakenCourse() }>Add Course</button>
     </div>
    </div>
  );
}