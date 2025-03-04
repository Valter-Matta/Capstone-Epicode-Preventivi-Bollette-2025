function Register() {
	return (
		<div className="register-container">
			<h2>Registrazione</h2>
			<form className="register-form">
				<label>Nome:</label>
				<input type="text" required />

				<label>Email:</label>
				<input type="email" required />

				<label>Password:</label>
				<input type="password" required />

				<button type="submit">Registrati</button>
			</form>
		</div>
	);
}

export default Register;
