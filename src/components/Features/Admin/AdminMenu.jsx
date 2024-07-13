import React from "react";
import { Link } from "react-router-dom";
import Button from "@mui/material/Button";
function AdminMenu() {
  return (
    <>
      <div className="flex border-b-2 py-6 rounded-lg bg-white justify-between h-24 w-full ">
        <Button title="back to admin menu" component={Link} to="/admin">
          <h1 className="w-full px-6 text-black text-3xl font-bold"> ADMIN </h1>
        </Button>
        <ul className="flex my-auto">
          <li className=" px-8 text-nowrap ">
            <Button
              component={Link}
              to="/view-manager-list"
              className=" px-8 rounded-sm text-black font-medium"
            >
              {" "}
              <h2 className="font-bold">View Manager List</h2>
            </Button>
          </li>
          <li className=" px-8 text-nowrap ">
            <Button
              component={Link}
              to="/add-manager"
              className=" px-8 rounded-sm text-black font-medium"
            >
              {" "}
              <h2 className="font-bold">Add Manager</h2>{" "}
            </Button>
          </li>
          <li className=" px-8 text-nowrap ">
            <Button
              component={Link}
              to="/dashboard"
            className=" px-8 rounded-sm text-black font-medium">
              {" "}
              <h2 className="font-bold">View Dashboard</h2>{" "}
            </Button>
          </li>
        </ul>
      </div>
    </>
  );
}

export default AdminMenu;
