// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyCIebxl-8ixlSe90aU9MYEDFpCRzOFdexY",
  authDomain: "v-bash.firebaseapp.com",
  projectId: "v-bash",
  storageBucket: "v-bash.firebasestorage.app",
  messagingSenderId: "265545935719",
  appId: "1:265545935719:web:e5f52551864b0ba509ddc2"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);

export {app, auth};