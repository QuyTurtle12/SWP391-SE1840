import { Box } from "@mui/material"

export default function Register() {
  return (
    <Box className="h-screen w-screen flex items-center justify-center">
      <section class="flex flex-col items-center pt-6">
  <div
    className="w-full bg-white rounded-lg shadow dark:border md:mt-0 sm:max-w-md xl:p-0 dark:bg-gray-800 dark:border-gray-700">
    <div className="p-6 space-y-4 md:space-y-6 sm:p-8">
      <h1 className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-black">Create an
        account
      </h1>
      <form className="space-y-4 md:space-y-6" method="POST">
        <div>
          <label for="name" className="block mb-2 text-sm font-medium text-gray-900 dark:text-black">Your full name</label>
          <input type="text" name="name" id="name" className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-blue-600 focus:border-blue-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="Emelia Erickson" required=""/>
        </div>
        <div>
          <label for="username" className="block mb-2 text-sm font-medium text-gray-900 dark:text-black">Username</label>
          <input type="text" name="username" id="username" className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-blue-600 focus:border-blue-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="emelia_erickson24" required=""/>
        </div>
        <div>
          <label for="password" className="block mb-2 text-sm font-medium text-gray-900 dark:text-black">Password</label>
          <input type="password" name="password" id="password" placeholder="••••••••" className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-blue-600 focus:border-blue-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" required=""/>
        </div>
        <button type="submit" className="w-full text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Create an account</button>
        <p className="text-sm font-light text-gray-500 dark:text-gray-400">Already have an account? <a
            className="font-medium text-blue-600 hover:underline dark:text-blue-500" href="/login">Sign in here</a>
        </p>
      </form>
    </div>
  </div>
</section>
      </Box>
  )
}