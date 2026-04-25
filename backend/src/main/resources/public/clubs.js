document.addEventListener("DOMContentLoaded", () => {
const list = document.getElementById("list");

loadClubs();

const input = document.getElementById("clubinput");
const dropdown = document.getElementById("clubDropdown");
const button = document.getElementById("enterBtn");
const save = document.getElementById("saveClubsBtn");


button.addEventListener("click", addClub);
save.addEventListener("click", saveClubs);


input.addEventListener("keydown", (event) => {
if(event.key === "Enter"){
         addClub();
         }
         });

         function addClub(){
         let name = input.value.trim() || dropdown.value.trim();
         if(!name) return;

         const existingClubs = list.querySelectorAll("li");
         for(const item of existingClubs){
              if(item.firstChild.nodeValue.trim() === name){
                          alert("That club is already added.");
                          return;
              }
         }

         const days = getSelectedDays();
         const startTime = document.getElementById("startTime").value;
         const endTime = document.getElementById("endTime").value;

         if(days.length === 0 || !startTime || !endTime){
         alert("Please select meeting days and times.");
         return;
         }
         renderClub({ name, days, startTime, endTime });
         }


         async function deleteClub(li){
         li.remove();
         }

         async function saveClubs(){
            const clubs = [];

            for (const li of list.querySelectorAll("li")){
                clubs.push({
                name: li.dataset.name,
                days: JSON.parse(li.dataset.days),
                 startTime : li.dataset.startTime,
                 endTime : li.dataset.endTime

                });
                }

            await fetch("/saveClubs", {
                method: "POST",
                headers: {
                        "Content-Type": "application/json"},
                body: JSON.stringify(clubs)
                });
                alert("Clubs saved.");

         }

         async function loadClubs(){
         const response = await fetch("/loadClubs");
         const clubs = await response.json();

         for(const club of clubs){
            renderClub(club);}
         }

         function getSelectedDays(){
         const checked = document.querySelectorAll('input[type="checkbox"]:checked'
         );
         return Array.from(checked).map(cb =>cb.value)}

         function renderClub(club){
                  const li = document.createElement("li");
                  li.dataset.name = club.name;
                  li.dataset.days = JSON.stringify(club.days);
                  li.dataset.startTime = club.startTime;
                  li.dataset.endTime = club.endTime;

                  li.textContent = `${club.name} - ${club.days.join(", ")} ${club.startTime} - ${club.endTime} `;

                const delBtn = document.createElement("button");
                delBtn.textContent = "Delete";
                delBtn.onclick = () => deleteClub(li);

                li.appendChild(delBtn);
                list.appendChild(li);
         }
});
