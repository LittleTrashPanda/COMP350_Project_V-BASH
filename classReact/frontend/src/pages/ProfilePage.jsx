import { auth } from "../firebase/firebase";
import { signOut } from "firebase/auth";

export default function ProfilePage() {
    const user = auth.currentUser; //because of ProtectedRoute, no checks for null or unlogged users are needed
    const handleLogout = async () => {
      await signOut(auth);
    };


  return (
    <div style={{ padding: "20px" }}>
      <h1>Your Profile</h1>
      <p>Email: {user.email}</p>
      <p>Welcome to your profile page.</p>
      <button onClick={handleLogout}>Log Out</button>
    </div>
  );
}
