import axios from "axios";
import React, { useEffect, useState } from "react";
import StaffMenu from "./StaffMenu";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import { Button } from "@mui/material";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";

function RefundList() {
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState([]);

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/v2/products")
      .then((response) => {
        console.log(response.data);
        setProducts(response.data);
      })
      .catch((error) => console.error("error at fetching data", error));
  }, []);


  const addToCart = (product) => {
  
    const isAlreadyInCart = cart.some((item) => item.id === product.id);
    if (isAlreadyInCart) {
      toast.error("Product is already in the cart.");
      return;
    }

    axios
      .post("http://localhost:8080/cart/refundItem", product)
      .then((response) => {
        console.log("Item added to cart:", response.data);
        setCart([...cart, product]);
      })
      .catch((error) => {
        console.error("Error adding item to cart:", error);
      });
  };

  return (
    <div className=" ">
     
        <StaffMenu />
    <ToastContainer/>

      <div className="bg-white py-36">
        <div className="container mx-auto px-4">
          <h2 className="text-3xl justify-between text-center font-bold text-black mb-8">
             List Refund Products
            <div className="justify-end flex pr-8">
              <div className="relative">
                <Button component={Link} to="/refund-viewcart">
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
                <div className="relative border-2 border-black  overflow-hidden">
                  <img className="object-fit w-full h-96" src={product.img} alt={product.name} />
                  
                </div>
                <h3 className="text-xl font-bold text-gray-900 mt-4">{product.name}</h3>

                <div className="flex">
                <h3 className="text-xl font-bold text-gray-900 mt-4">Price : </h3>
                <h4 className="text-lg font-semibold text-gray-900 px-4 mt-4">${product.price}</h4>

                </div>
                <div className="flex">
                <h3 className="text-xl font-bold text-gray-900 mt-4">Refund Price : </h3>
                <h4 className="text-lg font-semibold text-gray-900 px-4 mt-4">${product.refundPrice}</h4>

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
