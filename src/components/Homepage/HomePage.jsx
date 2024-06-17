import React from "react";
import logobanner from "../asset/banner1.png";

const HomePage = () => {
  return (
    <main className="w-screen  text-black bg-tiffany relative overflow-hidden h-screen">
      <header className="h-24 sm:h-32 flex items-center z-30 w-full bg-slate-200">
        <div className="container mx-auto px-6 flex items-center justify-between">
          <div className="uppercase text-black dark:text-white font-black text-3xl">
            JSS STORE
          </div>
          <div className="flex items-center">
            <nav className="font-sen text-black dark:text-white uppercase text-lg lg:flex items-center hidden">
              <a href="#" className="py-2 px-6 flex">
                Home
              </a>
              <a href="#" className="py-2 px-6 flex">
                Watch
              </a>
              <a href="#" className="py-2 px-6 flex">
                Product
              </a>
              <a href="#" className="py-2 px-6 flex">
                Contact
              </a>
              <a href="#" className="py-2 px-6 flex">
                Career
              </a>
            </nav>
            <button className="lg:hidden flex flex-col ml-4">
              <span className="w-6 h-1 bg-gray-800 dark:bg-white mb-1"></span>
              <span className="w-6 h-1 bg-gray-800 dark:bg-white mb-1"></span>
              <span className="w-6 h-1 bg-gray-800 dark:bg-white mb-1"></span>
            </button>
          </div>
        </div>
      </header>
      <div className="bg-tiffany  flex relative z-20 items-center overflow-hidden">
        <div className="container mx-auto px-6 flex relative py-16">
          <div className="sm:w-1/2 lg:w-1/2 flex flex-col relative z-20">
            <span className="w-20 h-2 bg-gray-800 dark:bg-tiffany mb-12"></span>
            <h1 className="font-bebas-neue uppercase text-7xl sm:text-8xl font-black flex flex-col leading-none dark:text-white text-black">
              Timeless Treasure
              <span className="text-7xl sm:text-9xl">Await</span>
            </h1>
            <p className=" text-base sm:text-lg text-black dark:text-white mt-8">
              <p>Diamonds sparkle in the night, a symphony of light</p>
              <p>Emeralds gleaming, with a secret they're keeping</p>
              <p>Rubies, oh so bright, their crimson sets the night alight </p>
              <p>Each piece of jewelry holds a tale, of love, of joy, of strife, of hail.</p>
            </p>
            <div className="flex mt-12">
              <a
                href="#"
                className="uppercase py-3 px-6 rounded-lg bg-pink-500 border-2 border-transparent text-black text-lg sm:text-xl mr-4 hover:bg-pink-400"
              >
                Get started
              </a>
              <a
                href="#"
                className="uppercase py-3 px-6 rounded-lg bg-transparent border-2 border-pink-500 text-pink-500 dark:text-black hover:bg-pink-500 hover:text-white text-lg sm:text-xl"
              >
                Read more
              </a>
            </div>
          </div>
          <div className="hidden sm:block sm:w-1/2 lg:w-1/2 relative">
            <img
              src={logobanner}
              className="max-w-full md:max-w-full m-auto pt-20"
            />
          </div>
        </div>
      </div>
    </main>
  );
};

export default HomePage
;
