import { useState } from "react";
import { useDispatch } from "react-redux";
import { loginUser } from "../redux/actions";
import { useNavigate } from "react-router-dom";
import "../css-components/Login.css";

function Login() {
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const dispatch = useDispatch();
	const navigate = useNavigate();

	const handleLogin = async e => {
		e.preventDefault();
		await dispatch(loginUser(email, password));
		navigate("/");
	};

	return (
		<div className="container-login">
			<div className="card-login">
				<h2>Accesso</h2>
				<form onSubmit={handleLogin}>
					<input
						type="email"
						placeholder="Email"
						required
						value={email}
						onChange={e => setEmail(e.target.value)}
					/>
					<input
						type="password"
						placeholder="Password"
						required
						value={password}
						onChange={e => setPassword(e.target.value)}
					/>
					<button type="submit">Accedi</button>
				</form>
				<p>
					Non hai un account?{" "}
					<span onClick={() => navigate("/register")} className="register-link">
						Registrati
					</span>
				</p>
			</div>
		</div>
	);
}

export default Login;
