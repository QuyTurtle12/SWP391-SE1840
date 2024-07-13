import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function EditStaff() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [staff, setStaff] = useState({
    fullName: "",
    gender: "",
    contactInfo: "",
    counterID: "",
  });
  const [counters, setCounters] = useState([]);

  useEffect(() => {
    const fetchStaff = async () => {
      const token = localStorage.getItem('token');

      try {
        const response = await axios.get(
          `https://jewelrysalesystem-backend.onrender.com/api/v2/accounts/user?id=${id}`, {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          }
        );
        setStaff(response.data);
      } catch (error) {
        console.error("Error fetching staff data", error);
      }
    };

    const fetchCounters = async () => {
      const token = localStorage.getItem('token');

      try {
        const response = await axios.get(
          "http://localhost:8080/api/v2/counters/no-sale", {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          }
        );
        setCounters(response.data);
      } catch (error) {
        console.error("Error fetching counters", error);
      }
    };

    fetchStaff();
    fetchCounters();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setStaff({ ...staff, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validate inputs
    if (staff.fullName.match(/[^a-zA-Z\s]/)) {
      toast.error("Name cannot contain special characters!");
      return;
    }
    if (staff.counterID < 0) {
      toast.error("Counter ID cannot be below 0!");
      return;
    }

    const token = localStorage.getItem('token');

    try {
      await axios.put(
        `https://jewelrysalesystem-backend.onrender.com/api/v2/accounts/STAFF?ID=${parseInt(id)}&fullName=${staff.fullName}&gender=${staff.gender}&contactInfo=${staff.contactInfo}&counterID=${staff.counterID}`, {}, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }
      );
      toast.success("Staff updated successfully!");
      navigate("/view-staff-list");
    } catch (error) {
      toast.error(
        error.response ? error.response.data : "Error updating staff!"
      );
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-tiffany">
      <ToastContainer />
      <div className="bg-white rounded-lg shadow-lg p-8 w-full max-w-2xl">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-2xl font-bold">Edit Staff</h2>
          <button
            onClick={() => navigate("/view-staff-list")}
            className="text-gray-500 hover:text-gray-700"
          >
            X
          </button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="grid grid-cols-1 gap-6">
            <div>
              <label
                htmlFor="fullName"
                className="block text-sm font-medium text-gray-700"
              >
                Full Name
              </label>
              <input
                type="text"
                name="fullName"
                id="fullName"
                value={staff.fullName}
                onChange={handleChange}
                className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                required
              />
            </div>
            <div>
              <label
                htmlFor="gender"
                className="block text-sm font-medium text-gray-700"
              >
                Gender
              </label>
              <select
                name="gender"
                id="gender"
                value={staff.gender}
                onChange={handleChange}
                className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                required
              >
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
            </div>
            <div>
              <label
                htmlFor="contactInfo"
                className="block text-sm font-medium text-gray-700"
              >
                Contact Info
              </label>
              <input
                type="text"
                name="contactInfo"
                id="contactInfo"
                value={staff.contactInfo}
                onChange={handleChange}
                className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                required
              />
            </div>
            <div>
              <label
                htmlFor="counterID"
                className="block text-sm font-medium text-gray-700"
              >
                Counter ID
              </label>
              <select
                name="counterID"
                id="counterID"
                value={staff.counterID}
                onChange={handleChange}
                className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                required
              >
                {counters.map((counter) => (
                  <option key={counter.id} value={counter.id}>
                    {counter.id}
                  </option>
                ))}
              </select>
            </div>
          </div>
          <div className="mt-6 flex justify-end">
            <button
              type="submit"
              className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
            >
              Save Changes
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default EditStaff;
