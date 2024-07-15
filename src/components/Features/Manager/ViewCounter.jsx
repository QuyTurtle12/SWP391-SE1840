import React, { useState, useEffect } from 'react';
import axios from 'axios';
import AddCounter from './AddCounter';
import ManagerMenu from './ManagerMenu';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

// Helper function to format date to 'yyyy-MM-dd'
const formatDate = (date) => {
  return date.toISOString().split('T')[0];
};

// Helper function to get the first day of the current month
const getStartOfMonth = () => {
  const now = new Date();
  return new Date(now.getFullYear(), now.getMonth(), 1);
};

// Helper function to get the last day of the current month
const getEndOfMonth = () => {
  const now = new Date();
  return new Date(now.getFullYear(), now.getMonth() + 1, 0);
};

const ViewCounter = () => {
  const [counters, setCounters] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [startDate, setStartDate] = useState(getStartOfMonth());
  const [endDate, setEndDate] = useState(getEndOfMonth());
  const [salesData, setSalesData] = useState([]);
  
  useEffect(() => {
    fetchCounters(startDate, endDate);
  }, [startDate, endDate]);

  const fetchCounters = async (startDate, endDate) => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token'); // Retrieve token from localStorage
      const headers = { Authorization: `Bearer ${token}` };
      const response = await axios.get('https://jewelrysalesystem-backend.onrender.com/api/v2/counters', {
        headers,
        params: {
          startDate: formatDate(startDate),
          endDate: formatDate(endDate),
        }
      });
      setCounters(response.data);
      setSalesData(response.data); // Sales data is included in the same response
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
      await axios.delete(`https://jewelrysalesystem-backend.onrender.com/api/v2/counters?id=${id}`, { headers });
      fetchCounters(startDate, endDate); // Refresh the counter list
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
      await axios.put(`https://jewelrysalesystem-backend.onrender.com/api/v2/counters/${id}/status`, null, { headers });
      fetchCounters(startDate, endDate); // Refresh the counter list
    } catch (err) {
      setError('Error updating counter status');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col min-h-screen h-full overflow-hidden">
      <ManagerMenu />
      <div className="container mx-auto p-4">
        <h1 className="text-2xl font-bold mb-4">Counter List</h1>
        {error && <p className="text-red-500">{error}</p>}
        <AddCounter onAdd={() => fetchCounters(startDate, endDate)} />
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="start-date">
            Start Date:
          </label>
          <DatePicker
            selected={startDate}
            onChange={(date) => setStartDate(date)}
            dateFormat="yyyy-MM-dd"
            className="border border-gray-300 rounded p-2"
          />
          <label className="block text-gray-700 text-sm font-bold mb-2 mt-4" htmlFor="end-date">
            End Date:
          </label>
          <DatePicker
            selected={endDate}
            onChange={(date) => setEndDate(date)}
            dateFormat="yyyy-MM-dd"
            className="border border-gray-300 rounded p-2"
          />
        </div>
        <table className="min-w-full bg-white border border-gray-200 mb-4">
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
                <td className="py-2 px-4 border-b">{counter.sale !== null ? `$${counter.sale.toFixed(2)}` : 'Error fetching sale'}</td>
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
        <div className="bg-gray-100 p-4 rounded-lg mt-4">
          <h2 className="text-xl font-bold mb-2">Sales Data for {startDate.toLocaleDateString('en-US', { year: 'numeric', month: 'long' })}</h2>
          {loading ? (
            <p>Loading...</p>
          ) : salesData.length > 0 ? (
            <ul>
              {salesData.map((data) => (
                <li key={data.id} className="border-b py-2">
                  Counter ID: {data.id}, Total Sales: ${data.sale.toFixed(2)}
                </li>
              ))}
            </ul>
          ) : (
            <p>No sales data available for this period.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default ViewCounter;
