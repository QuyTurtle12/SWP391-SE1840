import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import ManagerMenu from './ManagerMenu';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const AddCategory = () => {
	const [categoryName, setCategoryName] = useState('');
	const navigate = useNavigate();

	const handleSubmit = async (e) => {
		e.preventDefault();

		const data = {
			categoryName
		};

		try {
			console.log('Submitting data:', data);
			const response = await axios.post(
				'http://localhost:8080/api/categories',
				null,
				{
					params: data
				}
			);
			toast.success('Category created successfully!');
			// Optionally reset form fields
			setCategoryName('');
			console.log('Add response', response);

			// Navigate to view-category after successful submission
			navigate('/view-category');
		} catch (error) {
			console.error(
				'Error adding category:',
				error.response ? error.response.data : error.message
			);
			toast.error(
				`Failed to add category. Error: ${
					error.response ? error.response.data.error : error.message
				}`
			);
		}
	};

	return (
		<div>
			<ManagerMenu />
			<ToastContainer />
			<div className="min-h-screen bg-gray-100 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
				<div className="sm:mx-auto sm:w-full sm:max-w-md">
					<img
						className="mx-auto h-10 w-auto"
						src="https://www.svgrepo.com/show/301692/login.svg"
						alt="Workflow"
					/>
					<h2 className="mt-6 text-center text-3xl leading-9 font-extrabold text-gray-900">
						Create new category
					</h2>
				</div>

				<div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
					<div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
						<form onSubmit={handleSubmit}>
							<div className="mt-6">
								<label className="block text-sm font-medium leading-5 text-gray-700">
									Category Name
								</label>
								<div className="mt-1 relative rounded-md shadow-sm">
									<input
										id="categoryName"
										name="categoryName"
										placeholder="Category Name"
										type="text"
										required
										value={categoryName}
										onChange={(e) => setCategoryName(e.target.value)}
										className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition duration-150 ease-in-out sm:text-sm sm:leading-5"
									/>
								</div>
							</div>
							<div className="mt-6">
								<button
									type="submit"
									className="w-full py-2 font-medium text-white bg-green-500 rounded-md hover:bg-green-300 focus:outline-none focus:shadow-outline-blue active:bg-red-600 transition duration-150 ease-in-out"
								>
									Create Category
								</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	);
};

export default AddCategory;
