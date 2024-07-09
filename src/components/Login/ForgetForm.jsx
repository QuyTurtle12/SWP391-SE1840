import axios from "axios";
import React, { useState } from "react";

function ForgetForm() {
  const [email, setEmail] = useState("");
  const handleSubmit = async (e) => {
    const data = {
      email,
    };
    const respone = await axios.post(
      "https://jewelrysalesystem-backend.onrender.com/api/auth/forgot-password",
      data
    );
    try {
      console.log("submitting data", data);
    } catch (error) {
      console.error("error at submitting email");
    }
  };

  return (
    <main
      id="content"
      role="main"
      class="w-full h-screen  max-w-md mx-auto p-6"
    >
      <div class="mt-7 bg-white  rounded-xl shadow-lg dark:bg-gray-800 dark:border-gray-700 border-2 border-indigo-300">
        <div class="p-4 sm:p-7">
          <div class="text-center">
            <h1 class="block text-2xl font-bold text-black">
              Forgot password?
            </h1>
            <p class="mt-2 text-sm text-gray-600 dark:text-gray-400">
              Remember your password?
              <a
                class="text-blue-600 decoration-2 hover:underline font-medium"
                href="/login"
              >
                Login here
              </a>
            </p>
          </div>

          <div class="mt-3">
            <form onSubmit={handleSubmit}>
              <div class="grid gap-y-4">
                <div>
                  <label
                    for="email"
                    class="block text-sm  font-bold ml-1 mb-2 text-black"
                  >
                    Email address
                  </label>
                  <div class="relative">
                    <input
                      type="email"
                      id="email"
                      name="email"
                      class="py-3 px-4 block w-full border-2 border-gray-200 rounded-md text-sm focus:border-blue-500 focus:ring-blue-500 shadow-sm"
                      required
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      aria-describedby="email-error"
                    />
                  </div>
                  <p class="hidden text-xs text-red-600 mt-2" id="email-error">
                    Please include a valid email address so we can get back to
                    you
                  </p>
                </div>
                <button
                  type="submit"
                  class="py-3 px-4 inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold bg-blue-500 text-white hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm dark:focus:ring-offset-gray-800"
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

export default ForgetForm;
