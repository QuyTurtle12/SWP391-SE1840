import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import ManagerMenu from './ManagerMenu';
import FileUpload from "./FileUpload";

const AddProduct = () => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [goldWeight, setGoldWeight] = useState('');
  const [laborCost, setLaborCost] = useState('');
  const [stoneCost, setStoneCost] = useState('');
  const [stock, setStock] = useState('');
  const [categoryName, setCategoryName] = useState('');
  const [img, setImg] = useState('');
  const [promotionID, setPromotionID] = useState('');
  const [stoneName, setStoneName] = useState('');
  const [stoneType, setStoneType] = useState('');
  const [categories, setCategories] = useState([]);
  const [promotions, setPromotions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const navigate = useNavigate();

  useEffect(() => {
    fetchCategories();
    fetchPromotions();
  }, []);

  const fetchCategories = async () => {
    const token = localStorage.getItem('token');
    try {
      const response = await axios.get('http://localhost:8080/api/categories', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      setCategories(response.data);
    } catch (error) {
      console.error("Error fetching categories", error);
      setError('Error fetching categories');
    }
  };

  const fetchPromotions = async () => {
    const token = localStorage.getItem('token');
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/api/v2/promotions', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      setPromotions(response.data);
    } catch (err) {
      console.error("Error fetching promotions", err);
      setError('Error fetching promotions');
    } finally {
      setLoading(false);
    }
  };

  const notifySuccess = () => toast.success('Product added successfully!');
  const notifyError = (message) => toast.error(message);

  const validateInputs = () => {
    const specialCharPattern = /[!@#$%^&*().":{}|<>]/;
    if (parseFloat(goldWeight) < 0 || parseFloat(laborCost) < 0 || parseFloat(stoneCost) < 0 || parseInt(stock) < 0) {
      notifyError('Numeric values cannot be below zero.');
      return false;
    }
    if (specialCharPattern.test(name) || specialCharPattern.test(description) || specialCharPattern.test(stoneName)) {
      notifyError('Special characters are not allowed.');
      return false;
    }
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateInputs()) {
      return;
    }

    const token = localStorage.getItem('token');
    if (!token) {
      notifyError('User not authenticated. Please login.');
      navigate('/login');
      return;
    }

    const params = new URLSearchParams({
      name,
      description,
      goldWeight: parseFloat(goldWeight),
      laborCost: parseFloat(laborCost),
      stoneCost: parseFloat(stoneCost),
      stoneName,
      stoneType,
      stock: parseInt(stock),
      categoryName,
      img,
      promotionID: parseInt(promotionID)
    });

    try {
      const response = await axios.post(`http://localhost:8080/api/v2/products?${params.toString()}`, null, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.status === 201) {
        notifySuccess();
        navigate('/productlist2');
        // Optionally reset form fields
        setName('');
        setDescription('');
        setGoldWeight('');
        setLaborCost('');
        setStoneCost('');
        setStoneName('');
        setStoneType('');
        setStock('');
        setCategoryName('');
        setImg('');
        setPromotionID('');
      } else {
        notifyError('Failed to add product. Please try again.');
      }
    } catch (error) {
      console.error('Error adding product:', error.response ? error.response.data : error.message);
      notifyError('Failed to add product. Please try again.');
    }
  };

  return (
    <div>
      <ManagerMenu />
      <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-md">
          <img
            className="mx-auto h-12 w-auto"
            src="https://www.svgrepo.com/show/301692/login.svg"
            alt="Workflow"
          />
          <h2 className="mt-6 text-center text-4xl font-extrabold text-gray-900">
            Add New Product
          </h2>
        </div>

        <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
          <div className="bg-white py-8 px-6 shadow sm:rounded-lg sm:px-10">
            <form onSubmit={handleSubmit}>
              <div className='mt-6'>
                <label className="block text-sm font-medium text-gray-700">
                  Name
                </label>
                <div className="mt-1">
                  <input
                    type="text"
                    placeholder="Enter product name"
                    required
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="block w-full px-4 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
              </div>
              
              <div className='mt-6'>
                <label className="block text-sm font-medium text-gray-700">
                  Description
                </label>
                <div className="mt-1">
                  <input
                    type="text"
                    placeholder="Enter product description"
                    required
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    className="block w-full px-4 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
              </div>
              
              <div className='mt-6'>
                <label className="block text-sm font-medium text-gray-700">
                  Gold Weight
                </label>
                <div className="mt-1">
                  <input
                    type="number"
                    placeholder="Enter gold weight"
                    required
                    value={goldWeight}
                    onChange={(e) => setGoldWeight(e.target.value)}
                    className="block w-full px-4 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
              </div>
              
              <div className='mt-6'>
                <label className="block text-sm font-medium text-gray-700">
                  Labor Cost
                </label>
                <div className="mt-1">
                  <input
                    type="number"
                    placeholder="Enter labor cost"
                    required
                    value={laborCost}
                    onChange={(e) => setLaborCost(e.target.value)}
                    className="block w-full px-4 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
              </div>
              
              <div className='mt-6'>
                <label className="block text-sm font-medium text-gray-700">
                  Stone Cost
                </label>
                <div className="mt-1">
                  <input
                    type="number"
                    placeholder="Enter stone cost"
                    required
                    value={stoneCost}
                    onChange={(e) => setStoneCost(e.target.value)}
                    className="block w-full px-4 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
              </div>
              
              <div className='mt-6'>
                <label className="block text-sm font-medium text-gray-700">
                  Stone Name
                </label>
                <div className="mt-1">
                  <input
                    type="text"
                    placeholder="Enter stone name"
                    required
                    value={stoneName}
                    onChange={(e) => setStoneName(e.target.value)}
                    className="block w-full px-4 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
              </div>

              <div className='mt-6'>
                <label className="block text-sm font-medium text-gray-700">
                  Stone Type
                </label>
                <div className="mt-1">
                  <select
                    required
                    value={stoneType}
                    onChange={(e) => setStoneType(e.target.value)}
                    className="block w-full bg-white border border-gray-300 text-gray-700 py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  >
                    <option value="">Select a stone type</option>
                    <option value="Normal Stone">Normal Stone</option>
                    <option value="Jewel">Jewel</option>
                  </select>
                </div>
              </div>
              
              <div className='mt-6'>
                <label className="block text-sm font-medium text-gray-700">
                  Stock
                </label>
                <div className="mt-1">
                  <input
                    type="number"
                    placeholder="Enter stock"
                    required
                    value={stock}
                    onChange={(e) => setStock(e.target.value)}
                    className="block w-full px-4 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
              </div>
              
              <div className='mt-6'>
                <label className="block text-sm font-medium text-gray-700">
                  Category
                </label>
                <div className="mt-1">
                  <select
                    required
                    value={categoryName}
                    onChange={(e) => setCategoryName(e.target.value)}
                    className="block w-full bg-white border border-gray-300 text-gray-700 py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  >
                    <option value="">Select a category</option>
                    {categories.map((category) => (
                      <option key={category.id} value={category.name}>
                        {category.name}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
              
              <div className='mt-6'>
                <label className="block text-sm font-medium text-gray-700">
                  Image URL
                </label>
                <div className="mt-1">
                  <input
                    type="text"
                    placeholder="Enter image URL"
                    required
                    value={img}
                    onChange={(e) => setImg(e.target.value)}
                    className="block w-full px-4 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
              </div>
              
              <div className='mt-6'>
                <label className="block text-sm font-medium text-gray-700">
                  Promotion
                </label>
                <div className="mt-1">
                  <select
                    required
                    value={promotionID}
                    onChange={(e) => setPromotionID(e.target.value)}
                    className="block w-full bg-white border border-gray-300 text-gray-700 py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  >
                    <option value="">Select a promotion</option>
                    {promotions.map((promotion) => (
                      <option key={promotion.id} value={promotion.id}>
                        {promotion.description}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
              
              <div className="mt-6">
                <FileUpload/>
                <button
                  type="submit"
                  className="w-full py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 transition duration-150 ease-in-out"
                >
                  Add Product
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
      <ToastContainer />
    </div>
  );
};

export default AddProduct;
