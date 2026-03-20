function CourseList({ courses, schedule, setSchedule }) {
  async function addCourse(course) {
    const res = await fetch("/addCourse", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(course)
    });

    const result = await res.json();
    alert(result.message);
  }

  return (
    <ul>
      {courses.map(course => (
        <li key={course.courseCode}>
          <div>
            <p>{course.courseName} - {course.courseCode}</p>
            <p>{course.department} | Credits: {course.credits}</p>

            <button onClick={() => addCourse(course)}>Add</button>
          </div>
        </li>
      ))}
    </ul>
  );
}
