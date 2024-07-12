import axios from "axios";
import React, { useEffect, useState } from "react";
import StaffMenu from "./StaffMenu";
import { useNavigate } from "react-router-dom";

function CustomerList() {
  const [customers, setCustomers] = useState([]);
  const navigate = useNavigate();
  const token = localStorage.getItem('token');
  const [searchInput, setSearchInput]= useState();
  useEffect(() => {
    axios
      .get("https://jewelrysalesystem-backend.onrender.com/api/v2/customers", {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      .then((response) => {
        console.log(response.data);
        setCustomers(response.data);
      })
      .catch((error) => console.error("Error at fetching data", error));
  },[]);

  const handleViewOrder = (cusphone) => {
    navigate(`/order-list/${cusphone}`);
  }
  const searchCustomer = () => {
    if (token) {
      axios
        .get(
          `http://localhost:8080/api/v2/customers/search?input=${searchInput}&filter=ByPhoneNumber`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then((response) => {
          setCustomers(response.data);
        })
        .catch((error) => console.error("Error searching c:", error));
    } else {
      console.error("No token found");
    }
  };
  const handleSearchInputChange = (event) => {
    setSearchInput(event.target.value);
  };
  return (
    <>
      <StaffMenu />
      <div className="text-3xl justify-between text-center font-bold pt-10 text-black mb-8">
        <h1>Customer List</h1>
      </div>
      <div className=" font-bold">
            <input
              type="text"
              value={searchInput}
              onChange={handleSearchInputChange}
              placeholder="Search Customer's number..."
              className="border p-2 mr-2"
            />
            <button
              onClick={searchCustomer}
              className="bg-gray-900 text-white py-2 px-4 rounded-full font-bold hover:bg-gray-500"
            >
              Search
            </button>
          </div>
      <div className="bg-white py-16 view-manager-list flex justify-center h-screen">
        <div className="justify-between items-center px-10">
          <table className="border border-black min-w-full divide-y divide-gray-500">
            <thead>
              <tr>
                <th className="px-6 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  ID
                </th>
                <th className="px-6 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  Name
                </th>
                <th className="px-6 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  contactInfo
                </th>
                <th className="px-6 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  gender
                </th>
                <th className="px-6 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  point
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-500">
              {customers.map((customer) => (
                <tr key={customer.id}>
                  <td className="px-6 py-4 whitespace-nowrap">{customer.id}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{customer.name}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{customer.contactInfo}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{customer.gender}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{customer.point}</td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <button onClick={() => handleViewOrder(customer.contactInfo)} className="font-semibold hover:bg-gray-100">
                      view orders
                    </button>
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
