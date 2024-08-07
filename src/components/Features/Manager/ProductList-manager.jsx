import React, { useState, useEffect } from "react";
import axios from "axios";
import ManagerMenu from "./ManagerMenu";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function ProductListManager() {
  const [products, setProducts] = useState([]);
  const [searchInput, setSearchInput] = useState("");
  const [searchFilter, setSearchFilter] = useState("ByName");
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

  const handleSearch = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get("https://jewelrysalesystem-backend.onrender.com/api/v2/products/search", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        params: {
          input: searchInput,
          filter: searchFilter,
        },
      });
      if (response.status === 200) {
        setProducts(response.data);
      } else {
        toast.error("No products found");
      }
    } catch (error) {
      console.error("Error searching products", error);
      toast.error("Failed to search products");
    }
  };

  const handleReset = () => {
    setSearchInput("");
    fetchProducts();
  };

  const handleProductClick = (id) => {
    navigate(`/productdetail2/${id}`);
  };

  const handleChangeStatus = async (id, status) => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.put(`https://jewelrysalesystem-backend.onrender.com/api/v2/products/${id}/status`, null, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.status === 200) {
        toast.success(`Product ${status ? 'deactivated' : 'activated'} successfully!`);
        fetchProducts(); // Refresh the product list
      } else {
        toast.error(`Failed to change product status. Error: ${response.data}`);
      }
    } catch (error) {
      toast.error(`Failed to change product status. Error: ${error.response ? error.response.data : error.message}`);
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

          <div className="mb-8">
            <div className="flex items-center">
              <select
                value={searchFilter}
                onChange={(e) => setSearchFilter(e.target.value)}
                className="border p-2 rounded mr-4"
              >
                <option value="ByName">Search by Name</option>
                <option value="ByProductID">Search by ID</option>
              </select>
              <input
                type="text"
                value={searchInput}
                onChange={(e) => setSearchInput(e.target.value)}
                placeholder={`Enter product ${searchFilter === 'ByName' ? 'name' : 'ID'}`}
                className="border p-2 rounded w-full mr-4"
              />
              <button
                onClick={handleSearch}
                className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
              >
                Search
              </button>
              <button
                onClick={handleReset}
                className="bg-gray-500 text-white px-4 py-2 rounded ml-4 hover:bg-gray-600"
              >
                Reset
              </button>
            </div>
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
                    {product.status ? (
                      <button
                        onClick={() => handleChangeStatus(product.id, false)}
                        className="bg-red-600 text-white py-2 px-4 rounded-full font-semibold hover:bg-red-700"
                      >
                        Deactivate
                      </button>
                    ) : (
                      <button
                        onClick={() => handleChangeStatus(product.id, true)}
                        className="bg-green-600 text-white py-2 px-4 rounded-full font-semibold hover:bg-green-700"
                      >
                        Activate
                      </button>
                    )}
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
