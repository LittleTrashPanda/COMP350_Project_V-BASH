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
      "/keySearchTerm": "http://localhost:7000",
      "/search/filter": "http://localhost:7000",
      "/user/calendar": "http://localhost:7000",
      "/semester": "http://localhost:7000",
      "/course": "http://localhost:7000",
      "/schedule": "http://localhost:7000",
      "/schedule/name": "http://localhost:7000",
      "/schedule/save": "http://localhost:7000",
      "/schedule/current": "http://localhost:7000",
      "/userData": "http://localhost:7000",
      "/user": "http://localhost:7000",
      "/takenCourse": "http://localhost:7000",
      "/major": "http://localhost:7000",
      "/majorRequirement": "http://localhost:7000"
    }
  }
});
