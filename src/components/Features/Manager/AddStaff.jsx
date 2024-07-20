import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import ManagerMenu from './ManagerMenu';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const AddStaff = () => {
  const [fullName, setFullName] = useState('');
  const [gender, setGender] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [contactInfo, setContactInfo] = useState('');
  const [counterID, setCounterID] = useState('');
  const [counters, setCounters] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchCounters = async () => {
      const token = localStorage.getItem('token');

      try {
        const response = await axios.get('https://jewelrysalesystem-backend.onrender.com/api/v2/counters/no-sale', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        setCounters(response.data);
      } catch (error) {
        console.error('Error fetching counters:', error);
      }
    };

    fetchCounters();
  }, []);

  const validateInput = () => {
    const nameRegex = /^[a-zA-Z\s]+$/;
    const emailRegex = /^[a-zA-Z0-9._%+-]+@(gmail\.com|yahoo\.com)$/;
    const contactInfoRegex = /^[0-9\s-]+$/;

    if (!nameRegex.test(fullName)) {
      toast.error('Full Name must contain only letters and spaces');
      return false;
    }

    if (!emailRegex.test(email)) {
      toast.error('Email must end with @gmail.com or @yahoo.com');
      return false;
    }

    if (password.trim() === '') {
      toast.error('Password cannot be empty');
      return false;
    }

    if (!contactInfoRegex.test(contactInfo)) {
      toast.error('Contact Info must contain only numbers, spaces, or dashes');
      return false;
    }

    if (counterID.trim() === '') {
      toast.error('Counter ID cannot be empty');
      return false;
    }

    return true;
  };

  const handleSubmit = async (e) => {
	e.preventDefault();
  
	if (!validateInput()) {
	  return;
	}
  
	const token = localStorage.getItem('token');
	const url = `https://jewelrysalesystem-backend.onrender.com/api/v2/accounts/STAFF?fullName=${encodeURIComponent(fullName)}&gender=${encodeURIComponent(gender)}&email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}&contactInfo=${encodeURIComponent(contactInfo)}&counterID=${parseInt(counterID)}`;
  
	try {
	  await axios.post(url, null, {
		headers: {
		  'Authorization': `Bearer ${token}`
		}
	  });
  
	  toast.success('Staff added successfully!');
  
	  setFullName('');
	  setGender('');
	  setEmail('');
	  setPassword('');
	  setContactInfo('');
	  setCounterID('');
  
	  navigate('/view-staff-list');
	} catch (error) {
	  console.error('Error adding staff:', error.response ? error.response.data : error.message);
	  toast.error(`Failed to add staff. Error: ${error.response ? error.response.data : error.message}`);
	}
  };
	return (
		<div className="bg-dark text-light min-h-screen flex flex-col justify-center py-12 sm:px-6 lg:px-8">
			<ManagerMenu />
			<div className="sm:mx-auto sm:w-full sm:max-w-md">
				<img
					className="mx-auto h-12 w-auto"
					src="https://www.svgrepo.com/show/301692/login.svg"
					alt="Workflow"
				/>
				<h2 className="mt-6 text-center text-3xl leading-9 font-extrabold text-white">
					Create New Staff
				</h2>
			</div>
			<div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
				<div className="bg-gradient-to-r from-indigo-900 via-purple-900 to-pink-900 py-8 px-4 shadow sm:rounded-lg sm:px-10">
					<form onSubmit={handleSubmit} className="space-y-6">
						<div>
							<label className="block text-sm font-medium leading-5 text-white">Full Name</label>
							<div className="mt-1 rounded-md shadow-sm">
								<input
									id="fullName"
									name="fullName"
									type="text"
									required
									value={fullName}
									onChange={(e) => setFullName(e.target.value)}
									className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 bg-gray-800 text-white focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
								/>
							</div>
						</div>
						<div>
							<label className="block text-sm font-medium leading-5 text-white">Gender</label>
							<select
								id="gender"
								value={gender}
								onChange={(e) => setGender(e.target.value)}
								className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-gray-800 text-white rounded-md shadow-sm focus:outline-none focus:shadow-outline-blue focus:border-blue-300 sm:text-sm sm:leading-5"
								required
							>
								<option value="" disabled>Select Gender</option>
								<option value="Male">Male</option>
								<option value="Female">Female</option>
								<option value="Other">Other</option>
							</select>
						</div>
						<div>
							<label className="block text-sm font-medium leading-5 text-white">Email Address</label>
							<div className="mt-1 rounded-md shadow-sm">
								<input
									id="email"
									name="email"
									type="email"
									required
									value={email}
									onChange={(e) => setEmail(e.target.value)}
									className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 bg-gray-800 text-white focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
								/>
							</div>
						</div>
						<div>
							<label className="block text-sm font-medium leading-5 text-white">Password</label>
							<div className="mt-1 rounded-md shadow-sm">
								<input
									id="password"
									name="password"
									type="password"
									required
									value={password}
									onChange={(e) => setPassword(e.target.value)}
									className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 bg-gray-800 text-white focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
								/>
							</div>
						</div>
						<div>
							<label className="block text-sm font-medium leading-5 text-white">Contact Information</label>
							<div className="mt-1 rounded-md shadow-sm">
								<input
									id="contactInfo"
									name="contactInfo"
									type="text"
									required
									value={contactInfo}
									onChange={(e) => setContactInfo(e.target.value)}
									className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 bg-gray-800 text-white focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
								/>
							</div>
						</div>
						<div>
							<label className="block text-sm font-medium leading-5 text-white">Counter ID</label>
							<select
								id="counterID"
								value={counterID}
								onChange={(e) => setCounterID(e.target.value)}
								className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-gray-800 text-white rounded-md shadow-sm focus:outline-none focus:shadow-outline-blue focus:border-blue-300 sm:text-sm sm:leading-5"
								required
							>
								<option value="" disabled>Select Counter</option>
								{counters.map((counter) => (
									<option key={counter.id} value={counter.id}>{counter.id}</option>
								))}
							</select>
						</div>
						<div>
							<button
								type="submit"
								className="block w-full py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-purple-600 hover:bg-purple-500 focus:outline-none focus:shadow-outline-blue focus:border-blue-700"
							>
								Create Staff
							</button>
							<ToastContainer />
						</div>
					</form>
				</div>
			</div>
		</div>
	);
};

export default AddStaff;
