import React from "react";
import { Link } from "react-router-dom";
import Button from "@mui/material/Button";
export default function ManagerMenu() {
  return (
    <>
      <div className="flex py-6 rounded-lg bg-white justify-between h-24 w-full ">
        <Button title="back to admin menu" component={Link} to="/admin">
          <h1 className="w-full px-6 text-black text-3xl font-bold"> Manager </h1>
        </Button>
        <ul className="flex my-auto">
          <li className=" px-8 text-nowrap ">
            <Button
              component={Link}
              to="/view-staff-list"
              className=" px-8 rounded-sm text-black font-medium"
            >
              {" "}
              <h2 className="font-bold">View Staff List</h2>
            </Button>
          </li>
          <li className=" px-8 text-nowrap ">
            <Button
              component={Link}
              to="/add-staff"
              className=" px-8 rounded-sm text-black font-medium"
            >
              {" "}
              <h2 className="font-bold">Add Staff</h2>{" "}
            </Button>
          </li>
          <li className=" px-8 text-nowrap ">
            <Button
              component={Link}
              to="/add-product"
              className=" px-8 rounded-sm text-black font-medium"
            >
              {" "}
              <h2 className="font-bold">Add Product</h2>{" "}
            </Button>
          </li>
          
          <li className=" px-8 text-nowrap ">
            <Button className=" px-8 rounded-sm text-black font-medium">
              {" "}
              <h2 className="font-bold">View Promotion</h2>{" "}
            </Button>
          </li>
          <li><Button component={Link} to="/productlist2">
              <h3 className=" px-4 rounded-sm text-black font-bold">
                View Product Lists
              </h3>
            </Button>
            </li>
        </ul>
      </div>
    </>
  );
}

