/* Document Creation */
document.addEventListener("DOMContentLoaded", async () => {
    /* Event Listeners */
	document.getElementById("searchButton").addEventListener("click", searchCourses);

    const scheduleRes = await fetch("/calendar");
    scheduledCourses = await scheduleRes.json();
});

let scheduledCourses = [];

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

            const addButton = document.createElement("button");
            addButton.textContent = "Add to Schedule";
            addButton.onclick = () => addCourse(course);
            courseContent.appendChild(addButton);

            const isInSchedule = scheduledCourses.some(sc =>
                        sc.courseCode === course.courseCode &&
                        sc.courseName === course.courseName
                    );

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
    .then(res => res.json())
    .then(result => {

        if(result.msg === "This course conflicts with an existing course in your schedule."){

            let msg = "This course conflicts with these courses: \n" + result.conflicts.join("\n")+
                            "Would you like to replace them? This will replace ALL conflicts."

            if(confirm(msg)){
                replaceCourse(course)
            }
        }

        if (result.success) {
            window.location.href = "calendar.html";
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
        body: JSON.stringify(course: course)
    })
    .then(res => res.json())
    .then(result => {
        alert(result.message);

        if(result.success){
            window.location.href = "calendar.html";
        }
    });
}





