import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

export default function UpdateProduct() {
  const { id } = useParams(); // Lấy id từ URL
  const navigate = useNavigate(); // Để điều hướng sau khi cập nhật thành công
  const handleButtonX = () => {
    navigate("/productlist2");
  };
  const [product, setProduct] = useState({
    img: "",
    name: "",
    price: "",
    refundPrice: "",
    goldWeight: "",
    laborCost: "",
    stoneCost: "",
    stock: "",
    promotionID: "",
    category: "",
    stock: "",
    description: "",
  });

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/account/getProduct?id=${id}`
        );
        console.log("Fetched product data:", response.data);
        setProduct(response.data);
      } catch (error) {
        console.error("Error fetching product data", error);
      }
    };

    fetchProduct();
  }, [id]);



  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log("Submitting form with data:", product);
    try {
      const { price, refundPrice, goldWeight, laborCost, stoneCost, promotionID, category, stock, status } = product; // Lấy các giá trị từ object manager
      const response = await axios.put(
        `http://localhost:8080/api//v2/products/update-info?ID=${parseInt(
          id
        )}&name=${name}&price=${price}&refundPrice=${refundPrice}&description=${description}&goldWeight=${goldWeight}&laborCost=${laborCost}&stoneCost=${stoneCost}&promotionID=${promotionID}&category=${category}&stock=${stock}&status=${counterID}`
      );
      console.log("Update response:", response);
      navigate("/productlist2"); // Điều hướng quay lại danh sách manager sau khi cập nhật thành công
    } catch (error) {
      console.error(
        "Error updating staff",
        error.response ? error.response.data : error.message
      );
    }
  };

  const handleNameChange = (e) => {
    setProductName({ ...product, name: e.target.value });
  };
  const handlePriceChange = (e) => {
    setProductPrice({ ...product, price: e.target.value });
  };

  const handleRefundPriceChange = (e) => {
    setRefundPrice({ ...product, refundPrice: e.target.value });
  };
  const handleGoldWeightChange = (e) => {
    setGoldWeight({ ...product, goldWeight: e.target.value });
  };
  const handleStoneCostChange = (e) => {
    setStoneCost({ ...product, stoneCost: e.target.value });
  };
  const handlePromotionIDChange = (e) => {
    setPromotionID({ ...product, promotionID: e.target.value });
  };
  const handleCategoryChange = (e) => {
    setCategory({ ...product, category: e.target.value });
  };
  const handleDescriptionChange = (e) => {
    setDescription({ ...product, description: e.target.value });
  };
  const handleStockChange = (e) => {
    setStock({ ...product, stock: e.target.value });
    return (
      <div className="flex items-center justify-center min-h-screen bg-tiffany">
        <div className="bg-white rounded-lg shadow relative w-full max-w-2xl p-10">
          <div className="flex items-start justify-between p-5 border-b rounded-t">
            <h3 className="text-xl font-semibold">Edit Product</h3>
            <button
              type="button"
              className="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center"
              data-modal-toggle="product-modal"
              onClick={handleButtonX}
            >
              <svg
                className="w-5 h-5"
                fill="currentColor"
                viewBox="0 0 20 20"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  fillRule="evenodd"
                  d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                  clipRule="evenodd"
                ></path>
              </svg>
            </button>
          </div>

          <div className="p-6 space-y-6">
            <form onSubmit={handleSubmit}>
              <div className="grid grid-cols-6 gap-6">
                <div className="col-span-6 sm:col-span-3">
                  <label
                    htmlFor="fullName"
                    className="text-sm font-semibold text-gray-900 block mb-2"
                  >
                    Full Name
                  </label>
                  <input
                    type="text"
                    name="fullName"
                    id="fullName"
                    value={staff.fullName}
                    onChange={handleChange}
                    className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-cyan-600 focus:border-cyan-600 block w-full p-2.5"
                    required
                  />
                </div>
                <div className="col-span-6 sm:col-span-3">
                  <label
                    htmlFor="gender"
                    className="text-sm font-semibold text-gray-900 block mb-2"
                  >
                    Gender
                  </label>
                  <select
                    id="gender"
                    value={staff.gender}
                    onChange={handleGenderChange}
                    className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-cyan-600 focus:border-cyan-600 block w-full p-2.5"
                    required
                  >
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                    <option value="Third gender">Third gender</option>
                  </select>
                </div>
                <div className="col-span-6 sm:col-span-3">
                  <label
                    htmlFor="contactInfo"
                    className="text-sm font-semibold text-gray-900 block mb-2"
                  >
                    Contact Info
                  </label>
                  <input
                    type="text"
                    name="contactInfo"
                    id="contactInfo"
                    value={staff.contactInfo}
                    onChange={handleChange}
                    className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-cyan-600 focus:border-cyan-600 block w-full p-2.5"
                    required
                  />
                </div>
                <div className="col-span-6 sm:col-span-3">
                  <label
                    htmlFor="counterID"
                    className="text-sm font-semibold text-gray-900 block mb-2"
                  >
                    Counter ID
                  </label>

                  <select
                    id="counterID"
                    value={staff.counterID}
                    onChange={handleCounterChange}
                    className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-cyan-600 focus:border-cyan-600 block w-full p-2.5"
                    required
                  >
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                  </select>
                </div>
              </div>
              <div className="p-6 border-t border-gray-200 rounded-b">
                <button
                  className="text-white bg-cyan-600 hover:bg-cyan-700 focus:ring-4 focus:ring-cyan-200 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
                  type="submit"
                >
                  Save all
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    );
  }

}
