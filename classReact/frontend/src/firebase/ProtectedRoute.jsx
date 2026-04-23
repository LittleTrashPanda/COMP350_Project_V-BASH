import { auth } from "../firebase/firebase";
import { onAuthStateChanged } from "firebase/auth";
import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";

export default function ProtectedRoute({ children }) {
  const [user, setUser] = useState(undefined); //const for the user (if any)

  useEffect(() => {
    return onAuthStateChanged(auth, (u) => setUser(u)); //listens for login/logout, sets the const with the user if there is one
  }, []);

  if (user === undefined) return <div>Loading...</div>; // === is == for js
  if (!user) return <Navigate to="/login" />; //there is not an account logged in, so user gets bounced to LoginPage

  return children; //show the page trying to be accessed
}