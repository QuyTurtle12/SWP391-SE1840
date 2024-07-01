import axios from "axios";
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";

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

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/v2/products/${id}`
        );
        console.log("Fetched data success:", response.data);
        setProduct(response.data);
      } catch (error) {
        console.error("Error fetching product data", error);
      }
    };

    const fetchCart = async () => {
      try {
        const response = await axios.get("http://localhost:8080/cart");
        setCart(response.data);
      } catch (error) {
        console.error("Error fetching cart data", error);
      }
    };

    fetchProduct();
    fetchCart();
  }, [id]);
  const addToCart = (product) => {
    const isAlreadyInCart = cart.some((item) => item.id === product.id);
    if (isAlreadyInCart) {
      toast.error("Product is already in the cart.");
      return;
    }

    axios
      .post("http://localhost:8080/cart", product)
      .then((response) => {
        console.log("Item added to cart:", response.data);
        setCart([...cart, product]);
        toast.success("Item added to cart successfully!");
      })
      .catch((error) => {
        console.error("Error adding item to cart:", error);
        toast.error("Error adding item to cart!");
      });
  };
  
  return (
    <div className="h-screen">
      <ToastContainer/>
      <div className="bg-gray-100 py-8 h-full">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col md:flex-row -mx-4">
            <div className="md:flex-1 px-4">
              <div className="h-[520px] border-4 border-black rounded-lg mb-4">
                <img
                  className="w-full h-full object-fit"
                  src={product.img}
                  alt={product.name}
                />
              </div>
            </div>
            <div className="md:flex-1 px-4">
              <h2 className="text-4xl font-bold text-gray-800 mb-2">
                {product.name}
              </h2>
              <div className="mt-20 mb-4">
                <div className="mr-4">
                  <span className="text-2xl font-bold text-gray-700">
                    ID Product:
                  </span>
                  <span className="text-gray-800 text-xl ml-6">
                    {product.id}
                  </span>
                </div>
                <div className="mr-4">
                  <span className="text-2xl font-bold text-gray-700">
                    Price:
                  </span>
                  <span className="text-gray-800 text-xl ml-6">
                    {product.price.toLocaleString("en-US")}$
                  </span>
                </div>
                <div className="mr-4">
                  <span className="text-2xl font-bold text-gray-700">
                    Refund Price:
                  </span>
                  <span className="text-gray-800 text-xl ml-6">
                    {product.refundPrice.toLocaleString("en-US")}$
                  </span>
                </div>
                <div className="mr-4">
                  <span className="text-2xl font-bold text-gray-700">
                    Gold Weight:
                  </span>
                  <span className="text-gray-800 text-xl ml-6">
                    {product.goldWeight} gram 
                  </span>
                </div>
                <div className="mr-4">
                  <span className="text-2xl font-bold text-gray-700">
                    Labor Cost:
                  </span>
                  <span className="text-gray-800 text-xl ml-6">
                    {product.laborCost.toLocaleString("en-US")}$
                  </span>
                </div>
                <div className="mr-4">
                  <span className="text-2xl font-bold text-gray-700">
                    Stone Cost:
                  </span>
                  <span className="text-gray-800 text-xl ml-6">
                    {product.stoneCost.toLocaleString("en-US")}$
                  </span>
                </div>
                <div className="mr-4">
                  <span className="text-2xl font-bold text-gray-700">
                    Promotion:
                  </span>
                  <span className="text-gray-800 text-xl ml-6">
                    {product.promotionID}
                  </span>
                </div>
                <div className="mr-4">
                  <span className="text-2xl font-bold text-gray-700">
                    Category:
                    <span className="text-gray-800 text-xl ml-6">
                    {product.categoryName}
                  </span>
                  </span>
                  <span className="text-gray-800 text-xl ml-6">
                    {product.category}
                  </span>
                </div>
                <div>
                  <span className="font-bold text-2xl text-gray-700">
                    Stock:
                  </span>
                  <span className="text-gray-800 text-xl ml-6">
                    {product.stock} pcs
                  </span>
                </div>
              </div>
              <div className="w-1/2 px-2">
                <button
                  onClick={() => addToCart(product)}
                  className="w-full mt-8 bg-gray-900 dark:bg-gray-600 text-white py-2 px-4 rounded-full font-bold hover:bg-gray-800 dark:hover:bg-gray-700"
                >
                  Add to Cart
                </button>
              </div>
          
            </div>
          </div>
          <div className="mt-20">
            <span className="font-bold text-2xl text-gray-700">
              Product Description:
            </span>
            <p className="text-gray-800 text-xl ml-6">{product.description}</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProductDetailStaff;
