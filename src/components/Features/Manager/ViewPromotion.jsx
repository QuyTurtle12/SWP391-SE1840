import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Modal from './PromotionForm';
import ManagerMenu from './ManagerMenu';

const ViewPromotion = () => {
  const [promotions, setPromotions] = useState([]);
  const [description, setDescription] = useState('');
  const [discountRate, setDiscountRate] = useState(0);
  const [selectedPromotion, setSelectedPromotion] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false); // Loading indicator state

  useEffect(() => {
    fetchPromotions();
  }, []);

  const fetchPromotions = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token'); // Retrieve token from localStorage
      const headers = { Authorization: `Bearer ${token}` };
      const response = await axios.get('https://jewelrysalesystem-backend.onrender.com/api/v2/promotions', { headers });
      setPromotions(response.data);
    } catch (err) {
      setError('Error fetching promotions');
      toast.error('Error fetching promotions');
    } finally {
      setLoading(false);
    }
  };

  const addPromotion = async () => {
  if (discountRate <= 0 || discountRate >= 1) {
      toast.error('Discount rate cannot be below 0 and above 1');
    
    
      return;
    }
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      const headers = { Authorization: `Bearer ${token}` };
      await axios.post('https://jewelrysalesystem-backend.onrender.com/api/v2/promotions', null, {
        headers,
        params: {
          description,
          discountRate
        }
      });
      fetchPromotions();
      toast.success('Promotion added successfully');
    } catch (err) {
      setError('Error adding promotion');
      toast.error('Error adding promotion');
    } finally {
      setLoading(false);
    }
  };

  const updatePromotionStatus = async (id) => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      const headers = { Authorization: `Bearer ${token}` };
      await axios.put(`https://jewelrysalesystem-backend.onrender.com/api/v2/promotions/status?id=${id}`, null, { headers });
      fetchPromotions();
      toast.success('Promotion status updated successfully');
    } catch (err) {
      setError('Error updating promotion status');
      toast.error('Error updating promotion status');
    } finally {
      setLoading(false);
    }
  };

  const openUpdateModal = (promotion) => {
    setSelectedPromotion(promotion);
    setDescription(promotion.description);
    setDiscountRate(promotion.discountRate);
    setIsModalOpen(true);
  };

  const closeUpdateModal = () => {
    setSelectedPromotion(null);
    setDescription('');
    setDiscountRate(0);
    setIsModalOpen(false);
  };

  const updatePromotion = async (e) => {
    e.preventDefault();
    if (discountRate <= 0|| discountRate >= 1) {
      toast.error('Discount rate cannot be below 0 and above 1');
      return;
    }
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      const headers = { Authorization: `Bearer ${token}` };
      await axios.put(`https://jewelrysalesystem-backend.onrender.com/api/v2/promotions/${selectedPromotion.id}`, null, {
        headers,
        params: {
          description,
          discountRate
        }
      });
      fetchPromotions();
      closeUpdateModal();
      toast.success('Promotion updated successfully');
    } catch (err) {
      setError('Error updating promotion');
      toast.error('Error updating promotion');
    } finally {
      setLoading(false);
    }
  };


  return (
    <div className="flex flex-col min-h-screen h-screen overflow-hidden">
      <ManagerMenu />
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Promotion List</h1>
      {error && <p className="text-red-500">{error}</p>}
      <div className="mb-4">
        <input
          type="text"
          placeholder="Description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          className="border p-2 mr-2"
        />
        Discount Rate
        <input
          type="number"
          placeholder="Discount Rate"
          value={discountRate}
          onChange={(e) => setDiscountRate(e.target.value)}
          className="border p-2 mr-2"
        />
        <button onClick={addPromotion} className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
          Add Promotion
        </button>
      </div>
      <table className="min-w-full bg-white border border-gray-200">
        <thead>
          <tr>
            <th className="py-2 px-4 border-b">ID</th>
            <th className="py-2 px-4 border-b">Description</th>
            <th className="py-2 px-4 border-b">Discount Rate</th>
            <th className="py-2 px-4 border-b">Status</th>
            <th className="py-2 px-4 border-b">Actions</th>
          </tr>
        </thead>
        <tbody>
          {promotions.map(promotion => (
            <tr key={promotion.id} className="hover:bg-gray-100">
              <td className="py-2 px-4 border-b">{promotion.id}</td>
              <td className="py-2 px-4 border-b">{promotion.description}</td>
              <td className="py-2 px-4 border-b">{promotion.discountRate}</td>
              <td className="py-2 px-4 border-b">{promotion.status ? 'Active' : 'Inactive'}</td>
              <td className="py-2 px-4 border-b space-x-2">
               
                <button
                  onClick={() => updatePromotionStatus(promotion.id)}
                  className="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600"
                >
                  Update Status
                </button>
                <button
                  onClick={() => openUpdateModal(promotion)}
                  className="bg-yellow-500 text-white px-2 py-1 rounded hover:bg-yellow-600"
                >
                  Update All Info
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <Modal
        isOpen={isModalOpen}
        onClose={closeUpdateModal}
        onSubmit={updatePromotion}
        description={description}
        setDescription={setDescription}
        discountRate={discountRate}
        setDiscountRate={setDiscountRate}
      />
      <ToastContainer />
    </div>
    </div>
  );
};

export default ViewPromotion;
