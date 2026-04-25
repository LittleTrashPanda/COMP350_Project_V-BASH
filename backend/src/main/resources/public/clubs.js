document.addEventListener("DOMContentLoaded", () => {
const input = document.getElementById("clubinput");
const dropdown = document.getElementById("clubDropdown");
const button = document.getElementById("enterBtn");
const list = document.getElementById("list");
const save = document.getElementById("saveClubsBtn");


button.addEventListener("click", addClub);
save.addEventListener("click", saveClubs)

input.addEventListener("keydown", (event) => {
if(event.key === "Enter"){
         addClub();
         }
         });

         function addClub(){
         let name = input.value.trim();
         if(!name){
         name = dropdown.value.trim();}

         if(!name) return;

         const existingClubs = list.querySelectorAll("li");
         for(const item of existingClubs){
         if(item.firstChild.nodeValue.trim() === name){
                   alert("That club is already added.");
                   return;
         }
         }

         const li = document.createElement("li");
         li.textContent = name + " ";
         const delBtn = document.createElement("button");
         delBtn.textContent = "Delete";

         delBtn.onclick = () => deleteClub(li);

         li.appendChild(delBtn);
         list.appendChild(li);


         input.value = "";
         dropdown.selectedIndex = 0;
         }


         async function deleteClub(li){
         li.remove();
         }

         async function saveClubs(){
            const clubs = [];

            for (const li of list.querySelectorAll("li")){
                clubs.push(li.firstChild.nodeValue.trim());}

            await fetch("/saveClubs", {
                method: "POST",
                headers: {
                        "Content-Type": "application/json"},
                body: JSON.stringify(clubs)
                });
                alert("Clubs saved.");

         }
});
