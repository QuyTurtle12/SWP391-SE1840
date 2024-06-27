import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";

function OrderDetail() {
  const { id } = useParams();
  const [orders, setOrder] = useState([]);
  const [orderDetail, setOrderDetail] = useState({});

  useEffect(() => {
    const fetchOrder = async () => {
      axios
        .get(`http://localhost:8080/api/v2/orders/order/products?id=${id}`)
        .then((respone) => {
          console.log(respone.data);
          setOrder(respone.data);
        })
        .catch((error) => console.error("Error at fetching data", error));
    };
    fetchOrder();
    order();
  }, [id]);
  const order = async () => {
    axios
      .get(`http://localhost:8080/api/v2/orders/order?id=${id}`)
      .then((respone) => {
        console.log(respone.data);
        setOrderDetail(respone.data);
      })
      .catch((error) => console.error("Error at fetching data", error));
  };
  const formatTotalPrice = (price) => {
    if (typeof price !== "number") return "";
    return price.toLocaleString("en-US");
  };

  const formatDate = (date) => {
    if (!date) return "";
    const d = new Date(date.seconds * 1000 + date.nanos / 1000000);
    return d.toLocaleString();
  };
  return (
    <>
      <div>
        <div class="py-14 px-4 md:px-6 2xl:px-20 2xl:container h-screen 2xl:mx-auto">
          <a
            href="/order-list"
            className=" inline-flex border border-black px-3 py-1.5 rounded-md  text-black hover:bg-indigo-50"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              class="h-6 w-6"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M7 16l-4-4m0 0l4-4m-4 4h18"
              ></path>
            </svg>
            <span class="ml-1 font-bold text-lg">Back</span>
          </a>
          <div class="flex justify-start item-start space-y-2 flex-col">
            <h1 class="text-3xl dark:text-black lg:text-4xl font-semibold leading-7 lg:leading-9 text-gray-800">
              Order #{orderDetail.id}
            </h1>
            <p class="text-base dark:text-black font-medium leading-6 text-gray-600">
              {orderDetail.date ? formatDate(orderDetail.date) : ""}
            </p>
          </div>
          <div class="mt-10 flex flex-col xl:flex-row jusitfy-center items-stretch w-full xl:space-x-8 space-y-4 md:space-y-6 xl:space-y-0">
            <div class="flex flex-col justify-start items-start w-full space-y-4 md:space-y-6 xl:space-y-8">
              <div class="flex flex-col justify-start items-start bg-gray-300  px-4 py-4 md:py-6 md:p-6 xl:p-8 w-full">
                <p class="text-lg md:text-xl text-black font-semibold leading-6 xl:leading-5 ">
                  Customer’s Cart
                </p>

                {orders.map((order) => (
                  <div
                    key={order.orderID}
                    class="mt-4 md:mt-6 flex flex-col md:flex-row justify-start items-start md:items-center md:space-x-6 xl:space-x-8 w-full"
                  >
                    <div class="pb-4 md:pb-8 w-full md:w-40"></div>
                    <div class="border-b border-black md:flex-row flex-col flex justify-between items-start w-full pb-8 space-y-4 md:space-y-0">
                      <div class="w-full flex flex-col justify-start items-start space-y-8">
                        <h3 class="text-xl text-black xl:text-2xl font-semibold leading-6">
                          {order.productName}
                        </h3>
                      </div>
                      <div class="flex justify-between space-x-8 items-start w-full">
                        <p class="text-base text-black xl:text-lg leading-6"></p>
                        <p class="text-base text-black xl:text-lg leading-6 ">
                          {order.amount}
                        </p>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
              <div class="flex justify-center flex-col md:flex-row  items-stretch w-full space-y-4 md:space-y-0 md:space-x-6 xl:space-x-8">
                <div class="flex flex-col px-4 py-6 md:p-6 xl:p-8 w-full  bg-gray-300 space-y-6">
                  <h3 class="text-xl text-black font-semibold leading-5 ">
                    Summary
                  </h3>
                  <div class="flex justify-center items-center w-full space-y-4 flex-col border-black border-b pb-4"></div>
                  <div class="flex justify-between items-center w-full">
                    <p class="text-base text-black font-semibold leading-4 ">
                      Total
                    </p>
                    <p class="text-base text-black font-semibold leading-4 ">
                    ${formatTotalPrice(orderDetail.totalPrice)}
                    </p>
                  </div>
                </div>
              </div>
            </div>
            <div class="bg-gray-300 w-full xl:w-96 flex justify-between items-center md:items-start px-4 py-6 md:p-6 xl:p-8 flex-col">
              <h3 class="text-xl text-black font-semibold leading-5 ">
                Customer
              </h3>
              <div class="flex flex-col md:flex-row xl:flex-col justify-start items-stretch h-full w-full md:space-x-6 lg:space-x-8 xl:space-x-0">
                <div class="flex flex-col justify-start items-start flex-shrink-0">
                  <div class="flex justify-center w-full md:justify-start items-center space-x-4 py-8 border-b border-black">
                    <div class="flex justify-start items-start flex-col space-y-2">
                      <p class="text-base text-black font-semibold leading-4 text-left ">
                        ID: {orderDetail.customerID}
                      </p>
                    </div>
                  </div>
                </div>
                <div class="flex justify-between xl:h-full items-stretch w-full flex-col mt-6 md:mt-0">
                  <div class="flex justify-center md:justify-start xl:flex-col flex-col md:space-x-6 lg:space-x-8 xl:space-x-0 space-y-4 xl:space-y-12 md:space-y-0 md:flex-row items-center md:items-start"></div>
                  <div class="flex w-full justify-center items-center md:justify-start md:items-start"></div>
                </div>
              </div>
              <h3 class="text-xl text-black font-semibold leading-5 ">Staff</h3>
              <div class="flex flex-col md:flex-row xl:flex-col justify-start items-stretch h-full w-full md:space-x-6 lg:space-x-8 xl:space-x-0">
                <div class="flex flex-col justify-start items-start flex-shrink-0">
                  <div class="flex justify-center w-full md:justify-start items-center space-x-4 py-8 border-b border-black">
                    <div class="flex justify-start items-start flex-col space-y-2">
                      <p class="text-base text-black font-semibold leading-4 text-left ">
                        ID: {orderDetail.staffID}
                      </p>
                    </div>
                  </div>
                </div>
                <div class="flex justify-between xl:h-full items-stretch w-full flex-col mt-6 md:mt-0">
                  <div class="flex justify-center md:justify-start xl:flex-col flex-col md:space-x-6 lg:space-x-8 xl:space-x-0 space-y-4 xl:space-y-12 md:space-y-0 md:flex-row items-center md:items-start"></div>
                  <div class="flex w-full justify-center items-center md:justify-start md:items-start"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

     
    </>
  );
}

export default OrderDetail;
