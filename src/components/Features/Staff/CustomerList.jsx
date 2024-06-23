import axios from "axios";
import React, { useEffect, useState } from "react";
import StaffMenu from "./StaffMenu";

function CustomerList() {
  const [customers, setCustomers] = useState([]);

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/v2/customers")
      .then((response) => {
        console.log(response.data);
        setCustomers(response.data);
      })
      .catch((error) => console.error("Error at fetching data", error));
  }, []);

  return (
    <>
      <StaffMenu />
      <div className="text-3xl justify-between text-center font-bold pt-10 text-black mb-8">
        <h1>Customer List</h1>
      </div>
      <div className="bg-white py-16 view-manager-list flex justify-center h-screen ">
        <div className="justify-between items-center px-10">
          <table className="border border-black min-w-full divide-y divide-gray-500 ">
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
            <tbody className="bg-white divide-y divide-gray-500">
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
