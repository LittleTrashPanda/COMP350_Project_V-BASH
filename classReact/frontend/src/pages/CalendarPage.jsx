import { useEffect, useState } from "react";
import "./CalendarPage.css";

export default function CalendarPage() {
    const [courses, setCourses] = useState([]);
    const [saveName, setSaveName] = useState("");
    const [loadName, setLoadName] = useState("");

    useEffect(() => {
        loadCalendar();
    }, []);

    async function loadCalendar() {
        const res = await fetch("/loadCalendar");
        const data = await res.json();
        setCourses(data);
    }

    async function removeCourse(course) {
        if (!window.confirm("Do you want to remove this course?")) return;

        const res = await fetch("/removeCourse", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(course)
        });

        const result = await res.json();
        alert(result.message);

        if (result.success) loadCalendar();
    }

    async function saveSchedule() {
        await fetch("/nameCurrentSchedule", {
            method: "POST",
            body: JSON.stringify(saveName)
        });

        await fetch("/saveSchedule", { method: "POST" });
    }

    async function loadSchedule() {
        await fetch("/loadSchedule", {
            method: "POST",
            body: JSON.stringify(loadName)
        });

        loadCalendar();
    }

    async function resetSchedule() {
        await fetch("/resetSchedule", { method: "POST" });
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
            <a href="/">Home</a>

            <div className="controls">
                <input
                    id="saveScheduleName"
                    placeholder="Save Schedule by Name"
                    value={saveName}
                    onChange={(e) => setSaveName(e.target.value)}
                />
                <button id="saveSchedule" onClick={saveSchedule}>Save</button>

                <input
                    id="loadScheduleName"
                    placeholder="Load Schedule by Name"
                    value={loadName}
                    onChange={(e) => setLoadName(e.target.value)}
                />
                <button id="loadSchedule" onClick={loadSchedule}>Load</button>

                <button id="clearSchedule" onClick={resetSchedule}>Clear Schedule</button>
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
