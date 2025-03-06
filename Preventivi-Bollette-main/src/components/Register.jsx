import { useState } from "react";
import { useDispatch } from "react-redux";
import { registerUser } from "../redux/actions";
import { useNavigate } from "react-router-dom";
import "../css-components/Register.css";

function Register() {
	const [name, setName] = useState("");
	const [surname, setSurname] = useState("");
	const [phone, setPhone] = useState("");
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [confirmPassword, setConfirmPassword] = useState("");

	const dispatch = useDispatch();
	const navigate = useNavigate();

	const handleRegister = async e => {
		e.preventDefault();
		if (password !== confirmPassword) {
			alert("Le password non coincidono!");
			return;
		}

		const userData = { name, surname, phone, email, password };
		await dispatch(registerUser(userData));
		navigate("/login");
	};

	return (
		<div className="register-container">
			<div className="register-card">
				<h2>Registrazione</h2>
				<form onSubmit={handleRegister}>
					<input
						type="text"
						placeholder="Nome"
						required
						value={name}
						onChange={e => setName(e.target.value)}
					/>
					<input
						type="text"
						placeholder="Cognome"
						required
						value={surname}
						onChange={e => setSurname(e.target.value)}
					/>
					<input
						type="tel"
						placeholder="Numero di telefono"
						required
						value={phone}
						onChange={e => setPhone(e.target.value)}
					/>
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
					<input
						type="password"
						placeholder="Conferma Password"
						required
						value={confirmPassword}
						onChange={e => setConfirmPassword(e.target.value)}
					/>
					<button type="submit">Registrati</button>
				</form>
				<p>
					Hai già un account?{" "}
					<span onClick={() => navigate("/login")} className="login-link">
						Accedi
					</span>
				</p>
			</div>
		</div>
	);
}

export default Register;
