import React from "react";
import { Link } from "react-router-dom";
import Button from "@mui/material/Button";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
function ViewDashboard() {
  return (
    <>
      <Button color="inherit" component={Link} to="/admin">
        <ArrowBackIcon fontSize="large"></ArrowBackIcon>
      </Button>
      <div className="view-dashboard">
        <h1>View Dashboard</h1>

        {/* Add your form or other components for adding a manager here */}
      </div>
    </>
  );
}

export default ViewDashboard;
