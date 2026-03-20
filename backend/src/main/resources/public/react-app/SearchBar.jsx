function SearchBar({ filters, setFilters, keySearchTerms, setKeySearchTerms, onSearch }) {
  function update(field, value) {
    setFilters(prev => ({ ...prev, [field]: value }));
  }

  function toggleDay(day) {
    setFilters(prev => {
      const exists = prev.selectedDays.includes(day);
      return {
        ...prev,
        selectedDays: exists
          ? prev.selectedDays.filter(d => d !== day)
          : [...prev.selectedDays, day]
      };
    });
  }

  return (
    <div style={{ marginBottom: "20px" }}>
      <input
        placeholder="Search keywords"
        value={keySearchTerms}
        onChange={e => setKeySearchTerms(e.target.value)}
      />

      <select value={filters.dept} onChange={e => update("dept", e.target.value)}>
        <option value = "">DEPT</option>
            <option value = "abrd">ABRD</option>
            <option value = "acct">ACCT</option>
            <option value = "art">ART</option>
            <option value = "astr">ASTR</option>
            <option value = "bibl">BIBL</option>
            <option value = "biol">BIOL</option>
            <option value = "chem">CHEM</option>
            <option value = "comm">COMM</option>
            <option value = "comp">COMP</option>
            <option value = "desi">DESI</option>
            <option value = "econ">ECON</option>
            <option value = "educ">EDUC</option>
            <option value = "elee">ELEE</option>
            <option value = "engl">ENGL</option>
            <option value = "engr">ENGR</option>
            <option value = "entr">ENTR</option>
            <option value = "exer">EXER</option>
            <option value = "fnce">FNCE</option>
            <option value = "fren">FREN</option>
            <option value = "gobl">GOBL</option>
            <option value = "grek">GREK</option>
            <option value = "hebr">HEBR</option>
            <option value = "hist">HIST</option>
            <option value = "huma">HUMA</option>
            <option value = "inbs">INBS</option>
            <option value = "mark">MARK</option>
            <option value = "math">MATH</option>
            <option value = "mece">MECE</option>
            <option value = "mngt">MNGT</option>
            <option value = "muse">MUSE</option>
            <option value = "musi">MUSI</option>
            <option value = "nurs">NURS</option>
            <option value = "phil">PHIL</option>
            <option value = "phye">PHYE</option>
            <option value = "phys">PHYS</option>
            <option value = "pols">POLS</option>
            <option value = "psyc">PSYC</option>
            <option value = "robo">ROBO</option>
            <option value = "scic">SCIC</option>
            <option value = "sedu">SEDU</option>
            <option value = "soci">SOCI</option>
            <option value = "socw">SOCW</option>
            <option value = "span">SPAN</option>
            <option value = "ssft">SSFT</option>
            <option value = "stat">STAT</option>
            <option value = "thea">THEA</option>
            <option value = "writ">WRIT</option>
        <!-- Add more as needed -->
      </select>

      <input
        placeholder="Professor"
        value={filters.professor}
        onChange={e => update("professor", e.target.value)}
      />

      <input
        placeholder="Course Code"
        value={filters.courseCode}
        onChange={e => update("courseCode", e.target.value)}
      />

      <select value={filters.credits} onChange={e => update("credits", e.target.value)}>
        <option value="">Credits</option>
        <option value="1">1</option>
        <option value="2">2</option>
        <option value="3">3</option>
      </select>


      <select value={filters.time} onChange={e => update("time", e.target.value)}>
        <option value = "">CLASS TIME</option>
            <option value = "08:00-08:50">8:00-8:50 AM</option>
            <option value = "09:00-09:50">9:00-9:50 AM</option>
            <option value = "10:00-10:50">10:00-10:50 AM</option>
            <option value = "12:00-12:50">12:00-12:50 PM</option>
            <option value = "13:00-13:50">1:00-1:50 PM</option>
            <option value = "14:00-14:50">2:00-2:50 PM</option>
            <option value = "15:00-15:50">3:00-3:50 PM</option>
            <option value = "16:00-16:50">4:00-4:50 PM</option>
            <option value = "17:00-17:50">5:00-5:50 PM</option>
            <option value = "18:00-18:50">6:00-6:50 PM</option>
      </select>

      <div>
        <label><input type="checkbox" onChange={() => toggleDay( 2)} /> M</label>
        <label><input type="checkbox" onChange={() => toggleDay( 3)} /> T</label>
        <label><input type="checkbox" onChange={() => toggleDay( 5)} /> W</label>
        <label><input type="checkbox" onChange={() => toggleDay( 7)} /> R</label>
        <label><input type="checkbox" onChange={() => toggleDay(11)} /> F</label>
      </div>

      <button onClick={onSearch}>Search</button>
    </div>
  );
}
