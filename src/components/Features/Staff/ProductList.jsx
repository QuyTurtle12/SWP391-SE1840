import axios from "axios";
import React, { useEffect, useState } from "react";
import StaffMenu from "./StaffMenu";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import { Button } from "@mui/material";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";

function ProductList() {
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState([]);
  const navigate = useNavigate();
  const [searchInput, setSearchInput] = useState("");
  const token = localStorage.getItem("token"); // Fetch the token from local storage
  const [staff, setStaff] = useState("");

  useEffect(() => {
    if (token) {
      axios
        .get("https://jewelrysalesystem-backend.onrender.com/api/v2/products", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
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
        const response = await axios.get(
          "https://jewelrysalesystem-backend.onrender.com/api/this-info",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
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
        .get(
          `https://jewelrysalesystem-backend.onrender.com/api/v2/products/search?input=${searchInput}&filter=ByName`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
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
        .get(
          `https://jewelrysalesystem-backend.onrender.com/cart?staffId=${staffId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
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

  const handleProductClick = (id) => {
    navigate(`/productdetail/${id}`);
  };

  const addToCart = (product) => {
    if (product.stock <= 0) {
      toast.error("Product is out of stock.");
      return;
    }
  
    const isAlreadyInCart = cart.some((item) => item.product.id === product.id);
    if (isAlreadyInCart) {
      toast.error("Product is already in the cart.");
      return;
    }
  
    const priceToAdd = product.discountPrice ? product.discountPrice : product.price;
  
    if (token) {
      axios
        .post(
          `https://jewelrysalesystem-backend.onrender.com/cart?staffId=${staff.id}`,
          { ...product, price: priceToAdd },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then((response) => {
          toast.success("Item added to cart:", response.data);
          setCart([...cart, { product, quantity: 1, price: priceToAdd }]);
        })
        .catch((error) => {
          toast.error(
            `${error.response ? error.response.data : error.message}`
          );
          console.error("Error adding item to cart:", error);
        });
    } else {
      console.error("No token found");
    }
  };

  const handleViewCartClick = () => {
    navigate(`/viewcart?staffId=${staff.id}`);
  };

  return (
    <div>
      <StaffMenu />
      <ToastContainer />
      <div className="bg-white py-36">
        <div className="container mx-auto px-4">
          <h2 className="text-3xl justify-between text-center font-bold text-black mb-8">
            Our Latest Products
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
          </h2>
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
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {products.map((product) => (
              <div
                key={product.id}
                className="bg-white rounded-lg shadow-lg p-8"
              >
                <div className="relative overflow-hidden">
                  <img
                    className="object-fit w-full h-96"
                    src={product.img}
                    alt={product.name}
                  />
                  <div className="absolute inset-0 bg-black opacity-40"></div>
                  <div className="absolute inset-0 flex items-center justify-center">
                    <button
                      onClick={() => handleProductClick(product.id)}
                      className="bg-white text-gray-900 py-2 px-6 rounded-full font-bold hover:bg-gray-300"
                    >
                      View Product
                    </button>
                  </div>
                </div>
                <h3 className="text-xl font-bold text-gray-900 mt-4">
                  {product.name}
                </h3>
                <div className="flex items-center justify-between mt-4">
                  {product.price !== product.discountPrice ? (
                    <>
                      <span className="text-gray-400 font-bold text-lg line-through">
                        {product.price.toLocaleString("en-US")} $
                      </span>
                      <span className="text-gray-900 font-bold text-lg">
                        {product.discountPrice.toLocaleString("en-US")} $
                      </span>
                    </>
                  ) : (
                    <span className="text-gray-900 font-bold text-lg">
                      {product.price.toLocaleString("en-US")} $
                    </span>
                  )}{" "}
                  <button
                    onClick={() => addToCart(product)}
                    className="bg-gray-900 text-white py-2 px-4 rounded-full font-bold hover:bg-gray-500"
                  >
                    Add to Cart
                  </button>
                </div>
                {product.stock <= 0 ? (
                  <span className="text-gray-400 font-bold mt-2">
                    Out of Stock
                  </span>
                ) : (
                  <span className="text-black font-bold mt-2">
                    In Stock: {product.stock}
                  </span>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProductList;
