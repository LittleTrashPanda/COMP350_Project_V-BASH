import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/keySearchTerms": "http://localhost:7000",
      "/setFilters": "http://localhost:7000",
      "/search": "http://localhost:7000",
      "/addCourse": "http://localhost:7000",
      "/removeCourse": "http://localhost:7000",
      "/replaceCourse": "http://localhost:7000",
      "/loadCalendar": "http://localhost:7000",
      "/saveSchedule": "http://localhost:7000",
      "/loadSchedule": "http://localhost:7000",
      "/nameCurrentSchedule": "http://localhost:7000",
      "/resetSchedule": "http://localhost:7000"
    }
  }
})
