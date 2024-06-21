import "./App.scss";
import Login from "./components/Login/Login";
import Navbar from "./components/Navbar/Navbar";
import Footer from "./components/Footer/Footer";
import { Routes, Route } from "react-router-dom";
import ProductDetail from "./components/ProductDetail/ProductDetail";
import ViewDashboard from "./components/Features/Admin/ViewDashboard";
import ViewManagerList from "./components/Features/Admin/ViewManagerList";
import "bootstrap/dist/css/bootstrap.min.css";
import Profile from "./components/Profile/Profile";
import ListProducts from "./components/Products/ListProducts";
import EditManager from "./components/Features/Admin/EditManager";
import Register from "./components/Register/Register";
import Admin from "./components/Features/Admin/Admin";
import Staff from "./components/Features/Staff/Staff";
import CustomerList from "./components/Features/Staff/CustomerList";
import ProductList from "./components/Features/Staff/ProductList";
import HomePage from "./components/Homepage/HomePage";
import ViewCart from "./components/Features/Staff/ViewCart";
import AddManager from "./components/Features/Admin/AddManager";
import Manager from "./components/Features/Manager/Manager";
import AddStaff from "./components/Features/Manager/AddStaff";
import ViewStaffList from "./components/Features/Manager/ViewStaffList";
import EditStaff from "./components/Features/Manager/EditStaff";
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


function App() {
  return (
    <div className="overflow-hidden">
      <ToastContainer />
      <Navbar />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/homepage" element={<HomePage />} />
        <Route path="/product" element={<ListProducts />} />
        <Route path="/customer-list" element={<CustomerList />} />
        <Route path="/productlist" element={<ProductList />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/view-dashboard" element={<ViewDashboard />} />
        <Route path="/view-manager-list" element={<ViewManagerList />} />
        <Route path="/view-staff-list" element={<ViewStaffList />} />
        <Route path="/add-manager" element={<AddManager />} />
        <Route path="/add-staff" element={<AddStaff />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="/staff" element={<Staff />} />
        <Route path="/manager" element={<Manager />} />
        <Route path="/products/:id" element={<ProductDetail />} />

        <Route path="/edit-staff/:id" element={<EditStaff />} />

        <Route path="/edit-manager/:id" element={<EditManager />} />
        <Route path="/viewcart" element={<ViewCart />} />
      </Routes>
      <Routes></Routes>
      <Footer />
    </div>
  );
}

export default App;
