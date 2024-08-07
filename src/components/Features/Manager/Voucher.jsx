import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import ManagerMenu from './ManagerMenu';
import Modal from './Modal';  // Import the Modal component

export default function Voucher() {
    const [promotions, setPromotions] = useState([]);
    const [editingPromotion, setEditingPromotion] = useState(null);
    const [newPromotion, setNewPromotion] = useState({
        discountName: '',
        discountDescription: '',
        discountType: 'Normal',
        discountCondition: 'None',
        discountRate: ''
    });
    const [isAdding, setIsAdding] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const token = localStorage.getItem('token');

    useEffect(() => {
        fetchPromotions();
    }, []);

    useEffect(() => {
        if (editingPromotion && editingPromotion.discountType === 'Normal') {
            setEditingPromotion({ ...editingPromotion, discountCondition: 'None' });
        }
    }, [editingPromotion?.discountType]);

    useEffect(() => {
        if (newPromotion.discountType === 'Normal') {
            setNewPromotion({ ...newPromotion, discountCondition: 'None' });
        }
    }, [newPromotion.discountType]);

    const fetchPromotions = async () => {
        try {
            const response = await axios.get('https://jewelrysalesystem-backend.onrender.com/api/customer-promotions', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            setPromotions(response.data);
        } catch (error) {
            toast.error('Failed to fetch promotions.');
        }
    };

    const handleEdit = (promotion) => {
        setIsEditing(true); // Show Edit modal
        setEditingPromotion({ ...promotion });
        setIsAdding(false); // Ensure Add form is hidden
    };

    const validateInputs = (promotion) => {
        const { discountRate, discountCondition, discountType } = promotion;

        if (typeof discountRate === 'number' && (discountRate < 0 || discountRate > 1)) {
            toast.error('Invalid input: Discount rate must be between 0 and 1');
            return false;
        }

        if (discountType === 'Accepted Price' && discountCondition < 0) {
            toast.error('Invalid input: Condition for Accepted Price must be a non-negative number');
            return false;
        }

        if (discountCondition.includes('-') && discountType !== 'Accepted Price') {
            toast.error('Invalid input: Condition cannot contain "-"');
            return false;
        }

        return true;
    };

    const handleUpdate = async () => {
        if (!editingPromotion || !validateInputs(editingPromotion)) return;

        try {
            const { id, discountName, discountType, discountCondition, discountDescription, discountRate } = editingPromotion;
            await axios.put(`https://jewelrysalesystem-backend.onrender.com/api/customer-promotions/${id}?discountName=${discountName}&discountType=${discountType}&discountCondition=${discountCondition}&discountDescription=${discountDescription}&discountRate=${discountRate}`, null, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            toast.success('Promotion updated successfully.');
            fetchPromotions();
            setEditingPromotion(null);
            setIsEditing(false);
        } catch (error) {
            toast.error('Failed to update promotion.');
        }
    };

    const handleChangeStatus = async (id) => {
        try {
            await axios.put(`https://jewelrysalesystem-backend.onrender.com/api/customer-promotions/${id}/status`, {}, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            toast.success('Promotion status changed successfully.');
            fetchPromotions();
        } catch (error) {
            toast.error('Failed to change promotion status.');
        }
    };

    const handleAddPromotion = async () => {
        if (!validateInputs(newPromotion)) return;

        try {
            const { discountName, discountDescription, discountType, discountCondition, discountRate } = newPromotion;
            await axios.post(`https://jewelrysalesystem-backend.onrender.com/api/customer-promotions?discountName=${discountName}&discountDescription=${discountDescription}&discountType=${discountType}&discountCondition=${discountCondition}&discountRate=${discountRate}`, null, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            toast.success('Promotion added successfully.');
            fetchPromotions();
            setIsAdding(false);
            setNewPromotion({
                discountName: '',
                discountDescription: '',
                discountType: 'Normal',
                discountCondition: 'None',
                discountRate: ''
            });
        } catch (error) {
            toast.error('Failed to add promotion.');
        }
    };

    return (
        <div className="container mx-auto p-6">
            <ManagerMenu />
            <ToastContainer></ToastContainer>
            <h1 className="text-3xl font-bold mb-6 text-center">Customer Promotions</h1>
            <button
                className="bg-green-500 text-white px-4 py-2 mb-4 rounded hover:bg-green-600"
                onClick={() => {
                    setIsAdding(true); // Show Add modal
                    setIsEditing(false); // Ensure Edit form is hidden
                }}
            >
                Add Promotion
            </button>
            <table className="min-w-full bg-white border rounded shadow">
                <thead>
                    <tr>
                        <th className="py-2 px-4 border-b">ID</th>
                        <th className="py-2 px-4 border-b">Name</th>
                        <th className="py-2 px-4 border-b">Type</th>
                        <th className="py-2 px-4 border-b">Condition</th>
                        <th className="py-2 px-4 border-b">Description</th>
                        <th className="py-2 px-4 border-b">Rate</th>
                        <th className="py-2 px-4 border-b">Status</th>
                        <th className="py-2 px-4 border-b">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {promotions.map((promotion) => (
                        <tr key={promotion.id} className="border-t">
                            <td className="py-2 px-4">{promotion.id}</td>
                            <td className="py-2 px-4">{promotion.discountName}</td>
                            <td className="py-2 px-4">{promotion.discountType}</td>
                            <td className="py-2 px-4">{promotion.discountCondition}</td>
                            <td className="py-2 px-4">{promotion.discountDescription}</td>
                            <td className="py-2 px-4">{promotion.discountRate}</td>
                            <td className="py-2 px-4">{promotion.status ? 'Active' : 'Inactive'}</td>
                            <td className="py-2 px-4">
                                <button
                                    className="bg-blue-500 text-white px-2 py-1 mr-2 rounded hover:bg-blue-600"
                                    onClick={() => handleEdit(promotion)}
                                >
                                    Edit
                                </button>
                                <button
                                    className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
                                    onClick={() => handleChangeStatus(promotion.id)}
                                >
                                    Change Status
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <Modal isOpen={isEditing} onClose={() => setIsEditing(false)}>
                <div className="relative">
                    <button
                        className="absolute top-0 right-0 text-gray-500 hover:text-gray-700"
                        onClick={() => setIsEditing(false)}
                    >
                        &times;
                    </button>
                    <h2 className="text-xl font-bold mb-4">Edit Promotion</h2>
                    <div className="mb-4">
                        <label className="block mb-2 font-medium">Name</label>
                        <input
                            type="text"
                            className="border p-2 w-full rounded"
                            value={editingPromotion?.discountName || ''}
                            onChange={(e) =>
                                setEditingPromotion({ ...editingPromotion, discountName: e.target.value })
                            }
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block mb-2 font-medium">Type</label>
                        <select
                            className="border p-2 w-full rounded"
                            value={editingPromotion?.discountType || ''}
                            onChange={(e) => {
                                const newType = e.target.value;
                                setEditingPromotion({
                                    ...editingPromotion,
                                    discountType: newType,
                                    discountCondition: newType === 'Normal' ? 'None' : editingPromotion.discountCondition
                                });
                            }}
                        >
                            <option value="Accepted Price">Accepted Price</option>
                            <option value="Normal">Normal</option>
                        </select>
                    </div>
                    <div className="mb-4">
                        <label className="block mb-2 font-medium">Condition</label>
                        <input
                            type="text"
                            className="border p-2 w-full rounded"
                            value={editingPromotion?.discountCondition || ''}
                            disabled={editingPromotion?.discountType === 'Normal'}
                            onChange={(e) =>
                                setEditingPromotion({ ...editingPromotion, discountCondition: e.target.value })
                            }
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block mb-2 font-medium">Description</label>
                        <input
                            type="text"
                            className="border p-2 w-full rounded"
                            value={editingPromotion?.discountDescription || ''}
                            onChange={(e) =>
                                setEditingPromotion({ ...editingPromotion, discountDescription: e.target.value })
                            }
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block mb-2 font-medium">Rate</label>
                        <input
                            type="number"
                            step="0.01"
                            className="border p-2 w-full rounded"
                            value={editingPromotion?.discountRate || ''}
                            onChange={(e) =>
                                setEditingPromotion({ ...editingPromotion, discountRate: parseFloat(e.target.value) })
                            }
                        />
                    </div>
                    <button
                        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                        onClick={handleUpdate}
                    >
                        Save
                    </button>
                </div>
            </Modal>

            <Modal isOpen={isAdding} onClose={() => setIsAdding(false)}>
                <div className="relative">
                    <button
                        className="absolute top-0 right-0 text-gray-500 hover:text-gray-700"
                        onClick={() => setIsAdding(false)}
                    >
                        &times;
                    </button>
                    <h2 className="text-xl font-bold mb-4">Add Promotion</h2>
                    <div className="mb-4">
                        <label className="block mb-2 font-medium">Name</label>
                        <input
                            type="text"
                            className="border p-2 w-full rounded"
                            value={newPromotion.discountName}
                            onChange={(e) =>
                                setNewPromotion({ ...newPromotion, discountName: e.target.value })
                            }
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block mb-2 font-medium">Type</label>
                        <select
                            className="border p-2 w-full rounded"
                            value={newPromotion.discountType}
                            onChange={(e) => {
                                const newType = e.target.value;
                                setNewPromotion({
                                    ...newPromotion,
                                    discountType: newType,
                                    discountCondition: newType === 'Normal' ? 'None' : newPromotion.discountCondition
                                });
                            }}
                        >
                            <option value="Accepted Price">Accepted Price</option>
                            <option value="Normal">Normal</option>
                        </select>
                    </div>
                    <div className="mb-4">
                        <label className="block mb-2 font-medium">Condition</label>
                        <input
                            type="text"
                            className="border p-2 w-full rounded"
                            value={newPromotion.discountCondition}
                            disabled={newPromotion.discountType === 'Normal'}
                            onChange={(e) =>
                                setNewPromotion({ ...newPromotion, discountCondition: e.target.value })
                            }
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block mb-2 font-medium">Description</label>
                        <input
                            type="text"
                            className="border p-2 w-full rounded"
                            value={newPromotion.discountDescription}
                            onChange={(e) =>
                                setNewPromotion({ ...newPromotion, discountDescription: e.target.value })
                            }
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block mb-2 font-medium">Rate</label>
                        <input
                            type="number"
                            step="0.01"
                            className="border p-2 w-full rounded"
                            value={newPromotion.discountRate}
                            onChange={(e) =>
                                setNewPromotion({ ...newPromotion, discountRate: parseFloat(e.target.value) })
                            }
                        />
                    </div>
                    <button
                        className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                        onClick={handleAddPromotion}
                    >
                        Add
                    </button>
                </div>
            </Modal>
        </div>
    );
}
