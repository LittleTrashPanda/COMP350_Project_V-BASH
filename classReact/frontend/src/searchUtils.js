export async function fetchScheduledCourses() {
  const res = await fetch("/calendar");
  return await res.json();
}

export async function fetchKeySearchData() {
  const res = await fetch("/keySearchTerms");
  return await res.json();
}

export async function sendKeySearchTerms(terms) {
  await fetch("/keySearchTerms", {
    method: "POST",
    headers: { "Content-Type": "text/plain" },
    body: terms
  });
}

export async function sendFilters(filter) {
  await fetch("/setFilters", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(filter)
  });
}

export async function fetchSearchResults() {
  const res = await fetch("/search");
  return await res.json();
}

export async function addCourse(course) {
  const res = await fetch("/addCourse", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(course)
  });

  const result = await res.json();

  alert(result.message);

  return result;
}


export async function removeCourse(course) {
  const res = await fetch("/removeCourse", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(course)
  });
  return await res.json();
}

export async function replaceCourse(course) {
  const res = await fetch("/replaceCourse", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(course)
  });
  return await res.json();
}
