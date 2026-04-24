document.addEventListener("DOMContentLoaded", () => {
const input = document.getElementById("clubinput");
const dropdown = document.getElementById("clubDropdown");
const button = document.getElementById("enterBtn");
const list = document.getElementById("list");


button.addEventListener("click", addClub);

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

         delBtn.onclick = () => deleteClub(name, li);

         li.appendChild(delBtn);
         list.appendChild(li);


         input.value = "";
         dropdown.selectedIndex = 0;
         }


         async function deleteClub(name, li){
         await fetch('/items/${id}',{
            method: "DELETE"

         });
         li.remove();
         }
});
