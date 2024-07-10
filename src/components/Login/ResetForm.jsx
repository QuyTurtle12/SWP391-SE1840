import axios from "axios";
import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";

function ResetForm() {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const token = queryParams.get("token");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        `https://jewelrysalesystem-backend.onrender.com/api/auth/reset-password?token=${token}&newPassword=${password}`
      );
      console.log("Token:", token);
      console.log("Password reset successfully", response.data);
      toast.success("Password reset successfully");
      navigate("/login");
    } catch (error) {
      console.error("Error at submitting new password", error);
      toast.error("Error to reset password");
    }
  };

  return (
    <main
      id="content"
      role="main"
      className="w-full h-screen max-w-md mx-auto p-6"
    >
      <ToastContainer />
      <div className="mt-7 bg-white rounded-xl shadow-lg dark:bg-gray-800 dark:border-gray-700 border-2 border-indigo-300">
        <div className="p-4 sm:p-7">
          <div className="mt-3">
            <form onSubmit={handleSubmit}>
              <div className="grid gap-y-4">
                <div>
                  <label
                    htmlFor="password"
                    className="block text-sm text-center font-bold ml-1 mb-2 text-black"
                  >
                    NEW PASSWORD
                  </label>
                  <div className="relative">
                    <input
                      type="password"
                      id="password"
                      name="password"
                      placeholder="input your new password!"
                      className="py-3 px-4 block w-full border-2 border-gray-200 rounded-md text-sm focus:border-blue-500 focus:ring-blue-500 shadow-sm"
                      required
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      aria-describedby="password-error"
                    />
                  </div>
                </div>
                <button
                  type="submit"
                  className="py-3 px-4 inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold bg-blue-500 text-white hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm dark:focus:ring-offset-gray-800"
                >
                  Reset password
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </main>
  );
}

export default ResetForm;
