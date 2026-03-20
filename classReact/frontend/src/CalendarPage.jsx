import { useEffect, useState } from "react";
import "./CalendarPage.css";

export default function CalendarPage() {
  const [courses, setCourses] = useState([]);
  const [selectedCourse, setSelectedCourse] = useState(null);

  useEffect(() => {
    async function load() {
      const res = await fetch("/calendar");
      const data = await res.json();
      setCourses(data);
    }
    load();
  }, []);

  function removeCourse(course) {
    if (!window.confirm("Do you want to remove this course?")) return;

    fetch("/removeCourse", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(course)
    })
      .then((res) => res.json())
      .then((result) => {
        alert(result.message);
        if (result.success) {
          setCourses((prev) =>
            prev.filter(
              (c) =>
                !(
                  c.courseCode === course.courseCode &&
                  c.courseName === course.courseName
                )
            )
          );
        }
      });
  }

  const days = ["M", "T", "W", "R", "F"];
  const dayMultipliers = [2, 3, 5, 7, 11];

  return (
    <>
      {selectedCourse && (
        <div className="modal-overlay" onClick={() => setSelectedCourse(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h2>{selectedCourse.courseName}</h2>
            <p><strong>Code:</strong> {selectedCourse.courseCode}</p>
            <p><strong>Description:</strong></p>
            <p>{selectedCourse.description}</p>

            <button
              className="remove-btn"
              onClick={() => {
                removeCourse(selectedCourse);
                setSelectedCourse(null);
              }}
            >
              Remove Course
            </button>

            <button onClick={() => setSelectedCourse(null)}>Close</button>
          </div>
        </div>
      )}

      <div className="calendar-container">
        {/* Time Column */}
        <div className="time-column">
          {Array.from({ length: 13 }).map((_, i) => {
            const hour = 8 + i;
            return (
              <div key={i} className="time-slot">
                {hour}:00
              </div>
            );
          })}
        </div>

        {/* Calendar Grid */}
        <div className="calendar-grid">
          {days.map((day, dayIndex) => (
            <div key={day} className="calendar-day-column">
              <div className="day-header">{day}</div>

              <div className="day-body">
                {courses
                  .filter((course) => course.days % dayMultipliers[dayIndex] === 0)
                  .map((course) => {
                    const start = course.startTimes[dayIndex];
                    const duration = course.duration[dayIndex];

                    if (start === 0 || duration === 0) return null;

                    const top = ((start - 8 * 60) / (13 * 60)) * 100;
                    const height = (duration / (13 * 60)) * 100;

                    return (
                      <div
                        key={`${course.courseCode}-${day}-${start}`}
                        className="course-block"
                        style={{ top: `${top}%`, height: `${height}%` }}
                        onClick={() => setSelectedCourse(course)}
                      >
                        <p className="course-title">{course.courseName}</p>

                        <p className="course-time">
                          {formatTime(start, duration)}
                        </p>
                      </div>
                    );
                  })}
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
}

function formatTime(start, duration) {
  const end = start + duration;

  const fmt = (m) =>
    `${Math.trunc(m / 60)}:${(m % 60).toString().padStart(2, "0")}`;

  return `${fmt(start)} - ${fmt(end)}`;
}
