import React, { useEffect, useState } from "react";
import StaffMenu from "./StaffMenu";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function ViewOrderRefund() {
  const [orders, setOrder] = useState([]);
  const navigate = useNavigate();
  const token = localStorage.getItem('token'); // Fetch the token from local storage

  useEffect(() => {
    if (token) {
      axios
        .get("https://jewelrysalesystem-backend.onrender.com/api/refunds", {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })
        .then((response) => {
          console.log(response.data);
          setOrder(response.data);
        })
        .catch((error) => console.error("Error fetching data:", error));
    } else {
      console.error("No token found");
    }
  }, [token]);

  const handleOrder = (id) => {
    navigate(`/refund-detail/${id}`);
  }
  return (
    < >
      {" "}
      <StaffMenu />
      <div className="container min-h-screen h-full">
      <div className=" bg-white view-manager-list  justify-center h-auto ">
        <div className="text-3xl justify-between text-center font-bold pt-10 text-black mb-8">
          <h1>Order Refund List</h1>
        </div>
        <div className="py-20 justify-between items-center px-10">
          <table className="border border-black min-w-full divide-y divide-gray-500 ">
            <thead>
              <tr>
                <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  Order Refund ID{" "}
                </th>
                <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  Customer Name
                </th>
                <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  Date Time
                </th>
                <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  Total Price
                </th>
                <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  StaffID{" "}
                </th>
               
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-500">
              {orders.map((order) => (
                <tr key={order.id}>
                  <td className="px-4 py-4 whitespace-nowrap">{order.ID} </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                    {order.customerName}
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                  {new Date(order.date.seconds * 1000).toLocaleString()} {/* Example formatting */}
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                    ${order.totalPrice.toLocaleString("en-US")} 
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                    {order.staffID}
                  </td>
                  
                  <td className="px-4 py-4 whitespace-nowrap">
                  <button
                      onClick={() => handleOrder(order.ID )}
                      className="bg-white text-gray-900 py-2 px-6 rounded-full font-bold hover:bg-gray-300"
                    >
                      View 
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
      </div>
      
    </>
  );
}

export default ViewOrderRefund;
