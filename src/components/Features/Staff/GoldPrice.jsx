import axios from "axios";
import React, { useEffect, useState } from "react";
import StaffMenu from "./StaffMenu";

function GoldPrice() {
  const [gold, setGold] = useState(null);

  useEffect(() => {
    fetchGoldPrice();
  }, []);

  const fetchGoldPrice = () => {
    axios
      .get(
        `https://www.goldapi.io/api/XAU/USD`,
        {
          headers: {
            "x-access-token": "goldapi-zpgpue19lyy67c0i-io",
            "Content-Type": "application/json",
          },
        }
      )
      .then((response) => {
        console.log(response.data);
        setGold(response.data);
      })
      .catch((error) => {
        console.error("Error fetching gold data", error);
      });
  };

  return (
    <div className="min-h-screen">
      {gold && (
        <>
          <StaffMenu />
          <div>
            <h1 className="align-middle text-3xl font-bold pt-10 items-center text-center">
              Gold Price Today
            </h1>
            <h2 className="align-middle text-2xl font-bold pt-10 items-center text-center">
              Date: {new Date(gold.timestamp * 1000).toDateString()}
            </h2>
          </div>
          <table className="w-min border-collapse border border-blue-500 max-w-xl mt-16 mx-auto">
            <thead>
              <tr className="bg-blue-500 text-white">
                <th className="py-2 px-4 text-left text-nowrap">Type</th>
                <th className="py-2 px-4 text-left text-nowrap">Price per Gram</th>
              </tr>
            </thead>
            <tbody>
              <tr className="bg-white border-b border-blue-500">
                <td className="py-2 px-4 font-bold text-nowrap">24k Gold</td>

                <td className="py-2 px-4">{gold.price_gram_24k}$</td>


              </tr>
              <tr className="bg-white border-b border-blue-500">
                <td className="py-2 px-4 font-bold text-nowrap">22k Gold</td>

                <td className="py-2 px-4">{gold.price_gram_22k}$</td>


              </tr>
              <tr className="bg-white border-b border-blue-500">
                <td className="py-2 px-4 font-bold text-nowrap">21k Gold</td>

                <td className="py-2 px-4">{gold.price_gram_21k}$</td>


              </tr>
              <tr className="bg-white border-b border-blue-500">
                <td className="py-2 px-4 font-bold text-nowrap">20k Gold</td>

                <td className="py-2 px-4">{gold.price_gram_20k}$</td>


              </tr>
              <tr className="bg-white border-b border-blue-500">
                <td className="py-2 px-4 font-bold text-nowrap">18k Gold</td>

                <td className="py-2 px-4">{gold.price_gram_18k}$</td>


              </tr>
              <tr className="bg-white border-b border-blue-500">
                <td className="py-2 px-4 font-bold text-nowrap">16k Gold</td>

                <td className="py-2 px-4">{gold.price_gram_16k}$</td>


              </tr>
              <tr className="bg-white border-b border-blue-500">
                <td className="py-2 px-4 font-bold text-nowrap">14k Gold</td>

                <td className="py-2 px-4">{gold.price_gram_14k}$</td>


              </tr>
              <tr className="bg-white border-b border-blue-500">
                <td className="py-2 px-4 font-bold text-nowrap">10k Gold</td>

                <td className="py-2 px-4">{gold.price_gram_10k}$</td>


              </tr>
            </tbody>
          </table>
        </>
      )}
    </div>
  );
}

export default GoldPrice;
