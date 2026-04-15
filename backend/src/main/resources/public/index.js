/* Document Creation */
document.addEventListener("DOMContentLoaded", async () => {
    /* Event Listeners */
	document.getElementById("searchButton").addEventListener("click", searchCourses);
	document.getElementById("semesterButton").addEventListener("click", setSemester);

    const scheduleRes = await fetch("/loadCalendar");
    scheduledCourses = await scheduleRes.json();
});

let scheduledCourses = [];

/* Set Semester */
async function setSemester() {
    const semester = document.getElementById("semester").value;
    await fetch("/setCurrentSemester", { method: "POST", headers: { "Content-Type": "text/plain" }, body: semester });
    resetResults();
}

async function resetResults() {
    const list = document.getElementById("resultingCourses");
    list.innerHTML = "";
}

/* Display Courses */
async function searchCourses() {
    // Key Search Terms
    const keySearchTerms = document.getElementById("keySearchTerms").value;

    // Filter Values
    const department = document.getElementById("department").value;
    const courseCode = document.getElementById("courseCode").value;
    const professor = document.getElementById("professor").value;
    const credits = document.getElementById("credits").value;

    let days = 1;
    const dayCheckboxes = document.querySelectorAll("#day input[type='checkbox']");
    dayCheckboxes.forEach(cb => { if (cb.checked) { days *= parseInt(cb.value); } });

    const startTime = document.getElementById("startTimes").value;
    const startTimes = [startTime, startTime, startTime, startTime, startTime]
    const endTime = document.getElementById("endTimes").value
    const duration = [endTime - startTime, endTime - startTime, endTime - startTime, endTime - startTime, endTime - startTime];

    // Create Filter
    const filter = { department, courseCode, professor, credits, days, startTimes, duration };

    // Send Key Search Terms
    await fetch("/keySearchTerms", { method: "POST", headers: { "Content-Type": "text/plain" }, body: keySearchTerms });

    // Send Filter
    await fetch("/setFilters", { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(filter) });

    // Fetch Results
    const res = await fetch("/search");
    const courses = await res.json();

    // Render Results
    resetResults();
    const list = document.getElementById("resultingCourses");

    for (const course of courses) {
        // Create List Entry
        const li = document.createElement("li");
            const courseContent = document.createElement("div");
                // Course Name
                const courseID = document.createElement("p");
                courseID.textContent = course.courseName + " - " + course.courseCode;
                courseContent.appendChild(courseID);

                // Course Specifics
                const courseSpecifics = document.createElement("p");
                courseSpecifics.textContent = course.department.toString() + " " + course.professors + " Credits: " + course.credits + " Time: ";

                // Determining What Time Range to Show
                var tempStartTime = 0; var tempEndTime = 0;
                if (course.days %  2 == 0) { tempStartTime = course.startTimes[0]; tempEndTime = course.duration[0] + tempStartTime; courseSpecifics.textContent += "M"; }
                if (course.days %  3 == 0) { tempStartTime = course.startTimes[1]; tempEndTime = course.duration[1] + tempStartTime; courseSpecifics.textContent += "T"; }
                if (course.days %  5 == 0) { tempStartTime = course.startTimes[2]; tempEndTime = course.duration[2] + tempStartTime; courseSpecifics.textContent += "W"; }
                if (course.days %  7 == 0) { tempStartTime = course.startTimes[3]; tempEndTime = course.duration[3] + tempStartTime; courseSpecifics.textContent += "R"; }
                if (course.days % 11 == 0) { tempStartTime = course.startTimes[4]; tempEndTime = course.duration[4] + tempStartTime; courseSpecifics.textContent += "F"; }
                courseSpecifics.textContent += " " + Math.trunc(tempStartTime / 60) + ":" + (tempStartTime % 60).toLocaleString("en-US", { minimumIntegerDigits: 2 });
                courseSpecifics.textContent += " - " + Math.trunc(tempEndTime / 60) + ":" + (tempEndTime % 60).toLocaleString("en-US", { minimumIntegerDigits: 2 });

                courseContent.appendChild(courseSpecifics);

                // Add Course Button
                const addButton = document.createElement("button");
                addButton.textContent = "Add to Schedule";
                addButton.onclick = () => addCourse(course);
                courseContent.appendChild(addButton);

                const isInSchedule = scheduledCourses.some(sc => sc.courseCode === course.courseCode && sc.courseName === course.courseName);

                // Remove Course Button
            if (isInSchedule) {
                const removeButton = document.createElement("button");
                removeButton.textContent = "Remove from Schedule";
                removeButton.onclick = () => removeCourse(course);
                courseContent.appendChild(removeButton);
            }

            courseContent.classList.add("courseListItemContent");
            li.appendChild(courseContent);

        li.classList.add("courseListItem");
        list.appendChild(li);
    }
}

function addCourse(course) {
    fetch("/addCourse", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(course)
    })
    .then(res => {
            if (!res.ok) {
                 throw new Error("Failed to replace course: " + res.status);
            }
                 return res.json();
    })
    .then(result => {

        if(result.conflicts && result.conflicts.length > 0){

            let msg = "This course conflicts with these courses: \n" +
                       result.conflicts.map(c => c.courseName).join("\n")+
                            "\nWould you like to replace them? This will replace ALL conflicts."

            if(confirm(msg)){
                replaceCourse(course)
            }

        }else{
            alert(result.message);
        }
    });
}

function removeCourse(course) {
    if (!confirm("Do you want to remove this course?")) {
        console.log("Deletion cancelled");
        return;
    }

    fetch("/removeCourse", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(course)
    })
    .then(res => res.json())
    .then(result => {
        alert(result.message);

        if (result.success) {
            window.location.reload();
        }
    });
}

function replaceCourse(course){
    fetch("/replaceCourse", {
        method: "POST",
        headers: { "Content-Type": "application/json"},
        body: JSON.stringify(course)
    })
    .then(res => {
        if (!res.ok) {
             throw new Error("Failed to replace course: " + res.status);
        }
             return res.json();
    })
    .then(result => {
        alert(result.message);
    });
}





