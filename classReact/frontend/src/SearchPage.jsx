import { useEffect, useState } from "react";
import "./SearchPage.css";
import {
  sendKeySearchTerms,
  sendFilters,
  fetchSearchResults,
  addCourse,
  removeCourse,
  replaceCourse,
  fetchScheduledCourses,
  fetchKeySearchData
} from "./searchUtils";

export default function SearchPage() {
  // Form state
  const [keySearchTerms, setKeySearchTerms] = useState("");
  const [department, setDepartment] = useState("");
  const [courseCode, setCourseCode] = useState("");
  const [professor, setProfessor] = useState("");
  const [credits, setCredits] = useState("");
  const [semester, setSemester] = useState("");

  const DEPARTMENT_OPTIONS = [
    "ABRD","ACCT","ART","ASTR","BIBL","BIOL","CHEM","COMM","COMP","DESI",
    "ECON","EDUC","ELEE","ENGL","ENGR","ENTR","EXER","FNCE","FREN","GOBL",
    "GREK","HEBR","HIST","HUMA","INBS","MARK","MATH","MECE","MNGT","MUSE",
    "MUSI","NURS","PHIL","PHYE","PHYS","POLS","PSYC","ROBO","SCIC","SEDU",
    "SOCI","SOCW","SPAN","SSFT","STAT","THEA","WRIT"
  ];


  const [days, setDays] = useState({
    M: false,
    T: false,
    W: false,
    R: false,
    F: false
  });

  const TIME_OPTIONS = [
    480, 540, 570, 600, 660, 720, 750, 780, 840, 900, 930,
    960, 1020, 1080, 1110, 1140, 1200, 1260
  ];


  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");

  // Dropdown data
  const [departments, setDepartments] = useState([]);
  const [times, setTimes] = useState([]);

  // Results + schedule
  const [results, setResults] = useState([]);
  const [scheduledCourses, setScheduledCourses] = useState([]);

  // Load initial data
  useEffect(() => {
    async function loadData() {
      const schedule = await fetchScheduledCourses();
      setScheduledCourses(schedule);

      const keyData = await fetchKeySearchData();
      setDepartments(keyData.departments || []);
      setTimes(keyData.times || []);
    }
    loadData();
  }, []);

  // Convert checkbox days → multiplier system
  function computeDayMultiplier() {
    let mult = 1;
    if (days.M) mult *= 2;
    if (days.T) mult *= 3;
    if (days.W) mult *= 5;
    if (days.R) mult *= 7;
    if (days.F) mult *= 11;
    return mult;
  }

  // Build time arrays in backend format
  function buildTimeArrays() {
    const dayMult = computeDayMultiplier();

    const hasStart = startTime !== "" && !isNaN(startTime);
    const hasEnd = endTime !== "" && !isNaN(endTime);

    // If neither start nor end → ignore time
    if (!hasStart && !hasEnd) {
      return { startTimes: null, duration: null };
    }

    // Decide which days to apply time to:
    // - if user checked days → only those
    // - if no days checked → apply to ALL days (frontend-only behavior)
    const useDays = {
      M: days.M || dayMult === 1,
      T: days.T || dayMult === 1,
      W: days.W || dayMult === 1,
      R: days.R || dayMult === 1,
      F: days.F || dayMult === 1
    };

    let st, dur;

    // Start only → 1-minute window
    if (hasStart && !hasEnd) {
      st = Number(startTime);
      dur = 1;
    }
    // End only → 1-minute window ending at endTime
    else if (!hasStart && hasEnd) {
      const et = Number(endTime);
      st = et - 1;
      dur = 1;
    }
    // Start + End → normal window
    else {
      const s = Number(startTime);
      const e = Number(endTime);
      const d = e - s;
      if (d <= 0) {
        return { startTimes: null, duration: null };
      }
      st = s;
      dur = d;
    }

    return {
      startTimes: [
        useDays.M ? st : 0,
        useDays.T ? st : 0,
        useDays.W ? st : 0,
        useDays.R ? st : 0,
        useDays.F ? st : 0
      ],
      duration: [
        useDays.M ? dur : 0,
        useDays.T ? dur : 0,
        useDays.W ? dur : 0,
        useDays.R ? dur : 0,
        useDays.F ? dur : 0
      ]
    };
  }



  // Handle search
  async function handleSearch() {
    await sendKeySearchTerms(keySearchTerms);

    const dayMult = computeDayMultiplier();
    const { startTimes, duration } = buildTimeArrays();

    const filter = {
      department: department || "",
      courseCode: courseCode || "",
      professor: professor.trim() === "" ? null : professor,
      credits: credits ? parseInt(credits) : -1,
      days: dayMult, // MUST be 1 when no days selected
      startTimes: startTimes, // null or array
      duration: duration,     // null or array
      semester: semester || ""
    };

    await sendFilters(filter);

    const res = await fetchSearchResults();
    const cloned = res.map(c => JSON.parse(JSON.stringify(c)));
    setResults(cloned);
  }

  return (
    <div className="search-container">
      <h1 className="search-title">Search for Classes</h1>

      {/* Search Bar */}
      <div className="search-bar">
        <input
          className="search-input"
          type="text"
          placeholder="Search for your class"
          value={keySearchTerms}
          onChange={(e) => setKeySearchTerms(e.target.value)}
        />
        <button className="search-button" onClick={handleSearch}>
          Search
        </button>
      </div>

      {/* Filters */}
      <div className="filters-card">
        <h2>Filters</h2>

        <div className="filters-grid">
          {/* Department */}
          <div>
            <div className="filter-label">Department</div>
            <select
              className="filter-select"
              value={department}
              onChange={(e) => setDepartment(e.target.value)}
            >
              <option value="">DEPT</option>

              {DEPARTMENT_OPTIONS.map((d) => (
                <option key={d} value={d}>{d}</option>
              ))}
            </select>

          </div>

          {/* Professor */}
          <div>
            <div className="filter-label">Professor</div>
            <input
              className="filter-input"
              type="text"
              value={professor}
              onChange={(e) => setProfessor(e.target.value)}
            />
          </div>

          {/* Time */}
          <div>
            <div className="filter-label">Time</div>
            <div style={{ display: "flex", gap: "10px" }}>
              <select
                className="filter-select"
                value={startTime}
                onChange={(e) => setStartTime(parseInt(e.target.value))}
              >
                <option value="">START TIME</option>

                {TIME_OPTIONS.map((t) => (
                  <option key={t} value={t}>
                    {formatMinutes(t)}
                  </option>
                ))}
              </select>


              <select
                className="filter-select"
                value={endTime}
                onChange={(e) => setEndTime(parseInt(e.target.value))}
              >
                <option value="">END TIME</option>

                {TIME_OPTIONS.map((t) => (
                  <option key={t} value={t}>
                    {formatMinutes(t)}
                  </option>
                ))}
              </select>
            </div>
          </div>

          {/* Days */}
          <div>
            <div className="filter-label">Days</div>
            <div className="day-checkboxes">
              {["M", "T", "W", "R", "F"].map((d) => (
                <label key={d}>
                  <input
                    type="checkbox"
                    checked={days[d]}
                    onChange={() =>
                      setDays({ ...days, [d]: !days[d] })
                    }
                  />
                  {d}
                </label>
              ))}
            </div>
          </div>

          {/* Credits */}
          <div>
            <div className="filter-label">Credits</div>
            <select
              className="filter-select"
              value={credits}
              onChange={(e) => setCredits(e.target.value)}
            >
              <option value="">Select</option>
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
              <option value="4">4</option>
            </select>
          </div>

          {/* Course Code */}
          <div>
            <div className="filter-label">Course Code</div>
            <input
              className="filter-input"
              type="text"
              value={courseCode}
              onChange={(e) => setCourseCode(e.target.value)}
            />
          </div>

          {/* Semester */}
          <div>
            <div className="filter-label">Semester</div>
            <input
              className="filter-input"
              type="text"
              value={semester}
              onChange={(e) => setSemester(e.target.value)}
            />
          </div>
        </div>
      </div>

      {/* Results */}
      <div className="results-container">
        {results.map((course) => {
          const isInSchedule = scheduledCourses.some(
            (sc) =>
              sc.courseCode === course.courseCode &&
              sc.courseName === course.courseName
          );

          return (
            <div key={`${course.courseCode}-${course.courseName}-${course.semester}`} className="course-card">
              <h3>{course.courseName} - {course.courseCode}</h3>

              <p>
                {course.department} — {course.professors} — Credits: {course.credits}
              </p>

              <p>
                Days: {formatDays(course.days)}
                Time: {formatTime(course.startTimes, course.duration)}
              </p>

              <button onClick={() => addCourse(course)}>
                Add to Schedule
              </button>

              {isInSchedule && (
                <button onClick={() => removeCourse(course)}>
                  Remove from Schedule
                </button>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}

// Helper to format days
function formatDays(mult) {
  let s = "";
  if (mult % 2 === 0) s += "M";
  if (mult % 3 === 0) s += "T";
  if (mult % 5 === 0) s += "W";
  if (mult % 7 === 0) s += "R";
  if (mult % 11 === 0) s += "F";
  return s;
}

// Helper to format time
function formatTime(startTimes, duration) {
  // If backend sends null or empty, bail out
  if (!startTimes || !duration || startTimes.length === 0 || duration.length === 0) {
    return "No Meeting Time";
  }

  // Find the first day that actually has a time
  const idx = startTimes.findIndex((t, i) => t > 0 && duration[i] > 0);

  if (idx === -1) {
    return "No Meeting Time";
  }

  const st = startTimes[idx];
  const et = st + duration[idx];

  const fmt = (m) =>
    `${Math.trunc(m / 60)}:${(m % 60).toString().padStart(2, "0")}`;

  return `${fmt(st)} - ${fmt(et)}`;
}

function formatMinutes(m) {
  const h = Math.floor(m / 60);
  const min = (m % 60).toString().padStart(2, "0");
  const suffix = h >= 12 ? "PM" : "AM";
  const hour12 = ((h + 11) % 12) + 1;
  return `${hour12}:${min} ${suffix}`;
}

