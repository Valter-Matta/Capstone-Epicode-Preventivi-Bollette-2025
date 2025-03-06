export const REGISTER_USER = "REGISTER_USER";
export const LOGIN_SUCCESS = "LOGIN_SUCCESS";
export const LOGOUT = "LOGOUT";

const API_BASE_URL = "http://localhost:8080/api/auth";

// REGISTRAZIONE
export const registerUser = userData => {
	return async dispatch => {
		try {
			const response = await fetch(`${API_BASE_URL}/register`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(userData),
			});

			if (!response.ok) {
				throw new Error("Errore nella registrazione");
			}

			const result = await response.json();
			dispatch({ type: REGISTER_USER, payload: result });

			alert("Registrazione completata! Ora puoi accedere.");
		} catch (error) {
			console.error("Errore nella richiesta:", error);
			alert(error.message);
		}
	};
};

// LOGIN
export const loginUser = (email, password) => {
	return async dispatch => {
		try {
			const response = await fetch(`${API_BASE_URL}/login`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ email, password }),
			});

			if (!response.ok) {
				throw new Error("Credenziali non valide");
			}

			const data = await response.json();
			const token = data.token;

			dispatch({ type: LOGIN_SUCCESS, payload: token });
		} catch (error) {
			console.error("Errore durante il login:", error);
			alert(error.message);
		}
	};
};

// LOGOUT
export const logoutUser = () => {
	return dispatch => {
		dispatch({ type: LOGOUT });
	};
};
