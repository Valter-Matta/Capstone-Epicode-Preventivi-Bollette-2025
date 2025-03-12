import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Provider } from "react-redux";
import "./App.css";
import Home from "./components/Home";
import Login from "./components/Login";

import QuoteForm from "./components/QuoteForm";
import BillResultsPage from "./components/BillResultsPAge";
import store from "./redux/store";
import "boxicons";
import NavBar from "./components/NavBar";
import Register from "./components/Register";

function App() {
	return (
		<Provider store={store}>
			<BrowserRouter>
				<div className="app-container">
					<NavBar />
					<Routes>
						<Route path="/" element={<Home />} />
						<Route path="/login" element={<Login />} />
						<Route path="/register" element={<Register />} />
						<Route path="/upload-bill" element={<QuoteForm />} />
						<Route path="/risultati-bolletta" element={<BillResultsPage />} />
					</Routes>
				</div>
			</BrowserRouter>
		</Provider>
	);
}

export default App;
