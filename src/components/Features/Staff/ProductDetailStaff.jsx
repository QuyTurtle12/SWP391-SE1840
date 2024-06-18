import axios from "axios";
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

function ProductDetailStaff() {
  const { id } = useParams();
  const [product, setProduct] = useState({
    img: "",
    name: "",
    price: "",
    refundPrice: "",
    description: "",
    goldWeight: "",
    laborCost: "",
    stoneCost: "",
    stock: "",
    promotionID: "",
    category: "",
    status: "",
  });
  const [cart, setCart] = useState([]);
  const addToCart = (product) => {
    // Gửi yêu cầu POST đến backend để thêm sản phẩm vào giỏ hàng
    axios
      .post("http://localhost:8080/cart", product)
      .then((response) => {
        console.log("Item added to cart:", response.data);
        // Cập nhật giỏ hàng sau khi thêm sản phẩm
        setCart([...cart, product]);
      })
      .catch((error) => {
        console.error("Error adding item to cart:", error);
      });
  };

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const respone = await axios.get(
          `http://localhost:8080/api/v2/products/${id}`
        );
        console.log("Fetched data success:", respone.data);
        setProduct(respone.data);
      } catch (error) {
        console.error("Error at fetching product data", error);
      }
    };
    fetchProduct();
  }, [id]);
  return (
    <div className="h-screen">
      <div class="bg-gray-100 py-8 h-full">
        <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex flex-col md:flex-row -mx-4">
           
           
           
            <div class="md:flex-1 px-4">
              <div class="h-[520px] border-4 border-black rounded-lg  mb-4">
                <img
                  class="w-full h-full object-fit"
                  src={product.img}
                  alt={product.name}
                />
              </div>
            </div>
            <div class="md:flex-1 px-4">
              <h2 class="text-4xl font-bold text-gray-800 mb-2">
                {product.name}
              </h2>

              <div class="mt-20 mb-4">
              <div class="mr-4">
                  <span class="text-2xl font-bold text-gray-700 ">ID Product:</span>
                  <span class="text-gray-800 text-xl ml-6 ">{product.id}</span>
                </div>
                <div class="mr-4">
                  <span class="text-2xl font-bold text-gray-700 ">Price:</span>
                  <span class="text-gray-800 text-xl ml-6 ">{product.price}$</span>
                </div>
                <div class="mr-4">
                  <span class="text-2xl font-bold text-gray-700 ">Refund Price:</span>
                  <span class="text-gray-800 text-xl ml-6 ">{product.refundPrice}$</span>
                </div>
                <div class="mr-4">
                  <span class="text-2xl font-bold text-gray-700 ">GoldWeight:</span>
                  <span class="text-gray-800 text-xl ml-6 ">{product.goldWeight}</span>
                </div>
                <div class="mr-4">
                  <span class="text-2xl font-bold text-gray-700 ">Labor Cost:</span>
                  <span class="text-gray-800 text-xl ml-6 ">{product.laborCost}$</span>
                </div>
                <div class="mr-4">
                  <span class="text-2xl font-bold text-gray-700 ">Stone Cost:</span>
                  <span class="text-gray-800 text-xl ml-6 ">{product.stoneCost}$</span>
                </div>
                <div class="mr-4">
                  <span class="text-2xl font-bold text-gray-700 ">Promotion ID:</span>
                  <span class="text-gray-800 text-xl ml-6 ">{product.promotionID}</span>
                </div>
                <div class="mr-4">
                  <span class="text-2xl font-bold text-gray-700 ">Category:</span>
                  <span class="text-gray-800 text-xl ml-6 ">{product.category}</span>
                </div>
              
                <div>
                  <span class="font-bold text-2xl text-gray-700 ">Stock:</span>
                  <span class="text-gray-800 text-xl ml-6 ">{product.stock} pcs</span>
                </div>
              </div>

              
              <div class="w-1/2 px-2 ">
                <button
                  onClick={() => addToCart(product)}
                  class="w-full mt-8 bg-gray-900 dark:bg-gray-600 text-white py-2 px-4 rounded-full font-bold hover:bg-gray-800 dark:hover:bg-gray-700"
                >
                  Add to Cart
                </button>
                
              </div>
              
            </div>
          </div>
          <div className="mt-20">
                <span class="font-bold text-2xl text-gray-700 ">
                  Product Description:
                </span>
                <p class="text-gray-800 text-xl ml-6">{product.description}</p>
              </div>
        </div>
      </div>
    </div>
  );
}

export default ProductDetailStaff;
