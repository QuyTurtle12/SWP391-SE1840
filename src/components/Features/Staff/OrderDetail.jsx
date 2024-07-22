import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";

function OrderDetail() {
  const { id } = useParams();
  const [orders, setOrders] = useState([]);
  const [orderDetail, setOrderDetail] = useState({});
  const [staff, setStaff] = useState({});
  const [totalPriceOfProducts, setTotalPriceOfProducts] = useState(0);
  const [voucherValue, setVoucherValue] = useState("0.00");
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchOrderData = async () => {
      try {
        const orderProductsResponse = await axios.get(
          `http://localhost:8080/api/v2/orders/order/products?id=${id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setOrders(orderProductsResponse.data);
        console.log(orderProductsResponse.data);
        const orderDetailResponse = await axios.get(
          `http://localhost:8080/api/v2/orders/order?id=${id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log(orderDetailResponse.data);
        setOrderDetail(orderDetailResponse.data);

        const staffID = orderDetailResponse.data.staffID;
        const staffResponse = await axios.get(
          `https://jewelrysalesystem-backend.onrender.com/api/v2/accounts/staff?id=${staffID}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setStaff(staffResponse.data);

        // Calculate total price of products
        const total = orderProductsResponse.data.reduce(
          (acc, order) => acc + order.productPrice,
          0
        );
        setTotalPriceOfProducts(total);

        // Calculate voucher value if discountApplied is valid
        if (orderDetailResponse.data.discountApplied) {
          const voucher = orderDetailResponse.data.discountApplied * total;
          setVoucherValue(voucher.toFixed(2));
        }
      } catch (error) {
        console.error("Error fetching order data", error);
      }
    };

    fetchOrderData();
  }, [id, token]);

  const formatTotalPrice = (price) => {
    if (typeof price !== "number") return "";
    return price.toLocaleString("en-US", {
      style: "currency",
      currency: "USD",
    });
  };

  const formatDate = (date) => {
    if (!date) return "";
    const d = new Date(date.seconds * 1000 + date.nanos / 1000000);
    return d.toLocaleString();
  };

  return (
    <div className="py-14 px-4 md:px-6 2xl:px-20 2xl:container h-screen 2xl:mx-auto">
      <a
        href="/order-list"
        className="inline-flex border border-black px-3 py-1.5 rounded-md text-black hover:bg-indigo-50"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          className="h-6 w-6"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth="2"
            d="M7 16l-4-4m0 0l4-4m-4 4h18"
          ></path>
        </svg>
        <span className="ml-1 font-bold text-lg">Back</span>
      </a>
      <div className="flex justify-start item-start space-y-2 flex-col">
        <h1 className="text-3xl dark:text-black lg:text-4xl font-semibold leading-7 lg:leading-9 text-gray-800">
          Order #{orderDetail.id}
        </h1>
        <p className="text-base dark:text-black font-medium leading-6 text-gray-600">
          {orderDetail.date ? formatDate(orderDetail.date) : ""}
        </p>
      </div>
      <div className="mt-10 flex flex-col xl:flex-row justify-center items-stretch w-full xl:space-x-8 space-y-4 md:space-y-6 xl:space-y-0">
        <div className="flex flex-col justify-start items-start w-full space-y-4 md:space-y-6 xl:space-y-8">
          <div className="flex flex-col justify-start items-start bg-gray-300 px-4 py-4 md:py-6 md:p-6 xl:p-8 w-full">
            <p className="text-lg md:text-xl text-black font-semibold leading-6 xl:leading-5 ">
              Customer’s Cart
            </p>
            <div className="container mx-auto p-4">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th
                      scope="col"
                      className="px-6 py-3 text-left text-xs font-bold text-black uppercase tracking-wider"
                    >
                      Product Name
                    </th>
                    <th
                      scope="col"
                      className="px-6 py-3 text-left text-xs font-bold text-black uppercase tracking-wider"
                    >
                      Price per product
                    </th>
                    <th
                      scope="col"
                      className="px-6 py-3 text-left text-xs font-bold text-black uppercase tracking-wider"
                    >
                      Quantity
                    </th>
                    <th
                      scope="col"
                      className="px-6 py-3 text-left text-xs font-bold text-black uppercase tracking-wider"
                    >
                      Total Price of product
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {orders.map((order) => {
                    const pricePerProduct = order.productPrice / order.amount;

                    return (
                      <tr key={order.productID}>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          {order.productName}
                        </td>

                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {pricePerProduct !== order.productOriginalPrice ? (
                            <>
                              <span className="text-gray-400 font-bold text-lg line-through px-2">
                                  $ {order.productOriginalPrice}
                              </span>
                              <span className="text-gray-500 font-bold text-lg">
                              $ {pricePerProduct}
                              </span>
                            </>
                          ) : (
                            <span className="text-gray-400 font-bold text-lg">
                              $ {order.productOriginalPrice}
                            </span>
                          )}
                        </td>

                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {order.amount} pcs
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          $ {order.productPrice.toLocaleString("en-US")}
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
          <div className="flex justify-center flex-col md:flex-row items-stretch w-full space-y-4 md:space-y-0 md:space-x-6 xl:space-x-8">
            <div className="flex flex-col px-4 py-6 md:p-6 xl:p-8 w-full bg-gray-300 space-y-6">
              <h3 className="text-xl text-black font-semibold leading-5">
                Summary
              </h3>
              <div className="flex justify-between items-center w-full">
                <p className="text-base text-black font-semibold leading-4">
                  Original price{" "}
                </p>
                <p className="text-base text-black font-semibold leading-4">
                  {formatTotalPrice(totalPriceOfProducts)}
                </p>
              </div>
              <div className="flex justify-between items-center w-full">
                <p className="text-base text-black font-semibold leading-4">
                  Voucher : {orderDetail.discountName}
                </p>
                <p className="text-base text-gray-500 font-semibold line-through leading-4">
                  ${voucherValue}
                </p>
              </div>
              <div className="flex justify-between items-center w-full">
                <p className="text-base text-black font-semibold leading-4">
                  Customer Point:
                </p>
                <p className="text-base text-gray-500 font-semibold line-through leading-4">
                  ${orderDetail.pointApplied}
                </p>
              </div>
              <div className="flex justify-center items-end w-full space-y-4 flex-col border-black border-b pb-4"></div>

              <div className="flex justify-between items-center w-full">
                <p className="text-base text-black font-semibold leading-4">
                  Total
                </p>
                <p className="text-base text-black font-semibold leading-4">
                  {formatTotalPrice(orderDetail.totalPrice)}
                </p>
              </div>
            </div>
          </div>
        </div>
        <div className="bg-gray-300 w-full xl:w-96 flex justify-between items-center md:items-start px-4 py-6 md:p-6 xl:p-8 flex-col">
          <h3 className="text-xl text-black font-semibold leading-5">
            Customer
          </h3>
          <div className="flex flex-col md:flex-row xl:flex-col justify-start items-stretch h-full w-full md:space-x-6 lg:space-x-8 xl:space-x-0">
            <div className="flex flex-col justify-start items-start flex-shrink-0">
              <div className="flex justify-center w-full md:justify-start items-center space-x-4 py-8 border-b border-black">
                <div className="flex justify-start items-start flex-col space-y-2">
                  <p className="text-base text-wrap text-black font-semibold leading-4 text-left">
                    <ul>
                      <li className="pb-2 flex">
                        Name: <p className="pl-4">{orderDetail.customerName}</p>
                      </li>
                      <li className="pb-2 flex">
                        Contact:{" "}
                        <p className="pl-4"> {orderDetail.customerPhone}</p>
                      </li>
                    </ul>
                  </p>
                </div>
              </div>
            </div>
            <div className="flex justify-between xl:h-full items-stretch w-full flex-col mt-6 md:mt-0">
              <div className="flex justify-center md:justify-start xl:flex-col flex-col md:space-x-6 lg:space-x-8 xl:space-x-0 space-y-4 xl:space-y-12 md:space-y-0 md:flex-row items-center md:items-start"></div>
              <div className="flex w-full justify-center items-center md:justify-start md:items-start"></div>
            </div>
          </div>
          <h3 className="text-xl text-black font-semibold leading-5">Staff</h3>
          <div className="flex flex-col md:flex-row xl:flex-col justify-start items-stretch h-full w-full md:space-x-6 lg:space-x-8 xl:space-x-0">
            <div className="flex flex-col justify-start items-start flex-shrink-0">
              <div className="flex justify-center w-full md:justify-start items-center space-x-4 py-8 border-b border-black">
                <div className="flex justify-start items-start flex-col space-y-2">
                  <p className="text-base text-black font-semibold leading-4 text-left">
                    <ul>
                      <li className="pb-2 flex">
                        ID: <p className="pl-4 ">{orderDetail.staffID}</p>
                      </li>
                      <li className="pb-2 flex">
                        Name: <p className="pl-4">{staff.fullName}</p>
                      </li>
                      <li className="pb-2 flex">
                        Counter ID: <p className="pl-4">{staff.counterID}</p>
                      </li>
                      <li className="pb-2 flex">
                        Email: <p className="pl-4">{staff.email}</p>
                      </li>
                      <li className="pb-2 flex">
                        Gender: <p className="pl-4">{staff.gender}</p>
                      </li>
                      <li className="pb-2 flex">
                        Contact: <p className="pl-4">{staff.contactInfo}</p>
                      </li>
                    </ul>
                  </p>
                </div>
              </div>
            </div>
            <div className="flex justify-between xl:h-full items-stretch w-full flex-col mt-6 md:mt-0">
              <div className="flex justify-center md:justify-start xl:flex-col flex-col md:space-x-6 lg:space-x-8 xl:space-x-0 space-y-4 xl:space-y-12 md:space-y-0 md:flex-row items-center md:items-start"></div>
              <div className="flex w-full justify-center items-center md:justify-start md:items-start"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default OrderDetail;
