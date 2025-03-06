import { REGISTER_USER, LOGIN_SUCCESS, LOGOUT } from "./actions";

const initialState = {
	user: null,
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
			localStorage.setItem("token", action.payload);
			return {
				...state,
				token: action.payload,
			};
		case LOGOUT:
			localStorage.removeItem("token");
			return {
				...state,
				token: null,
			};

		default:
			return state;
	}
};

export default userReducer;
