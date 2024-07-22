import "./App.scss";
import ViewCategory from "./components/Features/Manager/ViewCategoryList";
import Login from "./components/Login/Login";
import ViewPromotion from "./components/Features/Manager/ViewPromotion";
import Navbar from "./components/Navbar/Navbar";
import Footer from "./components/Footer/Footer";
import { Routes, Route, Navigate } from "react-router-dom";
import ProductDetail from "./components/ProductDetail/ProductDetail";
import ViewDashboard from "./components/Features/Admin/ViewDashboard";
import ViewManagerList from "./components/Features/Admin/ViewManagerList";
import "bootstrap/dist/css/bootstrap.min.css";
import EditManager from "./components/Features/Admin/EditManager";
import Admin from "./components/Features/Admin/Admin";
import Staff from "./components/Features/Staff/Staff";
import CustomerList from "./components/Features/Staff/CustomerList";
import ProductList from "./components/Features/Staff/ProductList";
import ViewCart from "./components/Features/Staff/ViewCart";
import AddManager from "./components/Features/Admin/AddManager";
import Manager from "./components/Features/Manager/Manager";
import AddStaff from "./components/Features/Manager/AddStaff";
import ViewStaffList from "./components/Features/Manager/ViewStaffList";
import EditStaff from "./components/Features/Manager/EditStaff";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
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
import AddCategory from "./components/Features/Manager/AddCategory";
import ForgetForm from "./components/Login/ForgetForm";
import ResetForm from "./components/Login/ResetForm";
import ViewCounter from "./components/Features/Manager/ViewCounter";
import GoldPrice from "./components/Features/Staff/GoldPrice";
import Dashboard from "./components/Dashboard/Dashboard";
import Voucher from "./components/Features/Manager/Voucher";
import { getToken } from "./components/Authen/Auth";
import { useEffect, useState } from "react";
import axios from "axios";
import SuccessPayment from "./components/Features/Staff/SuccessPayment";
import StaffPolicy from "./components/Features/Staff/StaffPolicy";
function App() {
  //check validate pages
  const ProtectedRoute = ({ role, element }) => {
    const token = getToken(); // get token from  Auth
    const [userRole, setUserRole] = useState(null);

    useEffect(() => {
      const fetchUser = async () => {
        if (token) {
          try {
            const response = await axios.get(
              "http://localhost:8080/api/this-info",
              {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              }
            );
            setUserRole(response.data.roleID); // get roleID
          } catch (error) {
            console.error("Error fetching user:", error);
          }
        }
      };

      fetchUser();
    }, [token]);

    if (userRole === null) {
      
      return <div>Loading...</div>; // component loading
    }

    if (role.includes(userRole)) {

      return element;
    } else {
      return <Navigate to="/" />; // Redirect login if no access
    }
  };
  return (
    <div className="overflow-hidden">
      <ToastContainer />
      <Navbar />
      <Routes>
        <Route path="/" element={<Login />} />
        <Route
          path="/customer-list"
          element={<ProtectedRoute role={[1]} element={<CustomerList />} />}
        />
        <Route path="/login" element={<Login />} />
        <Route
          path="/view-dashboard"
          element={<ProtectedRoute role={[3]} element={<ViewDashboard />} />}
        />
        <Route
          path="/view-manager-list"
          element={<ProtectedRoute role={[3]} element={<ViewManagerList />} />}
        />
        <Route
          path="/view-staff-list"
          element={<ProtectedRoute role={[2]} element={<ViewStaffList />} />}
        />
        <Route
          path="/add-manager"
          element={<ProtectedRoute role={[3]} element={<AddManager />} />}
        />
        <Route
          path="/add-staff"
          element={<ProtectedRoute role={[2]} element={<AddStaff />} />}
        />
        <Route
          path="/admin"
          element={<ProtectedRoute role={[3]} element={<Admin />} />}
        />
        <Route
          path="/staff"
          element={<ProtectedRoute role={[1]} element={<Staff />} />}
        />
        <Route
          path="/manager"
          element={<ProtectedRoute role={[2]} element={<Manager />} />}
        />
        <Route
          path="/products/:id"
          element={<ProtectedRoute role={[1]} element={<ProductDetail />} />}
        />
        <Route
          path="/add-product"
          element={<ProtectedRoute role={[2]} element={<AddProduct />} />}
        />
        <Route
          path="/edit-staff/:id"
          element={<ProtectedRoute role={[2]} element={<EditStaff />} />}
        />
        <Route
          path="/view-category"
          element={<ProtectedRoute role={[2]} element={<ViewCategory />} />}
        />
        <Route
          path="/edit-manager/:id"
          element={<ProtectedRoute role={[3]} element={<EditManager />} />}
        />
        <Route
          path="/viewcart"
          element={<ProtectedRoute role={[1]} element={<ViewCart />} />}
        />
        <Route
          path="/productlist"
          element={<ProtectedRoute role={[1]} element={<ProductList />} />}
        />
        <Route
          path="/productlist2"
          element={
            <ProtectedRoute role={[2]} element={<ProductListManager />} />
          }
        />
        <Route
          path="/order-list"
          element={<ProtectedRoute role={[1]} element={<ViewOrderList />} />}
        />
        <Route
          path="/order-list/:cusphone"
          element={<ProtectedRoute role={[1]} element={<ViewOrderList />} />}
        />
        <Route
          path="/productdetail/:id"
          element={
            <ProtectedRoute role={[1]} element={<ProductDetailStaff />} />
          }
        />
        <Route
          path="/productdetail2/:id"
          element={
            <ProtectedRoute role={[2]} element={<ProductDetailManager />} />
          }
        />
        <Route
          path="/orderdetail/:id"
          element={<ProtectedRoute role={[1]} element={<OrderDetail />} />}
        />
        <Route
          path="/refund-detail/:id"
          element={<ProtectedRoute role={[1]} element={<RefundDetail />} />}
        />
        <Route
          path="/refund-form/:id"
          element={<ProtectedRoute role={[1]} element={<RefundForm />} />}
        />
        <Route
          path="/refund-list"
          element={<ProtectedRoute role={[1]} element={<RefundList />} />}
        />
        <Route
          path="/refund-viewcart"
          element={<ProtectedRoute role={[1]} element={<RefundViewCart />} />}
        />
        <Route
          path="/refund-purity/:id/:productID"
          element={<ProtectedRoute role={[1]} element={<RefundPurity />} />}
        />
        <Route
          path="/success"
          element={<ProtectedRoute role={[1]} element={<SuccessPayment />} />}
        />
        <Route
          path="/order-refund"
          element={<ProtectedRoute role={[1]} element={<ViewOrderRefund />} />}
        />
        <Route
          path="/add-category"
          element={<ProtectedRoute role={[2]} element={<AddCategory />} />}
        />
        <Route path="/reset-password" element={<ResetForm />} />
        <Route path="/forget-form" element={<ForgetForm />} />
        <Route
          path="/view-promotion"
          element={<ProtectedRoute role={[2]} element={<ViewPromotion />} />}
        />
        <Route
          path="/view-counter"
          element={<ProtectedRoute role={[2]} element={<ViewCounter />} />}
        />
        <Route path="/gold-price" element={<GoldPrice />} />
        <Route
          path="/dashboard"
          element={<ProtectedRoute role={[3]} element={<Dashboard />} />}
        />

        <Route
          path="/view-voucher"
          element={<ProtectedRoute role={[2]} element={<Voucher />} />}
        />

        <Route
          path="/staff-policy"
          element={<ProtectedRoute role={[1]} element={<StaffPolicy />} />}
        />
      </Routes>
      <Routes></Routes>
      <Footer />
    </div>
  );
}

export default App;
