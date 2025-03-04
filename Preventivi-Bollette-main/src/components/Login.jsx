import { useState } from "react";
import "../css-components/Login.css";

function Login() {
	const [isRegistering, setIsRegistering] = useState(false);

	return (
		<div className="container-login">
			<div className="card-login">
				<div className="card-header">
					<h2>{isRegistering ? "Registrazione" : "Accesso"}</h2>
				</div>
				<div className="card-content">
					<form className="form">
						{isRegistering && (
							<>
								<input
									type="text"
									placeholder="Nome"
									required
									className="input"
								/>
								<input
									type="text"
									placeholder="Cognome"
									required
									className="input"
								/>
							</>
						)}

						<input
							type="email"
							placeholder="Email"
							required
							className="input"
						/>
						<input
							type="password"
							placeholder="Password"
							required
							className="input"
						/>

						{isRegistering && (
							<input
								type="password"
								placeholder="Conferma Password"
								required
								className="input"
							/>
						)}

						<button type="submit" className="button">
							{isRegistering ? "Registrati" : "Accedi"}
						</button>
					</form>
					<div className="toggle">
						{isRegistering ? (
							<p>
								Hai già un account?{" "}
								<button
									type="button"
									className="link"
									onClick={() => setIsRegistering(false)}
								>
									Accedi
								</button>
							</p>
						) : (
							<p>
								Non hai un account?{" "}
								<button
									type="button"
									className="link"
									onClick={() => setIsRegistering(true)}
								>
									Registrati
								</button>
							</p>
						)}
					</div>
				</div>
			</div>
		</div>
	);
}

export default Login;
