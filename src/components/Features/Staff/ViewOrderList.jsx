import React, { useEffect, useState } from "react";
import StaffMenu from "./StaffMenu";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";

function ViewOrderList() {
  const [orders, setOrder] = useState([]);
  const navigate = useNavigate();
  const { cusphone } = useParams();
  const token = localStorage.getItem("token");

  useEffect(() => {
    if (cusphone) {
      searchOrder(cusphone);
    } else {
      fetchOrders();
    }
  }, [cusphone]);

  const fetchOrders = () => {
    axios
      .get("https://jewelrysalesystem-backend.onrender.com/api/v2/orders", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        console.log(response.data);
        setOrder(response.data);
      })
      .catch((error) => console.error("Error at fetching data", error));
    console.log(token);
  };

  const searchOrder = (customerPhone) => {
    axios
      .get(
        `https://jewelrysalesystem-backend.onrender.com/api/v2/orders/search?input=${customerPhone}&filter=ByPhoneNumber`
      ,{
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        setOrder(response.data);
      })
      .catch((error) => console.error("Error order customer:", error));
      console.log(token);

  };

  const handleOrder = (orderId) => {
    navigate(`/orderdetail/${orderId}`, { state: { orderId } });
  };

  return (
    <>
      <StaffMenu />
      <div className="container h-full">
        <div className="bg-white view-manager-list justify-center h-auto">
          <div className="text-3xl justify-between text-center font-bold pt-10 text-black mb-8">
            <h1>Order List</h1>
          </div>
          <div className="py-20 justify-between items-center px-10">
            <table className="border border-black min-w-full divide-y divide-gray-500">
              <thead>
                <tr>
                  <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                    Order
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                    Customer name
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                    Date Time
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                    Discount Applied
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                    StaffID
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                    Counter
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                    Total Price
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-500">
                {orders.map((order) => (
                  <tr key={order.id}>
                    <td className="px-4 py-4 whitespace-nowrap">{order.id}</td>
                    <td className="px-4 py-4 whitespace-nowrap">
                      {order.customerName}
                    </td>
                    <td className="px-4 py-4 whitespace-nowrap">
                      {new Date(order.date.seconds * 1000).toLocaleString()}
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
