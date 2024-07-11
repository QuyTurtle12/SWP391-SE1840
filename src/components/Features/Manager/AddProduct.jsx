import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import ManagerMenu from './ManagerMenu';

const AddProduct = () => {
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [refundPrice, setRefundPrice] = useState('');
  const [description, setDescription] = useState('');
  const [goldWeight, setGoldWeight] = useState('');
  const [laborCost, setLaborCost] = useState('');
  const [stoneCost, setStoneCost] = useState('');
  const [stock, setStock] = useState('');
  const [categoryName, setCategoryName] = useState('');
  const [img, setImg] = useState('');
  const [promotionID, setPromotionID] = useState('');
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
      const response = await axios.get('https://jewelrysalesystem-backend.onrender.com/api/categories', {
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
      const response = await axios.get('https://jewelrysalesystem-backend.onrender.com/api/v2/promotions', {
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
    const specialCharPattern = /[!@#$%^&*(),.?":{}|<>]/;
    if (parseFloat(price) < 0 || parseFloat(refundPrice) < 0 || parseFloat(goldWeight) < 0 || parseFloat(laborCost) < 0 || parseFloat(stoneCost) < 0 || parseInt(stock) < 0) {
      notifyError('Numeric values cannot be below zero.');
      return false;
    }
    if (specialCharPattern.test(name) || specialCharPattern.test(description) ) {
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
      price: parseFloat(price),
      refundPrice: parseFloat(refundPrice),
      description,
      goldWeight: parseFloat(goldWeight),
      laborCost: parseFloat(laborCost),
      stoneCost: parseFloat(stoneCost),
      stock: parseInt(stock),
      categoryName,
      img,
      promotionID: parseInt(promotionID)
    });

    try {
      const response = await axios.post(`https://jewelrysalesystem-backend.onrender.com/api/v2/products?${params.toString()}`, null, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.status === 201) {
        notifySuccess();
        navigate('/productlist2');
        // Optionally reset form fields
        setName('');
        setPrice('');
        setRefundPrice('');
        setDescription('');
        setGoldWeight('');
        setLaborCost('');
        setStoneCost('');
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
      <div className="min-h-screen bg-tiffany flex flex-col justify-center py-12 sm:px-6 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-md">
          <img
            className="mx-auto h-10 w-auto"
            src="https://www.svgrepo.com/show/301692/login.svg"
            alt="Workflow"
          />
          <h2 className="mt-6 text-center text-3xl leading-9 font-extrabold text-gray-900">
            Create new product
          </h2>
        </div>

        <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
          <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
            <form onSubmit={handleSubmit}>
              <div className='mt-6'>
                <label className="block text-sm font-medium leading-5 text-gray-700">
                  Name
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    type="text"
                    placeholder="Name"
                    required
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                  />
                </div>
              </div>
              <div className='mt-6'>
                <label className="block text-sm font-medium leading-5 text-gray-700">
                  Price
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    type="number"
                    placeholder="Price"
                    required
                    value={price}
                    onChange={(e) => setPrice(e.target.value)}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                  />
                </div>
              </div>
              <div className='mt-6'>
                <label className="block text-sm font-medium leading-5 text-gray-700">
                  Refund Price
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    type="number"
                    placeholder="Refund Price"
                    required
                    value={refundPrice}
                    onChange={(e) => setRefundPrice(e.target.value)}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                  />
                </div>
              </div>
              <div className='mt-6'>
                <label className="block text-sm font-medium leading-5 text-gray-700">
                  Description
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    type="text"
                    placeholder="Description"
                    required
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                  />
                </div>
              </div>
              <div className='mt-6'>
                <label className="block text-sm font-medium leading-5 text-gray-700">
                  Gold Weight
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    type="number"
                    placeholder="Gold Weight"
                    required
                    value={goldWeight}
                    onChange={(e) => setGoldWeight(e.target.value)}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                  />
                </div>
              </div>
              <div className='mt-6'>
                <label className="block text-sm font-medium                 leading-5 text-gray-700">
                  Labor Cost
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    type="number"
                    placeholder="Labor Cost"
                    required
                    value={laborCost}
                    onChange={(e) => setLaborCost(e.target.value)}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                  />
                </div>
              </div>
              <div className='mt-6'>
                <label className="block text-sm font-medium leading-5 text-gray-700">
                  Stone Cost
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    type="number"
                    placeholder="Stone Cost"
                    required
                    value={stoneCost}
                    onChange={(e) => setStoneCost(e.target.value)}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                  />
                </div>
              </div>
              <div className='mt-6'>
                <label className="block text-sm font-medium leading-5 text-gray-700">
                  Stock
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    type="number"
                    placeholder="Stock"
                    required
                    value={stock}
                    onChange={(e) => setStock(e.target.value)}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                  />
                </div>
              </div>
              <div className='mt-6'>
                <label className="block text-sm font-medium leading-5 text-gray-700">
                  Category
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <select
                    required
                    value={categoryName}
                    onChange={(e) => setCategoryName(e.target.value)}
                    className="block appearance-none w-full bg-white border border-gray-300 text-gray-700 py-2 px-4 pr-8 rounded leading-tight focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
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
                <label className="block text-sm font-medium leading-5 text-gray-700">
                  Image URL
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    type="text"
                    placeholder="Image URL"
                    required
                    value={img}
                    onChange={(e) => setImg(e.target.value)}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                  />
                </div>
              </div>
              <div className='mt-6'>
                <label className="block text-sm font-medium leading-5 text-gray-700">
                  Promotion
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <select
                    required
                    value={promotionID}
                    onChange={(e) => setPromotionID(e.target.value)}
                    className="block appearance-none w-full bg-white border border-gray-300 text-gray-700 py-2 px-4 pr-8 rounded leading-tight focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
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
                <button
                  type="submit"
                  className="w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-500 focus:outline-none focus:shadow-outline-blue focus:border-indigo-700 active:bg-indigo-700 transition duration-150 ease-in-out"
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

