import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";

function RefundDetail() {
  const { id } = useParams();
  const [orders, setOrder] = useState([]);
  const navigate = useNavigate();
  const token = localStorage.getItem('token'); // Fetch the token from local storage

  useEffect(() => {
    const fetchOrder = async () => {
      if (token) {
        axios
          .get(`https://jewelrysalesystem-backend.onrender.com/api/refunds/refund/products?refundID=${id}`, {
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
    }
    fetchOrder();
  }, [id, token]);

  const handleOrder = (productID) => {
    navigate(`/refund-purity/${id}/${productID}`);
  }
  return (
    <>
  <div className="text-3xl justify-between text-center font-bold pt-10 text-black mb-8">
        <h1>Order Detail</h1>
      
      </div>
      <a  href="/order-refund"
        className=" inline-flex border border-indigo-300 px-3 py-1.5 rounded-md  text-black hover:bg-indigo-50">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" class="h-6 w-6">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16l-4-4m0 0l4-4m-4 4h18">
            </path>
        </svg>
        <span class="ml-1 font-bold text-lg">Back</span>
    </a>
    
 
    <div className="container h-screen">
    <table className="border border-black min-w-full divide-y divide-gray-500 ">
            <thead>
              <tr>
                <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  Product Name
                </th>
                <th className="px-4 py-3 text-left text-xs font-bold text-black uppercase tracking-wider">
                  Amount
                </th>
                
              
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-500">
              {orders.map((order) => (
                <tr key={order.orderID}>
              
                  <td className="px-4 py-4 whitespace-nowrap">
                    {order.productName}
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                    {order.amount}
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                  <button
                      onClick={() => handleOrder(order.productID)}
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
     
    </>
  );
}

export default RefundDetail;
