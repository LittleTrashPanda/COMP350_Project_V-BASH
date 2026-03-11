/* Document Creation */
document.addEventListener("DOMContentLoaded", async () => {
    /* Event Listeners */
	document.getElementById("searchButton").addEventListener("click", searchCourses);
});

/* Display Courses */
async function searchCourses() {
    const keySearchTerms = document.getElementById("keySearchTerms").value.split();
    await fetch("/keySearchTerms", { method: "POST", body: keySearchTerms});

    const res = await fetch("/search")
    const courses = await res.json();

    const list = document.getElementById("resultingCourses");
    list.innerHTML = "";

    for (const course of courses) {
        const li = document.createElement("li");
        const courseContent = document.createElement("div");
            const courseID = document.createElement("p");
                courseID.textContent = course.courseName + " - " + course.courseCode;
                courseContent.appendChild(courseID);
            const courseSpecifics = document.createElement("p");
                courseSpecifics.textContent = course.department.toString() + " " + course.professors + " Credits: " + course.credits;
                courseContent.appendChild(courseSpecifics);
            courseContent.classList.add("courseListItemContent");
        li.appendChild(courseContent);
        li.classList.add("courseListItem");
        list.appendChild(li);
    }
}