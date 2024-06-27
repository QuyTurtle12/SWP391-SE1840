import React, { useEffect, useState } from "react";
import StaffMenu from "./StaffMenu";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function ViewOrderList() {
  const [orders, setOrder] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/v2/orders",[])
      .then((respone) => {
        console.log(respone.data);
        setOrder(respone.data);
      })
      .catch((error) => console.error("Error at fetching data", error));
  }, []);
  const handleOrder = (id) => {
    navigate(`/orderdetail/${id}`);
  }
  return (
    < >
      {" "}
      <StaffMenu />
      <div className="container h-full">
      <div className=" bg-white view-manager-list  justify-center h-auto ">
        <div className="text-3xl justify-between text-center font-bold pt-10 text-black mb-8">
          <h1>Order List</h1>
        </div>
        <div className="py-20 justify-between items-center px-10">
          <table className="border border-black min-w-full divide-y divide-gray-500 ">
            <thead>
              <tr>
                <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  Order{" "}
                </th>
                <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  Customer Name
                </th>
                <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  Date Time
                </th>
                <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  Discount Applied
                </th>
                <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  StaffID{" "}
                </th>
                <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  Counter
                </th>
                <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  total Price{" "}
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-500">
              {orders.map((order) => (
                <tr key={order.id}>
                  <td className="px-4 py-4 whitespace-nowrap">{order.id} </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                    {order.customerName}
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                  {new Date(order.date.seconds * 1000).toLocaleString()} {/* Example formatting */}
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                    {order.discountApplied}
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                    {order.staffID}
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                    {order.counterID}
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                    ${order.totalPrice.toLocaleString("en-US")}
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                  <button
                      onClick={() => handleOrder(order.id)}
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

export default ViewOrderList;
