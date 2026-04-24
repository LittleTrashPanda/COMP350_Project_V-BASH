//import "./LoginPage.css"; //To be imported once I understand how to do css
import { useState } from "react"
import { auth } from "../firebase/firebase";
import { signInWithEmailAndPassword } from "firebase/auth";
import { useNavigate } from "react-router-dom";

export default function LoginPage() {
  const [email, setEmail] = useState(""); //const for the email block
  const [password, setPassword] = useState(""); //const for the password block
  const navigate = useNavigate();
  const [error, setError] = useState("");

  const handleLogin = async () => {
    setError(""); //sets errors to empty (clears if there were any)

    try {
      await signInWithEmailAndPassword(auth, email, password); //prompts firebase to see if the username and password is valid
    } catch (err) {
      console.error(err)
      setError("Invalid username or password. Check your email or password and try again."); //something went wrong when prompting firebase, so invalid login attempt
        return;
    }

    const temp = await fetch("/loadUser", {
          method: "POST",
          headers: { "Content-Type": "text/plain" },
          body: email
      });

      navigate("/profile"); //if yes, go ahead in to profile page
  };

  return (
    <div>
      <h1>Login</h1>

      {error && <p style={{ color: "red" }}>{error}</p>}

      <input //input field for email
        type="email"
        placeholder="Email"
        onChange={(e) => setEmail(e.target.value)}
      />

      <input //input field for password
        type="password"
        placeholder="Password"
        onChange={(e) => setPassword(e.target.value)}
      />

      <button onClick={handleLogin}>Log In</button>
      <button onClick={() => navigate("/signup")}>
        Create Account
      </button>
    </div>
  );
}
