import React, { useState, useEffect } from "react";
import axios from "axios";
import ManagerMenu from "./ManagerMenu";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import { Button } from "@mui/material";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function ProductListManager() {
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get("https://jewelrysalesystem-backend.onrender.com/api/v2/products", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setProducts(response.data);
    } catch (error) {
      console.error("Error fetching products", error);
      toast.error("Failed to fetch products");
    }
  };

  const handleProductClick = (id) => {
    navigate(`/productdetail2/${id}`);
  };

  const handleDeleteProduct = async (id) => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.delete(`https://jewelrysalesystem-backend.onrender.com/api/v2/products/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.status === 200) {
        toast.success("Product deleted successfully!");
        setProducts(products.filter((product) => product.id !== id));
      } else {
        toast.error(`Failed to delete product. Error: ${response.data}`);
      }
    } catch (error) {
      toast.error(`Failed to delete product. Error: ${error.response ? error.response.data : error.message}`);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <ManagerMenu />
      <ToastContainer />

      <div className="py-16 bg-gray-50">
        <div className="container mx-auto px-4">
          <div className="flex justify-between items-center mb-8">
            <h2 className="text-3xl font-bold text-gray-800">Our Latest Products</h2>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {products.map((product) => (
              <div key={product.id} className="bg-white rounded-lg shadow-lg overflow-hidden">
                <div className="relative">
                  <img className="w-full h-64 object-cover" src={product.img} alt={product.name} />
                  <div className="absolute inset-0 bg-black opacity-30"></div>
                  <div className="absolute inset-0 flex items-center justify-center">
                    <button
                      onClick={() => handleProductClick(product.id)}
                      className="bg-white text-gray-900 py-2 px-4 rounded-full font-semibold hover:bg-gray-200"
                    >
                      Update Product
                    </button>
                  </div>
                </div>
                <div className="p-6">
                  <h3 className="text-xl font-semibold text-gray-900 mb-2">{product.name}</h3>
                  {product.stock <= 0 ? (
                    <span className="text-red-600 font-medium">Out of Stock</span>
                  ) : (
                    <span className="text-green-600 font-medium">In Stock: {product.stock}</span>
                  )}
                  <div className="mt-4 flex justify-between items-center">
                    <button
                      onClick={() => handleDeleteProduct(product.id)}
                      className="bg-red-600 text-white py-2 px-4 rounded-full font-semibold hover:bg-red-700"
                    >
                      Delete Product
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
