import axios from "axios";
import React, { useEffect, useState } from "react";
import ManagerMenu from "./ManagerMenu";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function ViewCategory() {
  const [categories, setCategories] = useState([]);
  const [error, setError] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState({ id: '', name: '' });

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = () => {
    const token = localStorage.getItem('token');
    
    axios
      .get("https://jewelrysalesystem-backend.onrender.com/api/categories", {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      .then((response) => {
        setCategories(response.data);
      })
      .catch((error) => {
        console.error("Error fetching data", error);
        setError(error);
      });
  };

  const handleEditClick = (category) => {
    setSelectedCategory(category);
    setShowModal(true);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setSelectedCategory((prevCategory) => ({ ...prevCategory, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const token = localStorage.getItem('token');
    
    axios
      .put(`https://jewelrysalesystem-backend.onrender.com/api/categories?ID=${selectedCategory.id}&categoryName=${selectedCategory.name}`, {}, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      .then(() => {
        setShowModal(false);
        fetchCategories();
      })
      .catch((error) => {
        console.error("Error updating category", error);
        setError(error);
        toast.error("Error updating category");
      });
  };

  return (
    <div className="flex flex-col min-h-screen h-screen overflow-hidden">
      <ManagerMenu />
      <ToastContainer />
      <div className="flex-grow bg-gray-100 flex justify-center items-start overflow-y-auto">
        <div className="w-full max-w-4xl bg-white shadow-md rounded my-10 p-6">
          {error ? (
            <div className="text-red-500">Error fetching data: {error.message}</div>
          ) : (
            <>
              <table className="min-w-full bg-white">
                <thead>
                  <tr>
                    <th className="py-2 px-4 bg-gray-200 font-bold text-left text-gray-600 uppercase text-sm">ID</th>
                    <th className="py-2 px-4 bg-gray-200 font-bold text-left text-gray-600 uppercase text-sm">Name</th>
                    <th className="py-2 px-4 bg-gray-200 font-bold text-left text-gray-600 uppercase text-sm">Action</th>
                  </tr>
                </thead>
                <tbody>
                  {categories.map((category) => (
                    <tr key={category.id} className="border-b">
                      <td className="py-2 px-4">{category.id}</td>
                      <td className="py-2 px-4">{category.name}</td>
                      <td className="py-2 px-4">
                        <button
                          onClick={() => handleEditClick(category)}
                          className="bg-blue-500 text-white px-4 py-2 rounded"
                        >
                          Edit
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>

              {/* Modal */}
              {showModal && (
                <div className="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-75">
                  <div className="bg-white rounded-lg p-8 w-full max-w-md">
                    <h2 className="text-xl mb-4">Edit Category</h2>
                    <form onSubmit={handleSubmit}>
                      <div className="mb-4">
                        <label className="block text-gray-700">Name</label>
                        <input
                          type="text"
                          name="name"
                          value={selectedCategory.name}
                          onChange={handleInputChange}
                          className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm"
                        />
                      </div>
                      <div className="flex justify-end">
                        <button
                          type="button"
                          onClick={() => setShowModal(false)}
                          className="bg-gray-300 text-gray-700 px-4 py-2 rounded mr-2"
                        >
                          Cancel
                        </button>
                        <button
                          type="submit"
                          className="bg-blue-500 text-white px-4 py-2 rounded"
                        >
                          Save
                        </button>
                      </div>
                    </form>
                  </div>
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
}

export default ViewCategory;
