/* Document Creation */
document.addEventListener("DOMContentLoaded", async () => {
	document.getElementById("semesterButton").addEventListener("click", setSemester);

    document.getElementById("saveSchedule").addEventListener("click", saveSchedule);
    document.getElementById("loadSchedule").addEventListener("click", loadSchedule);
    document.getElementById("deleteSchedule").addEventListener("click", deleteSchedule);
    document.getElementById("generatePDF").addEventListener("click", asPDF);
    loadCalendar();
});

/* Set Semester */
async function setSemester() {
    const semester = document.getElementById("semester").value;
    await fetch("/setCurrentSemester", { method: "POST", headers: { "Content-Type": "text/plain" }, body: semester });
    window.location.reload();
}


    // // Optional settings for the PDF layout


/* Display Courses */
async function loadCalendar() {
    const res = await fetch("/loadCalendar");
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

async function removeCourse(course) {
    if (!confirm("Do you want to remove this course?")) {
        console.log("Deletion cancelled");
        return;
    }

    fetch("/removeCourse", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
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

async function asPDF(){
    const initialize = document.getElementById("generatePDF").value;
    const courseTime = await fetch ("/getTimes", {method: "GET"})
    const times = await courseTime.text();
    const bodyText = document.getElementById('pdf-text');
    bodyText.innerText = times;
    const element = document.getElementById('pdf-save').innerHTML;
    const options = {
        margin:       10,
        filename:     'advisor-please-approve-this.pdf',
        image:        { type: 'jpeg', quality: 0.98 },
        html2canvas: { scale: 2 }, // Higher resolution
        jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' } // A4 page
    };

    html2pdf().set(options).from(element).save();

}

async function saveSchedule() {
    const saveName = document.getElementById("saveScheduleName").value;
    const resName = await fetch("/nameCurrentSchedule", { method: "POST", body: JSON.stringify(saveName) })
    const resSave = await fetch("/saveSchedule", { method: "POST" })
    const select = document.getElementById("loadScheduleName")
    // const addName = Option(saveName, saveName)
    // select.add(addName);
    select.add(new Option(JSON.stringify(saveName), "value1"));

}

async function loadSchedule() {
    const loadName = document.getElementById("loadScheduleName").value;

    const resLoad = await fetch("/loadSchedule", { method: "POST", body: JSON.stringify(loadName) })
    window.location.reload();
}
async function deleteSchedule(){
    const deletename = document.getElementById("deleteSchedule").value;
    const resDelete = await fetch("/deleteSchedule", {method:"POST"})
    window.location.reload();
}