import React from 'react';
import axios from 'axios';

const AddCounter = ({ onAdd }) => {
  const addCounter = async () => {
    try {
      await axios.post('http://localhost:8080/api/v2/counters');
      onAdd(); // Refresh the counter list after adding a new counter
    } catch (err) {
      console.error('Error adding counter', err);
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
