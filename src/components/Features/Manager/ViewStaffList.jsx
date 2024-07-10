import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import ManagerMenu from "./ManagerMenu";
import { ToastContainer, toast } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

export default function ViewStaffList() {
  const [staffs, setStaffs] = useState([]);
  const navigate = useNavigate();
  const [showConfirmation, setShowConfirmation] = useState(false);
  const [staffToUpdate, setStaffToUpdate] = useState(null);

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/v2/accounts/STAFF")
      .then((response) => {
        console.log(response.data);
        setStaffs(response.data);
      })
      .catch((error) => console.error("Error at fetching data", error));
  }, []);

  const handleEditClick = (id) => {
    navigate(`/edit-staff/${id}`);
  };

  const handleStatusChange = async (id) => {
    setShowConfirmation(true);
    setStaffToUpdate(id);
  };

  const confirmStatusChange = async () => {
    try {
      await axios.put(
        `http://localhost:8080/api/v2/accounts/STAFF/status?ID=${staffToUpdate}`
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
      <div className="bg-tiffany view-manager-list flex justify-center h-full ">
        <div className="justify-between items-center px-10">
          <table className="min-w-full divide-y divide-gray-200 ">
            <thead>
              <tr>
                <th className="px-6 py-3 font-bold text-left text-xs text-black uppercase tracking-wider">
                  ID
                </th>
                <th className="px-6 py-3 font-bold text-left text-xs text-black uppercase tracking-wider">
                  Full Name
                </th>
                <th className="px-6 py-3 font-bold text-left text-xs text-black uppercase tracking-wider">
                  Role ID
                </th>
                <th className="px-6 py-3 font-bold text-left text-xs text-black uppercase tracking-wider">
                  Email
                </th>
                <th className="px-6 py-3 font-bold text-left text-xs text-black uppercase tracking-wider">
                  Gender
                </th>
                <th className="px-6 py-3 font-bold text-left text-xs text-black uppercase tracking-wider">
                  Contact Information
                </th>
                <th className="px-6 py-3 font-bold text-left text-xs text-black uppercase tracking-wider">
                  Counter ID
                </th>
                <th className="px-6 py-3 font-bold text-left text-xs text-black uppercase tracking-wider">
                  Status
                </th>
                <th className="px-6 py-3 font-bold text-left text-xs text-black uppercase tracking-wider">
                  Action
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-black">
              {staffs.map((staff) => (
                <tr key={staff.id}>
                  <td className="px-6 py-4 whitespace-nowrap">{staff.id}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{staff.fullName}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{staff.roleID}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{staff.email}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{staff.gender}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{staff.contactInfo}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{staff.counterID}</td>
                  <td className="px-6 py-4 whitespace-nowrap">
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
                  <td className="px-6 py-4 whitespace-nowrap">
                    <button
                      onClick={() => handleEditClick(staff.id)}
                      className="px-4 py-2 font-medium text-white bg-blue-600 rounded-md hover:bg-blue-500 focus:outline-none focus:shadow-outline-blue active:bg-blue-600 transition duration-150 ease-in-out"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleStatusChange(staff.id)}
                      className="ml-2 px-4 py-2 font-medium text-white bg-red-600 rounded-md hover:bg-red-500 focus:outline-none focus:shadow-outline-blue active:bg-red-600 transition duration-150 ease-in-out"
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
          <div className="bg-white rounded-lg p-8">
            <p>Do you want to change status?</p>
            <div className="mt-4 flex justify-center space-x-4">
              <button
                onClick={cancelStatusChange}
                className="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-400"
              >
                No
              </button>
              <button
                onClick={confirmStatusChange}
                className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-600"
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


