import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function ProductDetailStaff() {
  const token = localStorage.getItem(`token`);
  const { id } = useParams();
  const [staff, setStaff] = useState("");
  const [product, setProduct] = useState({
    img: "",
    name: "",
    price: 0,
    refundPrice: 0,
    description: "",
    goldWeight: 0,
    laborCost: 0,
    stoneCost: 0,
    stock: 0,
    promotionID: "",
    categoryName: "",
    status: "",
  });
  const [cart, setCart] = useState([]);

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/v2/products/${id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log("Fetched data success:", response.data);
        setProduct(response.data);
      } catch (error) {
        console.error("Error fetching product data", error);
      }
    };

    const fetchStaff = async () => {
      if (token) {
        try {
          const response = await axios.get(
            "https://jewelrysalesystem-backend.onrender.com/api/this-info",
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );
          console.log(response.data);
          setStaff(response.data);
          fetchCart(response.data.id);
        } catch (error) {
          console.error("Error fetching staff:", error);
        }
      } else {
        console.error("No token found");
      }
    };

    const fetchCart = (staffId) => {
      if (token) {
        axios
          .get(
            `https://jewelrysalesystem-backend.onrender.com/cart?staffId=${staffId}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          )
          .then((response) => {
            setCart(response.data);
          })
          .catch((error) => {
            console.error("Error fetching cart data:", error);
          });
      } else {
        console.error("No token found");
      }
    };

    fetchProduct();
    fetchStaff();
  }, [id]);

  const addToCart = (product) => {
    if (product.stock <= 0) {
      toast.error("Product is out of stock.");
      return;
    }
  
    const isAlreadyInCart = cart.some((item) => item.product.id === product.id);
    if (isAlreadyInCart) {
      toast.error("Product is already in the cart.");
      return;
    }
  
    const priceToAdd = product.discountPrice ? product.discountPrice : product.price;
  
    if (token) {
      axios
        .post(
          `https://jewelrysalesystem-backend.onrender.com/cart?staffId=${staff.id}`,
          { ...product, price: priceToAdd },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then((response) => {
          toast.success("Item added to cart:", response.data);
          setCart([...cart, { product, quantity: 1, price: priceToAdd }]);
        })
        .catch((error) => {
          toast.error(
            `${error.response ? error.response.data : error.message}`
          );
          console.error("Error adding item to cart:", error);
        });
    } else {
      console.error("No token found");
    }
  };
  const navigate = useNavigate();
  const handleBack= ()=>{
    navigate("/productlist");
  }
  return (
    <div className="h-screen">
      <ToastContainer />
      <div className="bg-gray-100 py-8 h-full">
      <button onClick={handleBack}
      type="button" class="flex items-center justify-center w-1/2 px-5 py-2 text-sm text-black transition-colors duration-200 bg-white border rounded-lg gap-x-2 sm:w-auto dark:hover:bg-gray-800 dark:bg-gray-900 hover:bg-gray-100 dark:text-gray-200 dark:border-gray-700">
    <svg class="w-5 h-5 rtl:rotate-180" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" d="M6.75 15.75L3 12m0 0l3.75-3.75M3 12h18" />
    </svg>
    <span>Go back</span>
</button>
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
                  {product.discountPrice ? (
                    <>
                      <span className="text-gray-400  text-lg line-through pl-2">
                        {(product.price ?? 0).toLocaleString("en-US")} $
                      </span>
                      <span className="text-gray-900  text-lg pl-2">
                        {(product.discountPrice ?? 0).toLocaleString("en-US")} $
                      </span>
                    </>
                  ) : (
                    <span className="text-gray-900 text-lg">
                      {(product.price ?? 0).toLocaleString("en-US")} $
                    </span>
                  )}
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
                    Stone Name:
                  </span>
                  <span className="text-gray-800 text-xl ml-6">
                    {product.stoneName}
                  </span>
                </div>
                <div className="mr-4">
                  <span className="text-2xl font-bold text-gray-700">
                    Stone Type:
                  </span>
                  <span className="text-gray-800 text-xl ml-6">
                    {product.stoneType}
                  </span>
                </div>
                <div className="mr-4">
                  <span className="text-2xl font-bold text-gray-700">
                    Category:
                  </span>
                  <span className="text-gray-800 text-xl ml-6">
                    {product.categoryName}
                  </span>
                </div>
                <div>
                  <span className="font-bold text-2xl text-gray-700">
                    Stock:
                  </span>
                  <span className="text-gray-800 text-xl ml-6">
                    {product.stock} pieces
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
