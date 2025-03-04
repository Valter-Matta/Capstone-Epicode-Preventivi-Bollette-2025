export const GET_USER = "GET_USER";

export const getmyProfile = () => {
	return async (dispatch, getState) => {
		const state = getState();
		try {
			const call = await fetch(myProfile, {
				headers: {
					Authorization: `Bearer ${state.profileKey.key}`,
				},
			});
			if (call.ok) {
				const response = await call.json();
				console.log(response);
				dispatch({ type: GET_USER, payload: response });
			} else {
				console.log(`Error: ${call.status} - ${call.statusText}`);
			}
		} catch (error) {
			console.log(error);
		}
	};
};
