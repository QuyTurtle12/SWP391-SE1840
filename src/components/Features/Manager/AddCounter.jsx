import React, { useState } from 'react';
import axios from 'axios';

const AddCounter = ({ onAdd }) => {
  const [loading, setLoading] = useState(false); // State for loading indicator
  const [error, setError] = useState('');

  const addCounter = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token'); // Retrieve token from localStorage
      const headers = { Authorization: `Bearer ${token}` };
      await axios.post('https://jewelrysalesystem-backend.onrender.com/api/v2/counters', null, { headers });
      onAdd(); // Refresh the counter list after adding a new counter
      // Optionally, clear any previous errors on success
      setError('');
    } catch (err) {
      console.error('Error adding counter', err);
      setError('Failed to add counter');
    } finally {
      setLoading(false);
    }
  };


  return (
    <button
      onClick={addCounter}
      className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 mb-4"
    >
      Add Counter
    </button>
  );
};

export default AddCounter;
