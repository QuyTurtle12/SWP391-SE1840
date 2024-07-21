import React, { useState, useEffect } from 'react';
import axios from 'axios';
import AddCounter from './AddCounter';
import ManagerMenu from './ManagerMenu';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

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
  const [dateError, setDateError] = useState('');

  useEffect(() => {
    fetchCounters(startDate, endDate);
  }, [startDate, endDate]);

  const fetchCounters = async (startDate, endDate) => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token'); // Retrieve token from localStorage
      const headers = { Authorization: `Bearer ${token}` };
      const response = await axios.get('http://localhost:8080/api/v2/counters', {
        headers,
        params: {
          startDate: formatDate(startDate),
          endDate: formatDate(endDate),
        }
      });
      const sortedCounters = response.data.sort((a, b) => a.id - b.id);
      setCounters(sortedCounters);
      setSalesData(sortedCounters); // Sales data is included in the same response
    } catch (err) {
      setError('Error fetching counters');
    } finally {
      setLoading(false);
    }
  };

  const handleStartDateChange = (date) => {
    if (date > endDate) {
      setDateError('Start date cannot be after end date.');
    } else {
      setDateError('');
      setStartDate(date);
    }
  };

  const handleEndDateChange = (date) => {
    if (date < startDate) {
      setDateError('End date cannot be before start date.');
    } else {
      setDateError('');
      setEndDate(date);
    }
  };

  const handleAddCounter = async () => {
    try {
      // Assume AddCounter component triggers fetching counters
      await fetchCounters(startDate, endDate);
      toast.success('Counter added successfully!');
    } catch (error) {
      toast.error('Error adding counter');
    }
  };

  return (
    <div className="flex flex-col min-h-screen h-full overflow-hidden">
      <ManagerMenu />
      <div className="container mx-auto p-4">
        <h1 className="text-2xl font-bold mb-4">Counter List</h1>
        {error && <p className="text-red-500">{error}</p>}
        {dateError && <p className="text-red-500">{dateError}</p>}
        <AddCounter onAdd={handleAddCounter} />
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="start-date">
            Start Date:
          </label>
          <DatePicker
            selected={startDate}
            onChange={handleStartDateChange}
            dateFormat="yyyy-MM-dd"
            className="border border-gray-300 rounded p-2 w-full"
          />
          <label className="block text-gray-700 text-sm font-bold mb-2 mt-4" htmlFor="end-date">
            End Date:
          </label>
          <DatePicker
            selected={endDate}
            onChange={handleEndDateChange}
            dateFormat="yyyy-MM-dd"
            className="border border-gray-300 rounded p-2 w-full"
          />
        </div>
        <table className="min-w-full bg-white border border-gray-200 mb-4">
          <thead>
            <tr>
              <th className="py-2 px-4 border-b text-left">ID</th>
              <th className="py-2 px-4 border-b text-left">Sale</th>
            </tr>
          </thead>
          <tbody>
            {counters.map(counter => (
              <tr key={counter.id} className="hover:bg-gray-100">
                <td className="py-2 px-4 border-b">{counter.id}</td>
                <td className="py-2 px-4 border-b">{counter.sale !== null ? `$${counter.sale.toFixed(2)}` : 'Error fetching sale'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <ToastContainer />
    </div>
  );
};

export default ViewCounter;
