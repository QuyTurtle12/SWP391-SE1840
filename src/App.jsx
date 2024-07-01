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
import AddProduct from "./components/Features/Manager/AddProduct";
import ProductDetailManager from "./components/Features/Manager/ProductDetailManager";
import ProductDetailStaff from "./components/Features/Staff/ProductDetailStaff";
import ViewOrderList from "./components/Features/Staff/ViewOrderList";
import OrderDetail from "./components/Features/Staff/OrderDetail";
import RefundList from "./components/Features/Staff/RefundList";
import RefundViewCart from "./components/Features/Staff/RefundViewCart";
import RefundForm from "./components/Features/Staff/RefundForm";
import ViewOrderRefund from "./components/Features/Staff/ViewOrderRefund";
import RefundDetail from "./components/Features/Staff/RefundDetail";
import RefundPurity from "./components/Features/Staff/RefundPurity";
import ProductListManager from "./components/Features/Manager/ProductList-manager";
function App() {
  return (
    <div className="overflow-hidden">
      <ToastContainer />
      <Navbar />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/homepage" element={<HomePage />} />
        <Route path="/customer-list" element={<CustomerList />} />
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
        <Route path="/add-product" element={<AddProduct />} />
        <Route path="/edit-staff/:id" element={<EditStaff />} />
        <Route path="/edit-manager/:id" element={<EditManager />} />
        <Route path="/viewcart" element={<ViewCart />} />
        <Route path="/productlist" element={<ProductList />} />
        <Route path="/productlist2" element={<ProductListManager />} />
        <Route path="/order-list" element={<ViewOrderList />} />
        <Route path="/order-list/:cusphone" element={<ViewOrderList />} />
        <Route path="/productdetail/:id" element={<ProductDetailStaff />} />
        <Route path="/productdetail2/:id" element={<ProductDetailManager />} />
        <Route path="/orderdetail/:id" element={<OrderDetail />} />
        <Route path="/refund-detail/:id" element={<RefundDetail />} />
        <Route path="/refund-form/:id" element={<RefundForm />} />
        <Route path="/refund-list" element={<RefundList />} />
        <Route path="/refund-viewcart" element={<RefundViewCart />} />
        <Route path="/refund-purity/:id/:productID" element={<RefundPurity />} />
        <Route path="/order-refund" element={<ViewOrderRefund />} />
      </Routes>
      <Routes></Routes>
      <Footer />
    </div>
  );
}

export default App;