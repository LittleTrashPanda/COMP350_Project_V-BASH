/* Document Creation */
document.addEventListener("DOMContentLoaded", async () => {
    loadProfile();
});

/* Display Proper User Data or Prompt */
async function loadProfile() {
    const login = document.getElementById("loginDiv");
    const profileData = document.getElementById("profileDataDiv");

    const fetchUserData = await fetch("/userData");
    const userData = await fetchUserData.json();

    if (userData.username == "default") { profileData.remove(); return; }
    else { login.remove(); }
}