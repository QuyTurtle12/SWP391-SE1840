import React from "react";
import { Link } from "react-router-dom";
import Button from "@mui/material/Button";

function StaffMenu() {
  return (
    <>
      <div className="flex py-6 rounded-lg bg-white justify-between h-24 w-full ">
        <Button title="back to admin menu" component={Link} to="/staff">
          <h1 className="w-full px-6 text-3xl text-black font-bold"> staff </h1>
        </Button>
        <ul className="flex my-auto">
         

          <li className="font-bold px-8 text-nowrap ">
            <Button component={Link} to="/productlist">
              <h3 className=" px-4 rounded-sm text-black font-bold">
                View Product Lists
              </h3>
            </Button>
          </li>
          <li className=" px-8  text-nowrap ">
            <Button component={Link} to="/customer-list">
              <h3 className=" px-4 rounded-sm text-black font-bold">
                Customer List
              </h3>
            </Button>
          </li>
        </ul>
      </div>
    </>
  );
}

export default StaffMenu;
