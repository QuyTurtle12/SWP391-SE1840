import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import ManagerMenu from './ManagerMenu';

const AddProduct = () => {
  const [ID, setID] = useState('');
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [refundPrice, setRefundPrice] = useState('');
  const [description, setDescription] = useState('');
  const [goldWeight, setGoldWeight] = useState('');
  const [laborCost, setLaborCost] = useState('');
  const [stoneCost, setStoneCost] = useState('');
  const [stock, setStock] = useState('');
  const [categoryID, setCategoryID] = useState('');
  const [img, setImg] = useState('');
  const [promotionID, setPromotionID] = useState('');

  const navigate = useNavigate();

  const notifySuccess = () => toast.success('Product added successfully!');
  const notifyError = () => toast.error('Failed to add product. Please try again.');

  const handleSubmit = async (e) => {
    e.preventDefault();

    const token = localStorage.getItem('token');
    if (!token) {
      toast.error('User not authenticated. Please login.');
      navigate('/login'); // Redirect to login page if token is missing
      return;
    }

    const data = {
      ID: parseInt(ID),
      name,
      price: parseFloat(price),
      refundPrice: parseFloat(refundPrice),
      description,
      goldWeight: parseFloat(goldWeight),
      laborCost: parseFloat(laborCost),
      stoneCost: parseFloat(stoneCost),
      stock: parseInt(stock),
      categoryID: parseInt(categoryID),
      img,
      promotionID: parseInt(promotionID)
    };

    try {
      const response = await axios.post('https://jewelrysalesystem-backend.onrender.com/api/v2/products', data, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.status === 200) {
        notifySuccess();
        navigate('/productlist2');
        // Optionally reset form fields
        setID('');
        setName('');
        setPrice('');
        setRefundPrice('');
        setDescription('');
        setGoldWeight('');
        setLaborCost('');
        setStoneCost('');
        setStock('');
        setCategoryID('');
        setImg('');
        setPromotionID('');
      } else {
        notifyError();
      }
    } catch (error) {
      console.error('Error adding product:', error.response ? error.response.data : error.message);
      notifyError();
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
              <div>
                <label className="block text-sm font-medium leading-5 text-gray-700">
                  ID
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    id="ID"
                    name="ID"
                    type="number"
                    placeholder="ID"
                    required
                    value={ID}
                    onChange={(e) => setID(e.target.value)}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                  />
                </div>
              </div>
              <div className='mt-6'>
                <label className="block text-sm font-medium leading-5 text-gray-700">
                  Name
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    id="name"
                    name="name"
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
                    id="price"
                    name="price"
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
                    id="refundPrice"
                    name="refundPrice"
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
                    id="description"
                    name="description"
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
                    id="goldWeight"
                    name="goldWeight"
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
                <label className="block text-sm font-medium leading-5 text-gray-700">
                  Labor Cost
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    id="laborCost"
                    name="laborCost"
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
                    id="stoneCost"
                    name="stoneCost"
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
                    id="stock"
                    name="stock"
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
                  Category ID
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    id="categoryID"
                    name="categoryID"
                    type="number"
                    placeholder="Category ID"
                    required
                    value={categoryID}
                    onChange={(e) => setCategoryID(e.target.value)}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                  />
                </div>
              </div>
              <div className='mt-6'>
                <label className="block text-sm font-medium leading-5 text-gray-700">
                  Image URL
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    id="img"
                    name="img"
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
                  Promotion ID
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <input
                    id="promotionID"
                    name="promotionID"
                    type="number"
                    placeholder="Promotion ID"
                    required
                    value={promotionID}
                    onChange={(e) => setPromotionID(e.target.value)}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                  />
                </div>
              </div>
              <div className="mt-6">
                <button onClick={notifySuccess}
                  type="submit"
                  className="block w-full py-2 text-center text-white bg-teal-500 border border-teal-500 rounded  hover:text-teal-500 transition uppercase font-roboto font-medium"
                >
                  Create Product
                  <ToastContainer />
                </button >
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AddProduct;
