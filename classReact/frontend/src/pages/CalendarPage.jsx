import { useEffect, useState } from "react";
import "./CalendarPage.css";

export default function CalendarPage() {
    const [courses, setCourses] = useState([]);
    const [saveName, setSaveName] = useState("");
    const [loadName, setLoadName] = useState("");
    const [deleteName, setDeleteName] = useState("");

    const [currentSemester, setCurrentSemester] = useState("");


    useEffect(() => {
        loadCalendar();
    }, []);

    async function loadCalendar() {
        const res = await fetch("/calendar");
        const data = await res.json();
        setCourses(data);
    }

    async function removeCourse(course) {
        if (!window.confirm("Do you want to remove this course?")) return;

        const res = await fetch("/course", {
            method: "DELETE",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(course)
        });

        const result = await res.json();
        alert(result.message);

        if (result.success)
        loadCalendar();
    }

    async function saveSchedule() {
        await fetch("/schedule/name", {
            method: "PUT",
            body: JSON.stringify(saveName)
        });

        await fetch("/schedule/save", { method: "POST" });

        // Add the saved name to the load dropdown
        const select = document.getElementById("loadScheduleName");
        select.add(new Option(saveName, saveName));

        loadCalendar();
    }

    async function loadSchedule() {
        await fetch("/schedule", {
            method: "GET",
            body: JSON.stringify(loadName)
        });

        loadCalendar();
    }

    async function asPDF() {
        const courseTime = await fetch("/getTimes", { method: "GET" });
        const times = await courseTime.text();

        const bodyText = document.getElementById("pdf-text");
        bodyText.innerText = times;

        const element = document.getElementById("pdf-save");


        element.style.display = "block";

        const options = {
            margin: 10,
            filename: "advisor-please-approve-this.pdf",
            image: { type: "jpeg", quality: 0.98 },
            html2canvas: { scale: 2, useCORS: true},
            jsPDF: { unit: "mm", format: "a4", orientation: "portrait" }
        };

        await window.html2pdf().set(options).from(element).save();
        element.style.display = "none";

    }

    async function deleteSchedule() {
        await fetch("/schedule", {
            method: "DELETE",
            body: JSON.stringify(deleteName)
        });

        loadCalendar();
    }

    function addNewOption() {
        const select = document.getElementById("loadScheduleName");
        const text = document.getElementById("newItemText").value;

        if (!text.trim()) return;

        select.add(new Option(text, text));
    }

    async function resetSchedule() {
        await fetch("/schedule", { method: "POST" });
        loadCalendar();
    }

    const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];
    const divisors = [2, 3, 5, 7, 11];

    const timeMarkers = [
        "8:00","8:30","9:00","9:30","10:00","10:30","11:00","11:30",
        "12:00","12:30","1:00","1:30","2:00","2:30","3:00","3:30",
        "4:00","4:30","5:00","5:30","6:00","6:30","7:00","7:30",
        "8:00","8:30"
    ];

    return (
        <div className="calendarPage">

            <h1>Calendar</h1>

            <p>Selected Semester</p>
                <select id = "currentSemester" value = { currentSemester } onChange = { handleCurrentSemesterChange }>
                    <option value = "2025_Spring">Spring 2025</option>
                    <option value = "2024_Fall">Fall 2024</option>
                    <option value = "2024_Spring">Spring 2024</option>
                    <option value = "2023_Fall">Fall 2023</option>
                    <option value = "2023_Spring">Spring 2023</option>
                    <option value = "2022_Fall">Fall 2022</option>
                </select>

            <div className="controls">

                {/* Save Schedule */}
                <input
                    id="saveScheduleName"
                    placeholder="Save Schedule by Name"
                    value={saveName}
                    onChange={(e) => setSaveName(e.target.value)}
                />
                <button id="saveSchedule" onClick={saveSchedule}>Save</button>

                {/* Load Schedule Dropdown */}
                <label htmlFor="loadScheduleName">Choose yo name</label>
                <select
                    name="Names"
                    id="loadScheduleName"
                    value={loadName}
                    onChange={(e) => setLoadName(e.target.value)}
                >
                    {/* Options added dynamically */}
                </select>

                {/* Add Option */}
                <input
                    type="text"
                    id="newItemText"
                    placeholder="New option text"
                />
                <button onClick={addNewOption}>Add Option</button>

                {/* Load */}
                <button id="loadSchedule" onClick={loadSchedule}>Load</button>

                {/* Delete */}
                <button id="deleteSchedule" onClick={deleteSchedule}>Delete Schedule</button>

                {/* PDF */}
                <button id="generatePDF" onClick={asPDF}>Save as PDF</button>

                {/* Hidden PDF container */}
                <div id="pdf-save" className="pdf" style={{ display: "none" }}>
                    <h1>Schedule</h1>
                    <p id="pdf-text">test</p>
                </div>

            </div>

            <div className="calendarContentContainer">

                {/* Time Markers */}
                <div className="timeMarkers">
                    {timeMarkers.map((t, i) => (
                        <div
                            key={i}
                            className="timeMarker"
                            style={{ top: `calc(${((i)*0.5) / 13} * 100%)` }}
                        >
                            <p className="time">{t}</p>
                        </div>
                    ))}
                </div>

                {/* Calendar Grid */}
                <ul id="calendar" className="calendar">
                    {days.map((day, dayIndex) => (
                        <li key={day} className="calendarDay">
                            <p className="dayTitle"><b>{day}</b></p>

                            <div className="dayCourseTimeTable">
                                {courses
                                    .filter(c => c.days % divisors[dayIndex] === 0)
                                    .map((course, i) => {
                                        const top = ((course.startTimes[dayIndex] - 480) / 780) * 100;
                                        const height = (course.duration[dayIndex] / 780) * 100;

                                        return (
                                            <div
                                                key={i}
                                                className="dayCourse"
                                                style={{ top: `${top}%`, height: `${height}%` }}
                                            >
                                                <p className="dayCourseTitle">{course.courseName}</p>
                                                <button
                                                    className="removeButton"
                                                    onClick={() => removeCourse(course)}
                                                >
                                                    Remove
                                                </button>
                                            </div>
                                        );
                                    })}
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}
