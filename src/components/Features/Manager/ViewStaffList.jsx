import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import ManagerMenu from "./ManagerMenu";
import { ToastContainer, toast } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

// Helper function to format date to 'yyyy-MM-dd'
const formatDate = (date) => {
  return date.toISOString().split('T')[0];
};

// Helper function to get the first day of the current month
const getStartOfMonth = () => {
  const now = new Date();
  return new Date(now.getFullYear(), now.getMonth(), 1);
};

// Helper function to get the last day of the current month
const getEndOfMonth = () => {
  const now = new Date();
  return new Date(now.getFullYear(), now.getMonth() + 1, 0);
};

export default function ViewStaffList() {
  const [staffs, setStaffs] = useState([]);
  const [startDate, setStartDate] = useState(getStartOfMonth());
  const [endDate, setEndDate] = useState(getEndOfMonth());
  const [error, setError] = useState('');
  const [showConfirmation, setShowConfirmation] = useState(false);
  const [staffToUpdate, setStaffToUpdate] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetchStaffs(startDate, endDate);
  }, [startDate, endDate]);

  const fetchStaffs = async (startDate, endDate) => {
    try {
      const token = localStorage.getItem('token');
      const headers = { Authorization: `Bearer ${token}` };
      const response = await axios.get('http://localhost:8080/api/v2/accounts/STAFF', {
        headers,
        params: {
          startDate: formatDate(startDate),
          endDate: formatDate(endDate),
        },
      });
      setStaffs(response.data);
    } catch (err) {
      setError('Error fetching staff data');
    }
  };

  const handleEditClick = (id) => {
    navigate(`/edit-staff/${id}`);
  };

  const handleStatusChange = async (id) => {
    setShowConfirmation(true);
    setStaffToUpdate(id);
  };

  const confirmStatusChange = async () => {
    const token = localStorage.getItem('token');
    try {
      await axios.put(
        `https://jewelrysalesystem-backend.onrender.com/api/v2/accounts/STAFF/status?ID=${staffToUpdate}`, {}, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }
      );

      const updatedStaffs = staffs.map((staff) => {
        if (staff.id === staffToUpdate) {
          return { ...staff, status: !staff.status };
        }
        return staff;
      });

      setStaffs(updatedStaffs);
      setShowConfirmation(false);
      setStaffToUpdate(null);
      toast.success("Status updated successfully!");
    } catch (error) {
      console.error(
        "Error updating status",
        error.response ? error.response.data : error.message
      );
      toast.error("Failed to update status!");
    }
  };

  const cancelStatusChange = () => {
    setShowConfirmation(false);
    setStaffToUpdate(null);
  };

  return (
    <>
      <ManagerMenu />
      <div className="bg-gray-100 min-h-screen p-6">
        <div className="container mx-auto bg-white rounded-lg shadow-lg p-6">
          <h1 className="text-3xl font-semibold text-gray-800 mb-4">Staff List</h1>
          {error && <p className="text-red-600 mb-4">{error}</p>}
          <div className="flex flex-col sm:flex-row sm:justify-between mb-6">
            <div className="mb-4 sm:mb-0">
              <label className="block text-gray-700 text-sm font-medium mb-2" htmlFor="start-date">
                Start Date:
              </label>
              <DatePicker
                selected={startDate}
                onChange={(date) => setStartDate(date)}
                dateFormat="yyyy-MM-dd"
                className="border border-gray-300 rounded-lg p-2 w-full sm:w-48"
              />
            </div>
            <div>
              <label className="block text-gray-700 text-sm font-medium mb-2" htmlFor="end-date">
                End Date:
              </label>
              <DatePicker
                selected={endDate}
                onChange={(date) => setEndDate(date)}
                dateFormat="yyyy-MM-dd"
                className="border border-gray-300 rounded-lg p-2 w-full sm:w-48"
              />
            </div>
          </div>
          <table className="min-w-full divide-y divide-gray-200">
            <thead>
              <tr className="bg-gray-50">
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Full Name</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Role ID</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Gender</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Contact Information</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Counter ID</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Sales</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Action</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {staffs.map((staff) => (
                <tr key={staff.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{staff.id}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{staff.fullName}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{staff.roleID}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{staff.email}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{staff.gender}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{staff.contactInfo}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{staff.counterID}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm">
                    {staff.status ? (
                      <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                        Active
                      </span>
                    ) : (
                      <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">
                        Inactive
                      </span>
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{staff.sale ? staff.sale.toFixed(2) : 'N/A'}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button
                      onClick={() => handleEditClick(staff.id)}
                      className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleStatusChange(staff.id)}
                      className="ml-2 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500"
                    >
                      Change Status
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
      {showConfirmation && (
        <div className="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-75">
          <div className="bg-white rounded-lg shadow-lg p-6">
            <p className="text-lg font-medium text-gray-800">Do you want to change status?</p>
            <div className="mt-4 flex justify-center space-x-4">
              <button
                onClick={cancelStatusChange}
                className="px-4 py-2 bg-gray-300 text-gray-700 rounded-lg hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-400"
              >
                No
              </button>
              <button
                onClick={confirmStatusChange}
                className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500"
              >
                Yes
              </button>
            </div>
          </div>
        </div>
      )}
      <ToastContainer />
    </>
  );
}
