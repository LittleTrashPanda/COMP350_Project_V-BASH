function App() {
  const [filters, setFilters] = React.useState({
    dept: "",
    professor: "",
    credits: "",
    courseCode: "",
    selectedDays: [],
    time: ""
  });

  const [keySearchTerms, setKeySearchTerms] = React.useState("");
  const [courses, setCourses] = React.useState([]);
  const [schedule, setSchedule] = React.useState([]);

  React.useEffect(() => {
    fetch("/calendar")
      .then(res => res.json())
      .then(setSchedule);
  }, []);

  async function search() {
    await fetch("/keySearchTerms", {
      method: "POST",
      headers: { "Content-Type": "text/plain" },
      body: keySearchTerms.trim()
    });

    await fetch("/setFilters", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(filters)
    });

    const res = await fetch("/search");
    const data = await res.json();
    setCourses(data);
  }

  return (
    <div style={{ padding: "20px" }}>
      <h1>VBASH Course Search</h1>

      <SearchBar
        filters={filters}
        setFilters={setFilters}
        keySearchTerms={keySearchTerms}
        setKeySearchTerms={setKeySearchTerms}
        onSearch={search}
      />

      <CourseList
        courses={courses}
        schedule={schedule}
        setSchedule={setSchedule}
      />
    </div>
  );
}

ReactDOM.createRoot(document.getElementById("root")).render(<App />);
