/* Document Creation */
document.addEventListener("DOMContentLoaded", async () => {
    loadSchedule();
});

/* Display Courses */
async function loadSchedule() {
    const res = await fetch("/calendar");
    const scheduledCourses = await res.json();

    const calendarDays = document.getElementById("calendar").children;

    for (const course of scheduledCourses) {
        console.log(course);

        // Monday
        if (course.days % 2 == 0) {
            const block = document.createElement("div");
            const courseTitle = document.createElement("p");
            courseTitle.textContent = course.courseName;
            courseTitle.classList.add("dayCourseTitle");
            block.appendChild(courseTitle);

            const removeButton = document.createElement("button");
            removeButton.textContent = "Remove";
            removeButton.classList.add("removeButton");
            removeButton.onclick = () => removeCourse(course);
            block.appendChild(removeButton);

            block.style.top = ((course.startTimes[0] - 8 * 60) / (13 * 60)) * 100 + "%";
            block.style.height = (course.duration[0] / (13 * 60)) * 100 + "%";
            block.classList.add("dayCourse");

            calendarDays[0].children[1].appendChild(block);
        }

        // Tuesday
        if (course.days % 3 == 0) {
            const block = document.createElement("div");
            const courseTitle = document.createElement("p");
            courseTitle.textContent = course.courseName;
            courseTitle.classList.add("dayCourseTitle");
            block.appendChild(courseTitle);

            const removeButton = document.createElement("button");
            removeButton.textContent = "Remove";
            removeButton.classList.add("removeButton");
            removeButton.onclick = () => removeCourse(course);
            block.appendChild(removeButton);

            block.style.top = ((course.startTimes[1] - 8 * 60) / (13 * 60)) * 100 + "%";
            block.style.height = (course.duration[1] / (13 * 60)) * 100 + "%";
            block.classList.add("dayCourse");

            calendarDays[1].children[1].appendChild(block);
        }

        // Wednesday
        if (course.days % 5 == 0) {
            const block = document.createElement("div");
            const courseTitle = document.createElement("p");
            courseTitle.textContent = course.courseName;
            courseTitle.classList.add("dayCourseTitle");
            block.appendChild(courseTitle);

            const removeButton = document.createElement("button");
            removeButton.textContent = "Remove";
            removeButton.classList.add("removeButton");
            removeButton.onclick = () => removeCourse(course);
            block.appendChild(removeButton);

            block.style.top = ((course.startTimes[2] - 8 * 60) / (13 * 60)) * 100 + "%";
            block.style.height = (course.duration[2] / (13 * 60)) * 100 + "%";
            block.classList.add("dayCourse");

            calendarDays[2].children[1].appendChild(block);
        }

        // Thursday
        if (course.days % 7 == 0) {
            const block = document.createElement("div");
            const courseTitle = document.createElement("p");
            courseTitle.textContent = course.courseName;
            courseTitle.classList.add("dayCourseTitle");
            block.appendChild(courseTitle);

            const removeButton = document.createElement("button");
            removeButton.textContent = "Remove";
            removeButton.classList.add("removeButton");
            removeButton.onclick = () => removeCourse(course);
            block.appendChild(removeButton);

            block.style.top = ((course.startTimes[3] - 8 * 60) / (13 * 60)) * 100 + "%";
            block.style.height = (course.duration[3] / (13 * 60)) * 100 + "%";
            block.classList.add("dayCourse");

            calendarDays[3].children[1].appendChild(block);
        }

        // Friday
        if (course.days % 11 == 0) {
            const block = document.createElement("div");
            const courseTitle = document.createElement("p");
            courseTitle.textContent = course.courseName;
            courseTitle.classList.add("dayCourseTitle");
            block.appendChild(courseTitle);

            const removeButton = document.createElement("button");
            removeButton.textContent = "Remove";
            removeButton.classList.add("removeButton");
            removeButton.onclick = () => removeCourse(course);
            block.appendChild(removeButton);

            block.style.top = ((course.startTimes[4] - 8 * 60) / (13 * 60)) * 100 + "%";
            block.style.height = (course.duration[4] / (13 * 60)) * 100 + "%";
            block.classList.add("dayCourse");

            calendarDays[4].children[1].appendChild(block);
        }
    }
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
