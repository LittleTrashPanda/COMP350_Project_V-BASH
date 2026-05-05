import { useEffect, useState } from "react";
import "./SchedulePage.css";

export default function SchedulePage() {
  const [schedule, setSchedule] = useState([]);

  useEffect(() => {
    loadSchedule();
  }, []);

    async function loadSchedule() {
      const res = await fetch("/user/calendar");
      const data = await res.json();
      setSchedule(data);
    }

  function formatTime(minutes) {
    if (minutes == null) return "—";
    const h = Math.trunc(minutes / 60);
    const m = (minutes % 60).toString().padStart(2, "0");
    const suffix = h >= 12 ? "PM" : "AM";
    const hour12 = h % 12 === 0 ? 12 : h % 12;
    return `${hour12}:${m} ${suffix}`;
  }

  function formatDays(daysValue) {
    if (!daysValue || daysValue === 1) return "";
    const map = [
      { mod: 2, letter: "M" },
      { mod: 3, letter: "T" },
      { mod: 5, letter: "W" },
      { mod: 7, letter: "R" },
      { mod: 11, letter: "F" },
    ];
    return map.filter(d => daysValue % d.mod === 0).map(d => d.letter).join("");
  }

  function getStart(course) {
    const idx = course.startTimes.findIndex(t => t > 0);
    return idx === -1 ? null : course.startTimes[idx];
  }

  function getEnd(course) {
    const idx = course.startTimes.findIndex(t => t > 0);
    return idx === -1 ? null : course.startTimes[idx] + course.duration[idx];
  }

  return (
    <div className="schedulePage">
      <h1>Your Schedule</h1>

      <table className="schedule-table">
        <thead>
          <tr>
            <th>Course</th>
            <th>Professor</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th>Days</th>
            <th>Credits</th>
          </tr>
        </thead>

        <tbody>
          {schedule.map((course) => (
            <tr key={`${course.courseCode}-${course.courseName}`}>
              <td>{course.courseName} ({course.courseCode})</td>
              <td>{course.professors.join(", ")}</td>
              <td>{formatTime(getStart(course))}</td>
              <td>{formatTime(getEnd(course))}</td>
              <td>{formatDays(course.days)}</td>
              <td>{course.credits}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
