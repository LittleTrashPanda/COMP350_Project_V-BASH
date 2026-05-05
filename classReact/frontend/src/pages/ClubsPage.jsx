import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./ClubsPage.css";

export default function ClubsPage() {
  const [clubInput, setClubInput] = useState("");
  const [selectedClub, setSelectedClub] = useState("");
  const [meetingDays, setMeetingDays] = useState([]);
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");
  const [clubs, setClubs] = useState([]);

  // ✅ Load saved clubs on page load (replaces loadClubs())
  useEffect(() => {
    async function loadClubs() {
      const res = await fetch("/loadClubs");
      const data = await res.json();
      setClubs(data);
    }
    loadClubs();
  }, []);

  // ✅ Toggle meeting days (replaces querySelectorAll logic)
  const toggleDay = (day) => {
    setMeetingDays((prev) =>
      prev.includes(day)
        ? prev.filter((d) => d !== day)
        : [...prev, day]
    );
  };

  // ✅ Add club (replaces addClub())
  const handleEnter = () => {
    const clubName = clubInput || selectedClub;
    if (!clubName) return;

    if (clubs.some((c) => c.name === clubName)) {
      alert("That club is already added.");
      return;
    }

    if (meetingDays.length === 0 || !startTime || !endTime) {
      alert("Please select meeting days and times.");
      return;
    }

    setClubs((prev) => [
      ...prev,
      {
        name: clubName,
        days: meetingDays,
        startTime,
        endTime,
      },
    ]);

    setClubInput("");
    setSelectedClub("");
    setMeetingDays([]);
    setStartTime("");
    setEndTime("");
  };

  // ✅ Delete club (replaces deleteClub())
  const deleteClub = (name) => {
    setClubs((prev) => prev.filter((c) => c.name !== name));
  };

  // ✅ Save clubs (replaces saveClubs())
  const saveClubs = async () => {
    await fetch("/saveClubs", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(clubs),
    });
    alert("Clubs saved.");
  };

  return (
    <div className="clubs-page">
      <h1>Clubs</h1>

      <div>
        <Link to="/" className="indexLink">
          Home
        </Link>
      </div>

      <input
        placeholder="Enter Club..."
        value={clubInput}
        onChange={(e) => setClubInput(e.target.value)}
      />

      <select
        value={selectedClub}
        onChange={(e) => setSelectedClub(e.target.value)}
      >
        <option value=""></option>
        <option>Fencing</option>
        <option>Martial Arts</option>
        <option>Men's Ice Hockey</option>
        <option>Men's Rugby</option>
        <option>Men's Wrestling</option>
        <option>Ultimate Frisbee</option>
        <option>Women's Field Hockey</option>
        <option>Disc Golf</option>
        <option>Fly-Fishing</option>
        <option>Outing</option>
        <option>Ski and Snowboarding</option>
        <option>Delight Ministries</option>
        <option>Engineers with a Mission</option>
        <option>Fellowship of Christian Athletes</option>
        <option>Fellowship of Christian Educators</option>
        <option>Hymn Sing</option>
        <option>International Justice Mission</option>
        <option>Koinonia Gospel Team</option>
        <option>Life Advocates</option>
        <option>Lutheran Student Fellowship</option>
        <option>MetbyLove</option>
        <option>New Life</option>
        <option>Newman</option>
        <option>Orthodox Christian Fellowship</option>
        <option>Prison Ministry</option>
        <option>Ratio Christi</option>
        <option>Simple Charity</option>
        <option>Student Mission Fellowship</option>
        <option>Treasured Connections</option>
        <option>Warriors for Christ</option>
        <option>Women of Faith</option>
        <option>Young Life</option>
        <option>Accounting Society</option>
        <option>American Chemical Society</option>
        <option>American Sign Language</option>
        <option>American Society of Heating, Refrigeration and Air-conditioning Engineers</option>
        <option>American Society of Mechanical Engineers</option>
        <option>Association of Computing Machinery</option>
        <option>Social Work</option>
        <option>Council for Exceptional Children</option>
        <option>Environmental</option>
        <option>Crimson Collegiate Investors</option>
        <option>Exercise Science</option>
        <option>French</option>
        <option>Institute of Electrical and Electronics Engineers</option>
        <option>Management Association</option>
        <option>Mathematics Society</option>
        <option>Nursing</option>
        <option>Physics</option>
        <option>Pre-Health Society</option>
        <option>Secondary Education Society</option>
        <option>Society of Automotive Engineers/Baja Race Team</option>
        <option>Society of Women Engineers</option>
        <option>Spanish</option>
        <option>Speech & Debate Team</option>
        <option>National Association of Teachers of Singing</option>
        <option>Women for Computer Science</option>
        <option>Student Government Association</option>
        <option>Orchesis Dance Troupe</option>
        <option>Ballroom Dance</option>
        <option>Chess</option>
        <option>College Democrats</option>
        <option>College Republicans</option>
        <option>Crimsonprov</option>
        <option>Fireside Poets</option>
        <option>Amateur Radio</option>
        <option>Grove City Socratic Society</option>
        <option>His Power Displayed</option>
        <option>The Fugitives</option>
        <option>Line Dance</option>
        <option>Mises Society</option>
        <option>MuKappa</option>
        <option>Order of St. George</option>
        <option>Pan-Asian Association</option>
        <option>Philosophy Society</option>
        <option>Swing Dance</option>
        <option>Survivor</option>
        <option>Turning Point USA</option>
        <option>Urban Gaming</option>
        <option>Wolverine Broadcast Network</option>
        <option>Young Americans for Freedom</option>
        <option>Young Women for America</option>
        <option>Alpha Kappa Delta</option>
        <option>Alpha Mu Gamma</option>
        <option>Beta Beta Beta</option>
        <option>Crimson & White Society</option>
        <option>Crown & Sceptre</option>
        <option>Delta Mu Delta</option>
        <option>Kappa Delta Pi</option>
        <option>Kappa Mu Epsilon</option>
        <option>Kemikos</option>
        <option>Lambda Epsilon Delta</option>
        <option>Lambda Iota Tau</option>
        <option>Mortar Board</option>
        <option>Omicron Delta Epsilon</option>
        <option>Omicron Delta Kappa</option>
        <option>Phi Alpha Theta</option>
        <option>Phi Sigma Tau</option>
        <option>Pi Gamma Mu</option>
        <option>Pi Sigma Alpha</option>
        <option>Psi Chi</option>
        <option>Roundtable</option>
        <option>Sigma Pi Sigma</option>
        <option>Tau Alpha Pi</option>
        <option>Theta Alpha Kappa</option>
        <option>Alpha Psi Omega</option>
      </select>

      <button onClick={handleEnter}>Enter</button>

      <br /><br />

      <label>Meeting Days:</label><br />
      {[
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday",
      ].map((day) => (
        <label key={day} style={{ marginRight: "8px" }}>
          <input
            type="checkbox"
            checked={meetingDays.includes(day)}
            onChange={() => toggleDay(day)}
          />
          {day}
        </label>
      ))}

      <br /><br />

      <label>Start Time:</label>
      <input
        type="time"
        value={startTime}
        onChange={(e) => setStartTime(e.target.value)}
      />

      <label>End Time:</label>
      <input
        type="time"
        value={endTime}
        onChange={(e) => setEndTime(e.target.value)}
      />

      <ul>
        {clubs.map((club, i) => (
          <li key={i}>
            <strong>{club.name}</strong><br />
            {club.days.join(", ")}<br />
            {club.startTime} – {club.endTime}
            <br />
            <button onClick={() => deleteClub(club.name)}>Delete</button>
          </li>
        ))}
      </ul>

      <button onClick={saveClubs}>SAVE</button>
    </div>
  );
}
``