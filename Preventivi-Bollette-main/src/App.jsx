import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Provider } from "react-redux";
import { useEffect } from "react";
import { useDispatch } from "react-redux";

import Home from "./components/Home";
import Login from "./components/Login";
import Contact from "./components/Contact";
import QuoteForm from "./components/QuoteForm";
import BillResultsPage from "./components/BillResultsPage";
import store from "./redux/store";
import "boxicons";
import NavBar from "./components/NavBar";
import Register from "./components/Register";
import Footer from "./components/Footer";
import SpreadResultCard from "./components/SpreadResultCard";
import { setUserFromStorage } from "./redux/actions";

function App() {
	const dispatch = useDispatch();

	useEffect(() => {
		dispatch(setUserFromStorage()); // dati da local storage
	}, [dispatch]);

	return (
		<Provider store={store}>
			<BrowserRouter>
				<NavBar />
				<Routes>
					<Route path="/" element={<Home />} />
					<Route path="/contatti" element={<Contact />} />
					<Route path="/login" element={<Login />} />
					<Route path="/register" element={<Register />} />
					<Route path="/upload-bill" element={<QuoteForm />} />
					<Route path="/risultati-bolletta" element={<BillResultsPage />} />
					<Route path="/risultati-spread" element={<SpreadResultCard />} />
				</Routes>
				<Footer />
			</BrowserRouter>
		</Provider>
	);
}

export default App;
