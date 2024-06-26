import axios from "axios";
import React, { useEffect, useState } from "react";

import { useNavigate } from "react-router-dom";
import ManagerMenu from "./ManagerMenu";

function ViewManagerList() {
  const [staffs, setStaffs] = useState([]);
  const navigate = useNavigate();
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false); // State để điều khiển hiển thị popup xác nhận
  const [staffToDelete, setStaffToDelete] = useState(null); // State để lưu thông tin của manager đang được chọn để xóa
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/account/STAFF/list")
      .then((response) => {
        console.log(response.data);
        setStaffs(response.data);
      })
      .catch((error) => console.error("Error at fetching data", error));
  }, []);

  const handleEditClick = (id) => {
    navigate(`/edit-staff/${id}`);
  };

 const handleDelete = async (id) => {
    try {
      setShowDeleteConfirmation(true); // Hiển thị popup xác nhận
      setStaffToDelete(id); // Lưu thông tin của manager đang được chọn để xóa
    } catch (error) {
      console.error(
        "Error updating status",
        error.response ? error.response.data : error.message
      );
    }
  };

  const confirmDelete = async () => {
    try {
      // Thực hiện xóa
      await axios.put(
        `http://localhost:8080/api/account/STAFF/change-status?ID=${staffToDelete}`
      );
      // Cập nhật trạng thái của staff trong mảng staff
      const updatedStaffs = staffs.map(staff => {
        if (staff.id === staffToDelete) {
          // Đảo ngược trạng thái của manager
          return { ...staff, status: !staff.status };
        }
        return staff;
      });
      // Cập nhật mảng managers với trạng thái mới
      setStaffs(updatedStaffs);
      // Đặt lại state và thông tin của manager để xóa
      setShowDeleteConfirmation(false);
      setStaffToDelete(null);
    } catch (error) {
      console.error(
        "Error updating status",
        error.response ? error.response.data : error.message
      );
    }
  };

  const cancelDelete = () => {
    // Đặt lại state và thông tin của manager để xóa
    setShowDeleteConfirmation(false);
    setStaffToDelete(null);
  };
  
  return (
    <>
      <ManagerMenu/>
      <div className="bg-tiffany view-manager-list flex justify-center h-full ">
        <div className="justify-between items-center px-10">
          <table className="min-w-full divide-y divide-gray-200 ">
            <thead>
              <tr>
                <th className="px-6 py-3 font-bold text-left text-xs  text-black uppercase tracking-wider">
                  ID
                </th>
                <th className="px-6 py-3 font-bold text-left text-xs  text-black uppercase tracking-wider">
                  Full Name
                </th>
                <th className="px-6 py-3 font-bold text-left text-xs  text-black uppercase tracking-wider">
                  Role ID
                </th>
                <th className="px-6 py-3 font-bold text-left text-xs  text-black uppercase tracking-wider">
                  Email
                </th>
                <th className="px-6 py-3 font-bold text-left text-xs  text-black uppercase tracking-wider">
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
            <tbody className=" bg-white divide-y divide-black">
              {staffs.map((staff) => (
                <tr key={staff.id}>
                  <td className="px-6 py-4 whitespace-nowrap">{staff.id}</td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {staff.fullName}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {staff.roleID}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {staff.email}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {staff.gender}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {staff.contactInfo}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {staff.counterID}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {staff.status === true ? (
                      <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                        Active
                      </span>
                    ) : (
                      <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">
                        Inactive
                      </span>
                    )}
                  </td>{" "}
                  <td className="px-6 py-4 whitespace-nowrap">
                    <button
                      onClick={() => handleEditClick(staff.id)}
                      className=" px-4 py-2 font-medium text-white bg-blue-600 rounded-md hover:bg-blue-500 focus:outline-none focus:shadow-outline-blue active:bg-blue-600 transition duration-150 ease-in-out"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleDelete(staff.id)}
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
       {/* Popup xác nhận */}
       {showDeleteConfirmation && (
        <div className="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-75">
          <div className="bg-white rounded-lg p-8">
            <p>Do you want to change status?</p>
            <div className="mt-4 flex justify-center space-x-4">
              <button
                onClick={cancelDelete}
                className="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-400"
              >
                No
              </button>
              <button
                onClick={confirmDelete}
                className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-600"
              >
                Yes
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}

export default ViewManagerList;
