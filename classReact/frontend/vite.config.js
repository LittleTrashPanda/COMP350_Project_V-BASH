import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      "/search": "http://localhost:7000",
      "/getTimes": "http://localhost:7000",
      "/keySearchTerms": "http://localhost:7000",
      "/setFilters": "http://localhost:7000",
      "/loadCalendar": "http://localhost:7000",
      "/setCurrentSemester": "http://localhost:7000",
      "/addCourse": "http://localhost:7000",
      "/removeCourse": "http://localhost:7000",
      "/replaceCourse": "http://localhost:7000",
      "/newSchedule": "http://localhost:7000",
      "/nameCurrentSchedule": "http://localhost:7000",
      "/saveSchedule": "http://localhost:7000",
      "/loadSchedule": "http://localhost:7000",
      "/deleteSchedule": "http://localhost:7000",
      "/userData": "http://localhost:7000",
      "/newUser": "http://localhost:7000",
      "/loadUser": "http://localhost:7000",
      "/takenCourse": "http://localhost:7000",
      "/notTakenCourse": "http://localhost:7000",
      "/takenCourses": "http://localhost:7000",
      "/major": "http://localhost:7000",
      "/majorRequirements": "http://localhost:7000"
    }
  }
});
