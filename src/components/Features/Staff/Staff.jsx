import React, { useEffect } from "react";
import StaffMenu from "./StaffMenu";
import { useNavigate } from "react-router-dom";

function Staff() {
  const navigate = useNavigate();

  useEffect(() => {
    navigate("/productlist");
  }, [navigate]);

  return (
    <>
      <StaffMenu />
    </>
  );
}

export default Staff;
