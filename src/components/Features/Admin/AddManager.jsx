import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import AdminMenu from './AdminMenu';

const AddManager = () => {
    const [ID, setID] = useState('');
    const [fullName, setFullName] = useState('');
    const [gender, setGender] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [contactInfo, setContactInfo] = useState('');
    const [counterID, setCounterID] = useState('');

    const navigate = useNavigate(); // Initialize useNavigate hook

    const handleSubmit = async (e) => {
        e.preventDefault();

        const data = {
            ID: parseInt(ID),
            fullName,
            gender,
            email,
            password,
            contactInfo,
            counterID: parseInt(counterID)
        };

        try {
            console.log('Submitting data:', data);
            const response = await axios.post('http://localhost:8080/api/account/MANAGER/register', null, {
                params: data
            });
            alert('Manager added successfully!');
            // Optionally reset form fields
            setID('');
            setFullName('');
            setGender('');
            setEmail('');
            setPassword('');
            setContactInfo('');
            setCounterID('');
            console.log("Add response", response);

            // Navigate to view-manager-list after successful submission
            navigate('/view-manager-list');
        } catch (error) {
            console.error('Error adding manager:', error.response ? error.response.data : error.message);
            alert(`Failed to add manager. Error: ${error.response ? error.response.data.error : error.message}`);
        }
    };

    return (
        <div>
            <AdminMenu />
            <div className="min-h-screen bg-tiffany flex flex-col justify-center py-12 sm:px-6 lg:px-8">
                <div className="sm:mx-auto sm:w-full sm:max-w-md">
                    <img
                        className="mx-auto h-10 w-auto"
                        src="https://www.svgrepo.com/show/301692/login.svg"
                        alt="Workflow"
                    />
                    <h2 className="mt-6 text-center text-3xl leading-9 font-extrabold text-gray-900">
                        Create new manager
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
                                    Full Name
                                </label>
                                <div className="mt-1 relative rounded-md shadow-sm">
                                    <input
                                        id="fullName"
                                        name="fullName"
                                        placeholder="Full Name"
                                        type="text"
                                        required
                                        value={fullName}
                                        onChange={(e) => setFullName(e.target.value)}
                                        className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                                    />
                                </div>
                            </div>
                            <div className='mt-6'>
                                <label className="block text-sm font-medium leading-5 text-gray-700">
                                    Gender
                                </label>
                                <select
                                    id="gender"
                                    value={gender}
                                    onChange={(e) => setGender(e.target.value)}
                                    className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-cyan-600 focus:border-cyan-600 block w-full p-2.5"
                                    required
                                >
                                    <option value="" disabled>Select Gender</option>
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                    <option value="Third gender">Third gender</option>
                                </select>
                            </div>
                            <div className="mt-6">
                                <label className="block text-sm font-medium leading-5 text-gray-700">
                                    Email Address
                                </label>
                                <div className="mt-1 relative rounded-md shadow-sm">
                                    <input
                                        id="email"
                                        name="email"
                                        placeholder="Email Address"
                                        required
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                                    />
                                </div>
                            </div>
                            <div className="mt-6">
                                <label className="block text-sm font-medium leading-5 text-gray-700">
                                    Counter ID
                                </label>
                                <div className="mt-1 relative rounded-md shadow-sm">
                                    <input
                                        id="counterID"
                                        name="counterID"
                                        type='number'
                                        placeholder="Counter ID"
                                        required
                                        value={counterID}
                                        onChange={(e) => setCounterID(e.target.value)}
                                        className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                                    />
                                </div>
                            </div>
                            <div className="mt-6">
                                <label className="block text-sm font-medium leading-5 text-gray-700">
                                    Contact Information
                                </label>
                                <div className="mt-1 relative rounded-md shadow-sm">
                                    <input
                                        id="contactInfo"
                                        name="contactInfo"
                                        type='number'
                                        placeholder="Contact Information"
                                        required
                                        value={contactInfo}
                                        onChange={(e) => setContactInfo(e.target.value)}
                                        className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                                    />
                                </div>
                            </div>
                            <div className="mt-6">
                                <label
                                    htmlFor="password"
                                    className="block text-sm font-medium leading-5 text-gray-700"
                                >
                                    Password
                                </label>
                                <div className="mt-1 rounded-md shadow-sm">
                                    <input
                                        id="password"
                                        name="password"
                                        required
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        placeholder="Password"
                                        className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
                                    />
                                </div>
                            </div>
                            <div className="mt-6">
                                <button
                                    type="submit"
                                    className="block w-full py-2 text-center text-white bg-teal-500 border border-teal-500 rounded hover:bg-transparent hover:text-teal-500 transition uppercase font-roboto font-medium"
                                >
                                    Create Manager
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AddManager;
