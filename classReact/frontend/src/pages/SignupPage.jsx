import { useState } from "react";
import { auth } from "../firebase/firebase";
import { createUserWithEmailAndPassword } from "firebase/auth";
import { useNavigate } from "react-router-dom";

export default function SignupPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  const [error, setError] = useState("");

  const handleSignup = async () => {
    setError("");

    try {
      await createUserWithEmailAndPassword(auth, email, password);
    } catch (err) {
      console.error(err);

      switch (err.code) {
      case "auth/weak-password":
        setError("Password must be at least 6 characters.");
        break;
      case "auth/email-already-in-use":
        setError("This email is already registered.");
        break;
      case "auth/invalid-email":
        setError("Please enter a valid email address.");
        break;
      default:
        setError("Something went wrong. Please try again.");
      }

      return;
    }

    await fetch("/userCreation",{
        method: "POST",
        headers: { "Content-Type": "text/plain"},
        body: email
    });

    navigate("/profile"); // user is now logged in
  };

  return (
    <div>
      <h1>Create Account</h1>

      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <p className="password-rules">
        Password must be at least 6 characters long.
      </p>

      <button onClick={handleSignup}>Create Account</button>
    </div>
  );
}