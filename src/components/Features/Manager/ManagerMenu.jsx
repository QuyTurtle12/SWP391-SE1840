import React from "react";
import { Link } from "react-router-dom";
import Button from "@mui/material/Button";
import Policy from "./Policy";
export default function ManagerMenu() {
  return (
    <div className="bg-white shadow-md py-6 px-4 md:px-8 rounded-lg">
      <div className="flex flex-col md:flex-row justify-between items-center">
        <Button
          title="Back to Admin Menu"
          component={Link}
          to="/manager"
          className="w-full md:w-auto"
        >
          <h1 className="text-black text-3xl font-bold">Manager</h1>
        </Button>
        <ul className="flex flex-wrap justify-center md:justify-end mt-4 md:mt-0">
          <li className="px-4 py-2">
            <Button
              component={Link}
              to="/view-staff-list"
              className="text-black font-medium"
            >
              <h2 className="font-bold">View Staff List</h2>
            </Button>
          </li>
          <li className="px-4 py-2">
            <Button
              component={Link}
              to="/add-staff"
              className="text-black font-medium"
            >
              <h2 className="font-bold">Add Staff</h2>
            </Button>
          </li>
          <li className="px-4 py-2">
            <Button
              component={Link}
              to="/add-product"
              className="text-black font-medium"
            >
              <h2 className="font-bold">Add Product</h2>
            </Button>
          </li>
          <li className="px-4 py-2">
            <Button
              component={Link}
              to="/add-category"
              className="text-black font-medium"
            >
              <h2 className="font-bold">Add Category</h2>
            </Button>
          </li>
          <li className="px-4 py-2">
            <Button
              component={Link}
              to="/view-category"
              className="text-black font-medium"
            >
              <h2 className="font-bold">View Category</h2>
            </Button>
          </li>
          <li className="px-4 py-2">
            <Button
              component={Link}
              to="/productlist2"
              className="text-black font-medium"
            >
              <h2 className="font-bold">View Product Lists</h2>
            </Button>
          </li>
          <li className="px-4 py-2">
            <Button
              component={Link}
              to="/view-counter"
              className="text-black font-medium"
            >
              <h2 className="font-bold">View Counters</h2>
            </Button>
          </li>
          <li className="px-4 py-2">
            <Button
              component={Link}
              to="/view-promotion"
              className="text-black font-medium"
            >
              <h2 className="font-bold">View Promotion</h2>
            </Button>
          </li>
          <li className="px-4 py-2">
            <Button
              component={Link}
              to="/view-voucher"
              className="text-black font-medium"
            >
              <h2 className="font-bold">View Category</h2>
            </Button>
          </li>
          <li className="px-4 py-2">
            <Button
              component={Link}
              to="/view-category"
              className="text-black font-medium"
            >
              <h2 className="font-bold">View Voucher</h2>
            </Button>
          </li>
 
        </ul>
        
      </div>
      
    </div>
    
  );
}
