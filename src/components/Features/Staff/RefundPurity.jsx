import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams, Link } from "react-router-dom";

function RefundPurity() {
  const { id, productID } = useParams();
  const [orders, setOrder] = useState([]);
  const token = localStorage.getItem('token'); // Fetch the token from local storage

  useEffect(() => {
    const fetchOrder = async () => {
      if (token) {
        axios
          .get(`https://jewelrysalesystem-backend.onrender.com/api/refunds/refund/products/product?refundID=${id}&productID=${productID}`, {
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
  }, [id, productID, token]);
  return (
    <>
  <div className="text-3xl justify-between text-center font-bold pt-10 text-black mb-8">
        <h1>Product Purity Detail Of Product ID : {productID}</h1>
      
      </div>
      <Link
        to={`/refund-detail/${id}`}
        className="inline-flex border border-indigo-300 px-3 py-1.5 rounded-md text-black hover:bg-indigo-50"
      >
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" class="h-6 w-6">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16l-4-4m0 0l4-4m-4 4h18">
            </path>
        </svg>
        <span class="ml-1 font-bold text-lg">Back</span>
      </Link>
     
    
 
    <div className="container min-h-screen h-full">
    <table className="border text-center border-black min-w-full divide-y divide-gray-500 ">
            <thead>
              <tr>
             
                <th className="px-4 py-3  text-xs font-bold text-black uppercase tracking-wider">
                    Purity
                </th>
                <th className="px-4 py-3  text-xs font-bold text-black uppercase tracking-wider">
                  Amount
                </th>
                
              
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-500">
              {orders.map((order) => (
                <tr key={order.productID}>
              
                
                  <td className="px-4 py-4 whitespace-nowrap">
                    {order.purity} %
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                    {order.amount}
                  </td>
                 
                </tr>
              ))}
            </tbody>
          </table>
    </div>
     
    </>
  );
}

export default RefundPurity;
