import { useState, useEffect } from "react";
import "./SearchPage.css";

export default function SearchPage() {

  const [keySearchTerms, setKeySearchTerms] = useState("");

  const [departments, setDepartments] = useState([]);
  const [professors, setProfessors] = useState([]);

  const [startTime, setStartTime] = useState(0);
  const [endTime, setEndTime] = useState(0);
  const [credits, setCredits] = useState(-1);
  const [courseCode, setCourseCode] = useState("");
  const [semester, setSemester] = useState("");

  const [days, setDays] = useState({
    M: false,
    T: false,
    W: false,
    R: false,
    F: false,
  });

  const [scheduledCourses, setScheduledCourses] = useState([]);
  const [courses, setCourses] = useState([]);

  const [showFilterPopup, setShowFilterPopup] = useState(false);
  const [activeFilters, setActiveFilters] = useState([]);

//   useEffect(() => {
//     async function loadSchedule() {
//       const res = await fetch("/loadCalendar");
//       const data = await res.json();
//       setScheduledCourses(data);
//     }
//     loadSchedule();
//   }, []);

  function computeDaysValue() {
    let value = 1;
    if (days.M) value *= 2;
    if (days.T) value *= 3;
    if (days.W) value *= 5;
    if (days.R) value *= 7;
    if (days.F) value *= 11;
    return value;
  }

  async function searchCourses() {

//     const daysValue = computeDaysValue();

    setCourses([]);

    const validTime = startTime && endTime && endTime > startTime;

    const filter = {
      department: departments[0] || "",
      professor: professors[0] || "",
      courseCode: courseCode || "",
      credits: credits >= 0 ? credits : -1,
      days: computeDaysValue(),
      startTimes: validTime ? Array(5).fill(startTime) : null,
      duration: validTime ? Array(5).fill(endTime - startTime) : null,
      semester: semester || ""
    };

    await fetch("/keySearchTerms",{
        method: "POST",
        headers: { "Content-Type": "text/plain"},
        body: keySearchTerms
    });

    await fetch("/setFilters", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(filter)
    });

    const res = await fetch("/search");
    const data = await res.json();
    setCourses(data);
  }

  async function addCourse(course) {
    const res = await fetch("/addCourse", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(course),
    });

    const result = await res.json();

    if (result.conflicts?.length > 0) {
      const msg =
        "This course conflicts with:\n" +
        result.conflicts.map((c) => c.courseName).join("\n") +
        "\nReplace them all?";

      if (window.confirm(msg)) {
        replaceCourse(course);
      }
    } else {
      alert(result.message);
    }
  }

  async function removeCourse(course) {
    if (!window.confirm("Do you want to remove this course?"))
        return;

    const res = await fetch("/removeCourse", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(course),
    });

    const result = await res.json();
    alert(result.message);

    if (result.success) {
      window.location.reload();
    }
  }

  async function replaceCourse(course) {
    const res = await fetch("/replaceCourse", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(course),
    });

    const result = await res.json();
    alert(result.message);
  }

  function renderCourseSpecifics(course) {
    let text = `${course.department} ${course.professors} Credits: ${course.credits} Time: `;
    let tempStart = 0;
    let tempEnd = 0;

    const dayMap = [
      { mod: 2, label: "M", idx: 0 },
      { mod: 3, label: "T", idx: 1 },
      { mod: 5, label: "W", idx: 2 },
      { mod: 7, label: "R", idx: 3 },
      { mod: 11, label: "F", idx: 4 },
    ];

    dayMap.forEach((d) => {
      if (course.days % d.mod === 0) {
        tempStart = course.startTimes[d.idx];
        tempEnd = tempStart + course.duration[d.idx];
        text += d.label;
      }
    });

    text +=
      " " +
      Math.trunc(tempStart / 60) +
      ":" +
      (tempStart % 60).toLocaleString("en-US", { minimumIntegerDigits: 2 });

    text +=
      " - " +
      Math.trunc(tempEnd / 60) +
      ":" +
      (tempEnd % 60).toLocaleString("en-US", { minimumIntegerDigits: 2 });

    return text;
  }

  function renderFilterInput(filter) {
    switch (filter) {
      case "department":
        return (
          <select
            value=""
            onChange={(e) => {
              const value = e.target.value;
              if (value && !departments.includes(value)) {
                setDepartments([...departments, value]);
              }
            }}
          >
            <option value="">Add Department</option>
            {[
              "ABRD","ACCT","ART","ASTR","BIBL","BIOL","CHEM","COMM","COMP","DESI",
              "ECON","EDUC","ELEE","ENGL","ENGR","ENTR","EXER","FNCE","FREN","GOBL",
              "GREK","HEBR","HIST","HUMA","INBS","MARK","MATH","MECE","MNGT","MUSE",
              "MUSI","NURS","PHIL","PHYE","PHYS","POLS","PSYC","ROBO","SCIC","SEDU",
              "SOCI","SOCW","SPAN","SSFT","STAT","THEA","WRIT"
            ].map((dept) => (
              <option key={dept} value={dept}>
                {dept}
              </option>
            ))}
          </select>
        );

      case "professor":
        return (
          <input
            placeholder="Add professor"
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                const value = e.target.value.trim();
                if (value && !professors.includes(value)) {
                  setProfessors([...professors, value]);
                }
                e.target.value = "";
              }
            }}
          />
        );

      case "time":
        return (
          <div>
            <select value={startTime} onChange={(e) => setStartTime(Number(e.target.value))}>
              <option value="0">Start Time</option>
              {[480,540,600,660,720,780,840,900,960,1020,1080,1140,1200,1260].map((t) => (
                <option key={t} value={t}>{formatTime(t)}</option>
              ))}
            </select>

            <select value={endTime} onChange={(e) => setEndTime(Number(e.target.value))}>
              <option value="0">End Time</option>
              {[480,540,600,660,720,780,840,900,960,1020,1080,1140,1200,1260].map((t) => (
                <option key={t} value={t}>{formatTime(t)}</option>
              ))}
            </select>
          </div>
        );

      case "day":
        return (
          <div>
            {["M","T","W","R","F"].map((d) => (
              <label key={d}>
                <input
                  type="checkbox"
                  checked={days[d]}
                  onChange={() =>
                    setDays((prev) => ({ ...prev, [d]: !prev[d] }))
                  }
                />
                {dayName(d)}
              </label>
            ))}
          </div>
        );

      case "credits":
        return (
          <select value={credits} onChange={(e) => setCredits(Number(e.target.value))}>
            <option value="-1">Credits</option>
            {[0,1,2,3,4].map((c) => (
              <option key={c} value={c}>{c}</option>
            ))}
          </select>
        );

      case "courseCode":
        return (
          <input
            value={courseCode}
            onChange={(e) => setCourseCode(e.target.value)}
            placeholder="Course Code"
          />
        );

      case "semester":
        return (
          <select value={semester} onChange={(e) => setSemester(e.target.value)}>
            <option value="">Semester</option>
            {[
              "2023_Fall","2024_Spring","2024_Fall","2025_Spring","2025_Fall"
            ].map((sem) => (
              <option key={sem} value={sem}>
                {sem}
              </option>
            ))}
          </select>
        );

      default:
        return null;
    }
  }

  function addFilter(name) {
    if (!activeFilters.includes(name)) {
      setActiveFilters([...activeFilters, name]);
    }
    setShowFilterPopup(false);
  }

  return (
    <div className="searchPage">

      <h1>Search</h1>

      <div className="divider"></div>

      <div className="active-filters">
        {activeFilters.map((filter) => (
          <div key={filter} className="filter-pill parent-pill">
            {filter === "department" &&
              departments.map((d) => (
                <div key={d} className="inner-pill">
                  <span>{d.toUpperCase()}</span>
                  <button onClick={() =>
                    setDepartments(departments.filter((x) => x !== d))
                  }>
                    ✕
                  </button>
                </div>
              ))
            }

            {filter === "professor" &&
              professors.map((p) => (
                <div key={p} className="inner-pill">
                  <span>{p}</span>
                  <button onClick={() =>
                    setProfessors(professors.filter((x) => x !== p))
                  }>
                    ✕
                  </button>
                </div>
              ))
            }
            <div className="inline-add">
              {renderFilterInput(filter)}
            </div>

            <button
              className="remove-parent"
              onClick={() => {
                setActiveFilters(activeFilters.filter((f) => f !== filter));
                if (filter === "department") setDepartments([]);
                if (filter === "professor") setProfessors([]);
              }}
            >
              ✕
            </button>

          </div>
        ))}
            <button
                  className="filter-toggle"
                  onClick={() => setShowFilterPopup(true)}
                >
                  Filters
                </button>
      </div>

      {showFilterPopup && (
        <div className="popup-overlay" onClick={() => setShowFilterPopup(false)}>
          <div className="popup" onClick={(e) => e.stopPropagation()}>
            <h3>Select a Filter</h3>

            <button onClick={() => addFilter("department")}>Department</button>
            <button onClick={() => addFilter("professor")}>Professor</button>
            <button onClick={() => addFilter("time")}>Time</button>
            <button onClick={() => addFilter("day")}>Day</button>
            <button onClick={() => addFilter("credits")}>Credits</button>
            <button onClick={() => addFilter("courseCode")}>Course Code</button>
            <button onClick={() => addFilter("semester")}>Semester</button>

            <button className="close-btn" onClick={() => setShowFilterPopup(false)}>
              Close
            </button>
          </div>
        </div>
      )}

      <div className="divider"></div>

      <input
        value={keySearchTerms}
        onChange={(e) => setKeySearchTerms(e.target.value)}
        placeholder="Search for your class"
      />

      <button onClick={searchCourses} className="searchButton">
        Search
      </button>

      <table className="results-table">
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
          {courses.map((course) => {
            const isInSchedule = scheduledCourses.some(
              (sc) =>
                sc.courseCode === course.courseCode &&
                sc.courseName === course.courseName
            );

            return (
              <tr key={`${course.courseCode || "code"}-${course.semester || "sem"}`}>
                <td>{course.courseName} ({course.courseCode})</td>
                <td>{course.professors.join(", ")}</td>
                <td>{formatTime(course.startTimes.find(t => t > 0))}</td>
                <td>{formatTime(course.startTimes.find(t => t > 0) + course.duration[course.startTimes.findIndex(t => t > 0)])}</td>
                <td>{formatDays(course.days)}</td>
                <td>{course.credits}</td>
                <td>
                  {!isInSchedule && (
                    <button onClick={() => addCourse(course)}>Add</button>
                  )}
                  {isInSchedule && (
                    <button onClick={() => removeCourse(course)}>Remove</button>
                  )}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>

    </div>
  );
}

// Helper functions
function formatTime(minutes) {
  const h = Math.trunc(minutes / 60);
  const m = (minutes % 60).toString().padStart(2, "0");
  const suffix = h >= 12 ? "PM" : "AM";
  const hour12 = h % 12 === 0 ? 12 : h % 12;
  return `${hour12}:${m} ${suffix}`;
}

function dayName(letter) {
  return {
    M: "Monday",
    T: "Tuesday",
    W: "Wednesday",
    R: "Thursday",
    F: "Friday",
  }[letter];
}

function formatDays(daysValue) {
  if (!daysValue || daysValue === 1)
    return "";

  const map = [
    { mod: 2, letter: "M" },
    { mod: 3, letter: "T" },
    { mod: 5, letter: "W" },
    { mod: 7, letter: "R" },
    { mod: 11, letter: "F" },
  ];

  let result = "";

  map.forEach((d) => {
    if (daysValue % d.mod === 0) {
      result += d.letter;
    }
  });

  return result;
}