import { GET_USER } from "./actions";

const initialState = { user: {} };

const userReducer = (state = initialState, action) => {
	switch (action.type) {
		case GET_USER:
			return {
				...state,
				users: action.payload,
			};
		default:
			return state;
	}
};

export default userReducer;
