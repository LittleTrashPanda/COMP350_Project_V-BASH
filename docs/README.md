# Installation notes
COMP350 - V.BASH
Sheldon Deal, Alyssa Hoover

## Startup
To initialize this project, navigate to the Main class in backend/src/main/java/edu.gcc.VBASH/Main.java and click the run button.

Once the Javalin header has appeared in the terminal, open a web browser of your choice and go to http://localhost:7000/, this is the 'Search' page.

Click on the 'Calendar' link in the top left corner, this will take you to the 'Calendar' page, where the 'Home' link will take you back to the 'Search' page.

---

## 🚀 MVP Requirements

### Searching for Courses
- [X] **R1 – Viewing Courses**  
  As a user, I should be able to search for any of the available courses for the desired semester.

- [X] **R2 – Course Filtering**  
  As a user, I should be able to restrict the results presented based on qualities including, but not limited to:
    - [X] R2a - Course Code or Department
    - [X] R2b - Key Word in title or description
    - [X] R2c - Professor Name
    - [X] R2d - Number of Credits
    - [X] R2e - Day and Time Range.

- [X] **R3 – Non-Interference Filtering**  
  As a user, I should be able to use any combination of filters in a given search.

On the 'Search' page, type in key words you wish to search for in the input field next to the red 'Search' button; the additional filters are all labeled for what they filter by and may be used in any combination. Once the key words and filter have been decided, click the aforementioned red 'Search' button to retrieve results.

---

- [X] **R4 – Adding Courses**  
  As a user, I should be able to add courses to my schedule.

- [X] **R5 – Removing Courses**  
  As a user, I should be able to remove courses from my schedule.

- [X] **R6 – Course Conflicts (Notification)**  
  As a user, I should be notified if I attempt to add a course that has a time conflict with an existing class in my schedule, and it should not be added to my schedule.

- [X] **R7 – Course Conflicts (Resolution)**  
  As a user, I should be allowed to replace existing classes on my planned schedule when I attempt to add a class that would create a time conflict.

For each item in the 'Search' result, there will be an option to add the course to your schedule, and if it is already in your schedule, a button to remove it. When attempting to add a class that conflicts with your existing schedule, you will be notified of which classes the course conflicts with and an option to either cancel the addition or go ahead and replace those courses.

---

- [X] **R8 – Calendar View**  
  As a user, I should be able to view a calendar representation of my schedule.

- [X] **R9 – Calendar Representation**  
  As a user, I should be shown the duration of my classes and gaps in-between in a weekly format on my calendar.

On the 'Calendar' page, there are columns representing their respective days alongside time markers on the left hand side; courses in the current schedule, and gaps in between, will appear with their lengths shown in visual representation.

---

- [X] **R10 – Saving Schedules**  
  As a user, I should be able to save a schedule.

- [X] **R11 – Loading Schedules**  
  As a user, I should be able to load a saved schedule.

On the 'Calendar' page, there are input fields and buttons corresponding to 'Save' and 'Load'. Inputting a name into the 'Save' field and clicking the 'Save' button will store the current schedule under that name; inputting a name into the 'Load' field and clicking the 'Load' button will attempt to load a schedule saved under that name if it exists.

---

- [X] **R12 – Bugs**  
  As a user, I should be able to utilize every aspect of the software without it crashing.

As far as we are aware, there are no bugs with interacting with our project.

---

- [X] **R13 – User Interface**  
  As a user, I should be able to interact with all intended functions of the software via a graphical user interface.

All of this has been done through a graphical interface.

---

We have not, as of yet, fully implemented any features that went beyond the MVP.