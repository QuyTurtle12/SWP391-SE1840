import React, { useEffect } from "react";
import AdminMenu from "./AdminMenu";
import { useNavigate } from "react-router-dom";

function Admin() {
  const navigate = useNavigate();
  useEffect(() => {
    navigate("/dashboard");
  }, [navigate]);

  return (
    <>
      <AdminMenu></AdminMenu>
    
    </>
  );
}

export default Admin;
