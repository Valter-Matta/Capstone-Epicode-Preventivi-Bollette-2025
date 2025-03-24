import { REGISTER_USER, LOGIN_SUCCESS, LOGOUT } from "./actions";

const initialState = {
	user: JSON.parse(localStorage.getItem("user")) || null,
	token: localStorage.getItem("token") || null,
};

const userReducer = (state = initialState, action) => {
	switch (action.type) {
		case REGISTER_USER:
			return {
				...state,
				user: action.payload,
			};
		case LOGIN_SUCCESS:
			localStorage.setItem("token", action.payload.token);
			localStorage.setItem("user", JSON.stringify(action.payload.user));
			return {
				...state,
				token: action.payload.token,
				user: action.payload.user,
			};

		case LOGOUT:
			localStorage.removeItem("token");
			localStorage.removeItem("user");
			return {
				...state,
				token: null,
				user: null,
			};

		default:
			return state;
	}
};

export default userReducer;
