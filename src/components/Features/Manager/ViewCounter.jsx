import React, { useState, useEffect } from 'react';
import axios from 'axios';
import AddCounter from './AddCounter';
import ManagerMenu from './ManagerMenu';

const ViewCounter = () => {
  const [counters, setCounters] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false); // State for loading indicator

  useEffect(() => {
    fetchCounters();
  }, []);

  const fetchCounters = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token'); // Retrieve token from localStorage
      const headers = { Authorization: `Bearer ${token}` };
      const response = await axios.get('http://localhost:8080/api/v2/counters', { headers });
      setCounters(response.data);
    } catch (err) {
      setError('Error fetching counters');
    } finally {
      setLoading(false);
    }
  };

  const deleteCounter = async (id) => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      const headers = { Authorization: `Bearer ${token}` };
      await axios.delete(`http://localhost:8080/api/v2/counters?id=${id}`, { headers });
      fetchCounters(); // Refresh the counter list
    } catch (err) {
      setError('Error deleting counter');
    } finally {
      setLoading(false);
    }
  };

  const updateCounterStatus = async (id) => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      const headers = { Authorization: `Bearer ${token}` };
      await axios.put(`http://localhost:8080/api/v2/counters/${id}/status`, null, { headers });
      fetchCounters(); // Refresh the counter list
    } catch (err) {
      setError('Error updating counter status');
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className="flex flex-col min-h-screen h-screen overflow-hidden">
      <ManagerMenu/>
    <div className="container mx-auto p-4">
      
      <h1 className="text-2xl font-bold mb-4">Counter List</h1>
      {error && <p className="text-red-500">{error}</p>}
      <AddCounter onAdd={fetchCounters} />
      <table className="min-w-full bg-white border border-gray-200">
        <thead>
          <tr>
            <th className="py-2 px-4 border-b">ID</th>
            <th className="py-2 px-4 border-b">Sale</th>
            <th className="py-2 px-4 border-b">Status</th>
            <th className="py-2 px-4 border-b">Actions</th>
          </tr>
        </thead>
        <tbody>
          {counters.map(counter => (
            <tr key={counter.id} className="hover:bg-gray-100">
              <td className="py-2 px-4 border-b">{counter.id}</td>
              <td className="py-2 px-4 border-b">{counter.sale}</td>
              <td className="py-2 px-4 border-b">{counter.status ? 'Active' : 'Inactive'}</td>
              <td className="py-2 px-4 border-b space-x-2">
                <button
                  onClick={() => deleteCounter(counter.id)}
                  className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
                >
                  Delete
                </button>
                <button
                  onClick={() => updateCounterStatus(counter.id)}
                  className="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600"
                >
                  Update Status
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
    </div>
  );
};

export default ViewCounter;
