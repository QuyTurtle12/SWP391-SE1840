import axios from "axios";
import React, { useEffect, useState } from "react";
import ManagerMenu from "./ManagerMenu";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import { Button } from "@mui/material";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";

export default function ProductListManager() {
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/v2/products")
      .then((response) => {
        console.log(response.data);
        setProducts(response.data);
      })
      .catch((error) => console.error("error at fetching data", error));
  }, []);

  const handleProductClick = (id) => {
    navigate(`/productdetail2/${id}`);
  };

 
  return (
    <div className=" ">
     
    <ManagerMenu/>
    <ToastContainer/>

      <div className="bg-white py-36">
        <div className="container mx-auto px-4">
          <h2 className="text-3xl justify-between text-center font-bold text-black mb-8">
            Our Latest Products
            <div className="justify-end flex pr-8">
              <div className="relative">
                <Button component={Link} to="/viewcart">
                  <ShoppingCartIcon className="text-black" sx={{ fontSize: 40 }} />
                </Button>
                {cart.length > 0 && (
                  <div className="absolute top-0 right-0 bg-red-500 text-white rounded-full w-4 h-4 flex items-center justify-center">
                    <h6 className="text-sm">{cart.length}</h6>
                  </div>
                )}
              </div>
            </div>
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {products.map((product) => (
              <div key={product.id} className="bg-white rounded-lg shadow-lg p-8">
                <div className="relative overflow-hidden">
                  <img className="object-fit w-full h-96" src={product.img} alt={product.name} />
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
                <h3 className="text-xl font-bold text-gray-900 mt-4">{product.name}</h3>
                
                 {/* Display stock status */}
                 {product.stock <= 0 ? (
                  <span className="text-gray-400 font-bold mt-2">Out of Stock</span>
                ) : (
                  <span className="text-black font-bold mt-2">In Stock: {product.stock}</span>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}


