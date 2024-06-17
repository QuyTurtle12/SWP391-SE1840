import axios from "axios";
import React, { useEffect, useState } from "react";
import StaffMenu from "./StaffMenu";
import { useNavigate } from "react-router-dom";

function CustomerList() {
  const [customers, setCustomers] = useState([]);
  const navigate = useNavigate();
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/customer/list")
      .then((response) => {
        console.log(response.data);
        setCustomers(response.data);
      })
      .catch((error) => console.error("Error at fetching data", error));
  }, []);

  return (
    <>
      <StaffMenu />
      <div className="bg-tiffany py-36 view-manager-list flex justify-center h-screen ">
        <div className="justify-between items-center px-10">
          <table className="min-w-full divide-y divide-gray-200 ">
            <thead>
              <tr>
              <th className="px-6 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  ID
                </th>
                <th className="px-6 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  Name
                </th>
                <th className="px-6 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  contactInfo{" "}
                </th>
                <th className="px-6 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  gender{" "}
                </th>
                <th className="px-6 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  point{" "}
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {customers.map((customer) => (
                <tr key={customer.id}>
                   <td className="px-6 py-4 whitespace-nowrap">
                    {customer.id}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {customer.name}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {customer.contactInfo}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {customer.gender}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {customer.point}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
}

export default CustomerList;
