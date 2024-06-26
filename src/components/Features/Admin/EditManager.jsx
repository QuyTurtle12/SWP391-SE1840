import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

function EditManager() {
  const { id } = useParams(); // Lấy id từ URL
  const navigate = useNavigate(); // Để điều hướng sau khi cập nhật thành công
  const handleButtonX = () => {
    navigate("/view-manager-list");
  };

  const [manager, setManager] = useState({
    fullName: "",
    gender: "",
    contactInfo: "",
    counterID: "",
  });

  useEffect(() => {
    const fetchManager = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/v2/accounts/user?id=${id}`
        );
        console.log("Fetched manager data:", response.data);
        setManager(response.data);
      } catch (error) {
        console.error("Error fetching manager data", error);
      }
    };

    fetchManager();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setManager({ ...manager, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

  

    console.log("Submitting form with data:", manager);
    try {
      const { fullName, gender, contactInfo, counterID } = manager; // Lấy các giá trị từ object manager
      const response = await axios.put(
        `http://localhost:8080/api/v2/accounts/MANAGER?ID=${parseInt(
          id
        )}&fullName=${fullName}&gender=${gender}&contactInfo=${contactInfo}&counterID=${counterID}`
      );
      console.log("Update response:", response);
      navigate("/view-manager-list"); // Điều hướng quay lại danh sách manager sau khi cập nhật thành công
    } catch (error) {
      console.error(
        "Error updating manager",
        error.response ? error.response.data : error.message
      );
      alert("Error updating manager. Please try again.");
    }
  };

  const handleGenderChange = (e) => {
    setManager({ ...manager, gender: e.target.value });
  };

  const handleCounterChange = (e) => {
    setManager({ ...manager, counterID: e.target.value });
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white rounded-lg shadow relative w-full max-w-2xl p-10">
        <div className="flex items-start justify-between p-5 border-b rounded-t">
          <h3 className="text-xl font-semibold">Edit Manager</h3>
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
                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 111.414 1.414L11.414 10l4.293 4.293a1 1 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 01-1.414-1.414L8.586 10 4.293 5.707a1 1 010-1.414z"
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
                  value={manager.fullName}
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
                  value={manager.gender}
                  onChange={handleGenderChange}
                  className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-cyan-600 focus:border-cyan-600 block w-full p-2.5"
                  required
                >
                  <option value="Male">Male</option>
                  <option value="Female">Female</option>
                  <option value="Other">Other</option>
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
                  value={manager.contactInfo}
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
                  value={manager.counterID}
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
                className="text-white bg-black focus:ring-4 focus:ring-cyan-200 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
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

export default EditManager;
