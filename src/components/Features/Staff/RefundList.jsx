import axios from "axios";
import React, { useEffect, useState } from "react";
import StaffMenu from "./StaffMenu";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import { Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";

function RefundList() {
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState([]);
  const [searchInput, setSearchInput] = useState("");
  const [staff, setStaff] = useState("");
  const navigate = useNavigate();
  const token = localStorage.getItem('token'); // Fetch the token from local storage

  useEffect(() => {
    if (token) {
      axios
        .get("https://jewelrysalesystem-backend.onrender.com/api/v2/products", {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })
        .then((response) => {
          console.log(response.data);
          setProducts(response.data);
        })
        .catch((error) => console.error("Error fetching products:", error));
      fetchStaff();
    } else {
      console.error("No token found");
    }
  }, [token]);

  const fetchStaff = async () => {
    if (token) {
      try {
        const response = await axios.get("https://jewelrysalesystem-backend.onrender.com/api/this-info", {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        console.log(response.data);
        setStaff(response.data);
        fetchCartData(response.data.id); // Fetch cart data with the staff ID after setting the staff state
      } catch (error) {
        console.error("Error fetching staff:", error);
      }
    } else {
      console.error("No token found");
    }
  };

  const searchProduct = () => {
    if (token) {
      axios
        .get(`https://jewelrysalesystem-backend.onrender.com/api/v2/products/search?input=${searchInput}&filter=ByName`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })
        .then((response) => {
          setProducts(response.data);
        })
        .catch((error) => console.error("Error searching products:", error));
    } else {
      console.error("No token found");
    }
  };

  const handleSearchInputChange = (event) => {
    setSearchInput(event.target.value);
  };

  const fetchCartData = (staffId) => {
    if (token) {
      axios
        .get(`https://jewelrysalesystem-backend.onrender.com/cart/refundItem?staffId=${staffId}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })
        .then((response) => {
          setCart(response.data);
        })
        .catch((error) => {
          console.error("Error fetching cart data:", error);
        });
    } else {
      console.error("No token found");
    }
  };

  const addToCart = (product) => {
    const isAlreadyInCart = cart.some((item) => item.product.id === product.id);
    if (isAlreadyInCart) {
      toast.error("Product is already in the cart.");
      return;
    }

    if (token) {
      axios
        .post(`https://jewelrysalesystem-backend.onrender.com/cart/refundItem?staffId=${staff.id}`, product, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })
        .then((response) => {
          toast.success("Item added to cart:", response.data);
          setCart([...cart, { product, quantity: 1, price: product.refundPrice }]);
        })
        .catch((error) => {
          toast.error(`${error.response ? error.response.data : error.message}`);
          console.error("Error adding item to cart:", error);
        });
    } else {
      console.error("No token found");
    }
  };

  const handleViewCartClick = () => {
    navigate(`/refund-viewcart?staffId=${staff.id}`);
  };

  return (
    <div>
      <StaffMenu />
      <ToastContainer />
      <div className="bg-white py-36">
        <div className="container mx-auto px-4">
          <h2 className="text-3xl justify-between text-center font-bold text-black mb-8">
            List Refund Products
            <div className="justify-end flex pr-8">
              <div className="relative">
                <Button onClick={handleViewCartClick} className="text-black">
                  <ShoppingCartIcon />{" "}
                  <i
                    className="fa fa-shopping-cart"
                    style={{ fontSize: "40px" }}
                  ></i>
                </Button>
                {cart.length > 0 && (
                  <div className="absolute top-0 right-0 bg-red-500 text-white rounded-full w-4 h-4 flex items-center justify-center">
                    <h6 className="text-sm">{cart.length}</h6>
                  </div>
                )}
              </div>
            </div>
            <div className="mb-8 font-bold">
              <input
                type="text"
                value={searchInput}
                onChange={handleSearchInputChange}
                placeholder="Search products..."
                className="border p-2 mr-2"
              />
              <button
                onClick={searchProduct}
                className="bg-gray-900 text-white py-2 px-4 rounded-full font-bold hover:bg-gray-500"
              >
                Search
              </button>
            </div>
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {products.map((product) => (
              <div key={product.id} className="bg-white rounded-lg shadow-lg p-8">
                <div className="relative border-2 border-black overflow-hidden">
                  <img className="object-fit w-full h-96" src={product.img} alt={product.name} />
                </div>
                <h3 className="text-xl font-bold text-gray-900 mt-4">
                  {product.name}
                </h3>
                <div className="flex">
                  <h3 className="text-xl font-bold text-gray-900 mt-4">
                    Price :{" "}
                  </h3>
                  <h4 className="text-lg font-semibold text-gray-900 px-4 mt-4">
                    ${product.price}
                  </h4>
                </div>
                <div className="flex">
                  <h3 className="text-xl font-bold text-gray-900 mt-4">
                    Refund Price :{" "}
                  </h3>
                  <h4 className="text-lg font-semibold text-gray-900 px-4 mt-4">
                    ${product.refundPrice}
                  </h4>
                </div>
                <div className="flex items-center justify-between mt-4">
                  <button
                    onClick={() => addToCart(product)}
                    className="bg-gray-900 text-white py-2 px-4 rounded-full font-bold hover:bg-gray-800"
                  >
                    Add to Refund Cart
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default RefundList;
