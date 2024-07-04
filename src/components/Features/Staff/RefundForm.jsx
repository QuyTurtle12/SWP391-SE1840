import React, { useEffect, useState } from "react";
import StaffMenu from "./StaffMenu";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

function RefundForm() {
  const [orders, setOrders] = useState([]);
  const [productPurities, setProductPurities] = useState([]);
  const { id } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/refunds/refund/products?refundID=${id}`
        );
        console.log(response.data);
        setOrders(response.data);
        setProductPurities(
          response.data.flatMap((order) =>
            Array(order.amount)
              .fill()
              .map((_, i) => ({
                productID: order.productID,
                purity: "",
                index: `${order.productID}-${i}`,
              }))
          )
        );
      } catch (error) {
        console.error("Error fetching data", error);
      }
    };
    fetchProduct();
  }, [id]);

  const handlePurityChange = (index, value) => {
    if (value >= 0 && value <= 100) {
      const updatedPurities = [...productPurities];
      updatedPurities[index].purity = value;
      setProductPurities(updatedPurities);
    } else {
      toast.error("Purity must be between 0 and 100.");
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    const consolidatedPurities = productPurities.reduce((acc, current) => {
      const foundIndex = acc.findIndex(
        (item) =>
          item.productID === current.productID && item.purity === parseInt(current.purity)
      );
      if (foundIndex >= 0) {
        acc[foundIndex].amount += 1;
      } else {
        acc.push({
          productID: current.productID,
          purity: parseInt(current.purity),
          amount: 1,
        });
      }
      return acc;
    }, []);

    try {
      const response = await axios.post(
        `http://localhost:8080/api/refunds/itemPurity?refundID=${id}`,
        consolidatedPurities
      );
      console.log(response.data);
      toast.success("Created refund order successfully!!");
      setTimeout(() => navigate(`/productlist`), 2000); // Navigate after 2 seconds
    } catch (error) {
      console.error("Error submitting data", error);
      toast.error("Error creating refund order.");
    }
  };

  return (
    <>
      <StaffMenu />
      <ToastContainer />
      <div className="h-full">
        <div className="bg-white border-4 rounded-lg shadow relative m-10">
          <div className="flex items-start justify-between p-5 border-b rounded-t">
            <h3 className="text-xl font-semibold">Refund product purity</h3>
          </div>

          <div className="p-6 space-y-6">
            <form onSubmit={handleSubmit}>
              <div className="divide-y divide-black pb-4">
                {orders.map((order) => (
                  <div key={order.id} className="pt-8 grid grid-cols-6 gap-6">
                    <div className="col-span-6 sm:col-span-3">
                      <label
                        htmlFor={`product-name-${order.id}`}
                        className="text-sm font-bold text-gray-900 block mb-2"
                      >
                        Product name:
                        <div className="font-semibold">{order.productName}</div>
                      </label>
                    </div>
                    <div className="col-span-6 sm:col-span-3">
                      <label
                        htmlFor={`productID-${order.id}`}
                        className="text-sm font-bold text-gray-900 block mb-2"
                      >
                        ProductID
                        <div className="font-semibold">{order.productID}</div>
                      </label>
                    </div>
                    <div className="col-span-6 sm:col-span-3">
                      <label
                        htmlFor={`amount-${order.id}`}
                        className="text-sm font-bold text-gray-900 block mb-2"
                      >
                        Amount
                        <div className="font-semibold">{order.amount}</div>
                      </label>
                    </div>
                    {Array(order.amount)
                      .fill()
                      .map((_, i) => (
                        <React.Fragment key={`${order.id}-${i}`}>
                          <div className="col-span-6 pb-4 sm:col-span-1">
                            <label
                              htmlFor={`purity-${order.id}-${i}`}
                              className="text-sm font-bold text-gray-900 block mb-2"
                            >
                              Purity {i + 1}
                            </label>
                            <input
                              type="number"
                              id={`purity-${order.id}-${i}`}
                              className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-cyan-600 focus:border-cyan-600 block w-full p-2.5"
                              placeholder=""
                              required
                              value={
                                productPurities.find(
                                  (purity) =>
                                    purity.index === `${order.productID}-${i}`
                                )?.purity || ""
                              }
                              onChange={(e) =>
                                handlePurityChange(
                                  productPurities.findIndex(
                                    (purity) =>
                                      purity.index === `${order.productID}-${i}`
                                  ),
                                  e.target.value
                                )
                              }
                              min="0"
                              max="100"
                            />
                          </div>
                        </React.Fragment>
                      ))}
                  </div>
                ))}
              </div>
              <div className="p-6 border-t border-gray-200 rounded-b">
                <button
                  className="text-white bg-cyan-600 hover:bg-cyan-700 focus:ring-4 focus:ring-cyan-200 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
                  type="submit"
                >
                  Save all
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </>
  );
}

export default RefundForm;
