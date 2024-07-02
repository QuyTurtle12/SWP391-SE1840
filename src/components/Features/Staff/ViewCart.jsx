import axios from "axios";
import React, { useEffect, useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import { ToastContainer, toast } from "react-toastify";
import { useLocation } from 'react-router-dom';

function ViewCart() {
  const [cart, setCart] = useState([]);
  const [subtotal, setSubtotal] = useState(0);
  const [showModal, setShowModal] = useState(false);
  const [orderID, setOrderID] = useState(""); 
  const [staffID, setStaffID] = useState(""); 
  const [counterID, setCounterID] = useState(""); 
  const [customerPhone, setCustomerPhone] = useState(""); 
  const [discountApplied, setDiscountApplied] = useState(0); 
  const [customerGender,setCustomerGender ] = useState("");
  const [customerName,setCustomerName ] = useState("");
  
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const staffId = queryParams.get('staffId');
  
  useEffect(() => {
    fetchCartData();
  }, []);

  useEffect(() => {
    if (staffID) {
      fetchStaffDetails(staffID);
    }
  }, [staffID]);

  const fetchCartData = () => {
    axios
      .get(`http://localhost:8080/cart?staffId=${staffId}`)
      .then((response) => {
        setCart(response.data);
        calculateSubtotal(response.data);
      })
      .catch((error) => {
        console.error("Error fetching cart data", error);
      });
  };

  const fetchStaffDetails = (staffID) => {
    axios
      .get(`http://localhost:8080/api/v2/accounts/user?id=${staffID}`)
      .then((response) => {
        setCounterID(response.data.counterID);
      })
      .catch((error) => {
        console.error("Error fetching staff details", error);
      });
  };

  const calculateSubtotal = (cartItems) => {
    let total = 0;
    cartItems.forEach((item) => {
      total += item.product.price * item.quantity;
    });
    setSubtotal(total);
  };

  const handleCheckout = () => {
    setShowModal(true);
  };

  const handleClose = () => {
    setShowModal(false);
  };

  const handleCreateOrder = () => {
    if (
      !subtotal ||
      !staffID ||
      !counterID ||
      !customerPhone ||
      discountApplied === ""
    ) {
      toast.error("Fill all the fields");
      return;
    }
    
    axios
      .post(
        `http://localhost:8080/api/v2/orders?totalPrice=${subtotal}&staffID=${staffID}&counterID=${counterID}&customerPhone=${customerPhone}&customerName=${customerName}&customerGender=${customerGender}&discountApplied=${discountApplied}`,
        cart
      )
      .then(() => {
        toast.success("Order created successfully!");
        handleClearCart();
        setShowModal(false);
      })
      .catch((error) => {
        const errorMessage = error.response
          ? error.response.data
          : error.message;
        console.error(errorMessage);
        toast.error(errorMessage);
      });
  };

  const handleClearCart = () => {
    axios
      .put(`http://localhost:8080/cart/clear?staffId=${staffId}`)
      .then((response) => {
        setCart([]);
        setSubtotal(0);
        console.log(response.data);
      })
      .catch((error) => {
        console.error("Error clearing cart", error);
      });
  };

  const handleChangeQuantity = (productId, newQuantity) => {
    if (newQuantity < 1) {
      toast.error("Quantity cannot be less than 1");
      return;
    }
    const updatedCart = cart.map((item) => {
      if (item.product.id === productId) {
        return { ...item, quantity: newQuantity };
      }
      return item;
    });
    setCart(updatedCart);
    calculateSubtotal(updatedCart);
  };

  const handleUpdateQuantity = (productID, newQuantity) => {
    axios
      .put(
        `http://localhost:8080/cart?staffId=${staffId}&productID=${productID}&quantity=${newQuantity}`,
        {}
      )
      .then(() => {
        toast.success("Updated successfully with quantity: " + newQuantity);
        fetchCartData();
      })
      .catch((error) => {
        toast.error(`${error.response ? error.response.data : error.message}`);
        console.error("Error updating quantity", error);
        fetchCartData();
      });
  };

  const handleRemoveItem = (productID) => {
    axios
      .delete(`http://localhost:8080/cart?staffId=${staffId}&productID=${productID}`)
      .then(() => {
        toast.success("Product removed successfully");
        fetchCartData();
      })
      .catch((error) => {
        toast.error("Error removing product", error);
      });
  };
  return (
    <>
      <ToastContainer />
      <div className="bg-white h-screen py-8">
        <div className="container mx-auto px-4">
          <h1 className="text-2xl font-semibold mb-4">Shopping Cart</h1>
          {cart.length === 0 ? (
            <>
              <div className="bg-white text-red-900 rounded-lg font-bold shadow-md p-6 mb-4">
                Cart is empty
              </div>
              <a href="/productlist">
                <button className="bg-white text-black py-1 px-3 font-bold border-2 mb-4 border-black rounded">
                  Continue shopping
                </button>
              </a>
            </>
          ) : (
            <div className="flex flex-col md:flex-row gap-4">
              <div className="md:w-3/4">
                <a href="/productlist">
                  <button className="bg-white text-black py-1 px-3 font-bold border-2 mb-4 border-black rounded">
                    Continue shopping
                  </button>
                </a>
                <div className="bg-white rounded-lg shadow-md p-6 mb-4">
                  <table className="w-full">
                    <thead>
                      <tr>
                        <th className="text-left font-semibold">Product</th>
                        <th className="text-left font-semibold">Price</th>
                        <th className="text-left font-semibold">Quantity</th>
                        <th className="text-left font-semibold">Total</th>
                        <th className="text-left font-semibold">Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      {cart.map((item) => (
                        <tr key={item.product.id}>
                          <td className="py-4">
                            <div className="flex items-center">
                              <img
                                className="h-16 w-16 mr-4"
                                src={item.product.img}
                                alt={item.product.name}
                              />
                              <span className="font-semibold">
                                {item.product.name}
                              </span>
                            </div>
                          </td>
                          <td className="py-4">
                            ${item.product.price.toLocaleString("en-US")}
                          </td>
                          <td className="py-4">
                            <input
                              type="number"
                              value={item.quantity}
                              min="1"
                              className="w-16 border rounded px-2"
                              onChange={(e) =>
                                handleChangeQuantity(
                                  parseInt(item.product.id),
                                  parseInt(e.target.value)
                                )
                              }
                            />
                          </td>
                          <td className="py-4">
                            $
                            {(
                              item.product.price * item.quantity
                            ).toLocaleString("en-US")}
                          </td>
                          <td className="py-4">
                            <button
                              className="bg-gray-400 text-white py-1 px-3 rounded"
                              onClick={() => handleRemoveItem(item.product.id)}
                            >
                              Remove
                            </button>{" "}
                            <button
                              className="bg-black text-white py-1 px-3 rounded"
                              onClick={() => {
                                handleUpdateQuantity(
                                  item.product.id,
                                  item.quantity
                                );
                              }}
                            >
                              Update
                            </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
              <div className="md:w-1/4">
                <div className="bg-white rounded-lg shadow-md p-6">
                  <h2 className="text-lg font-semibold mb-4">Summary</h2>
                  <div className="flex justify-between mb-2">
                    <span>Subtotal</span>
                    <span>${subtotal.toLocaleString("en-US")}</span>
                  </div>
                  <hr className="my-2" />
                  <div className="flex justify-between mb-2">
                    <span className="font-semibold">Total</span>
                    <span className="font-semibold">
                      ${subtotal.toLocaleString("en-US")}
                    </span>
                  </div>
                  <button
                    className="bg-black text-white py-2 px-4 rounded-lg mt-4 w-full"
                    onClick={handleCheckout}
                  >
                    Checkout
                  </button>
                  <button
                    className="bg-gray-400 text-white py-2 px-4 rounded-lg mt-4 w-full"
                    onClick={handleClearCart}
                  >
                    Clear Cart
                  </button>
                </div>
              </div>
            </div>
          )}
        </div>

        <Modal show={showModal} onHide={handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Create Order</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
             
              <Form.Group controlId="formStaffID">
                <Form.Label>Staff ID</Form.Label>
                <Form.Control
                  required
                  type="number"
                  placeholder="Enter staff ID"
                  value={staffID}
                  onChange={(e) => setStaffID(e.target.value)}
                />
              </Form.Group>

              <Form.Group controlId="formCounterID">
                <Form.Label>Counter ID</Form.Label>
                <Form.Control
                  required
                  type="number"
                  value={counterID}
                  readOnly
                  className="bg-gray-100 border border-gray-300 text-gray-700 py-2 px-4 rounded" // Tailwind CSS classes
                />
              </Form.Group>

              <Form.Group controlId="formCustomerName">
                <Form.Label>Customer Name</Form.Label>
                <Form.Control
                  required
                  placeholder="Enter customer Name"
                  value={customerName}
                  onChange={(e) => setCustomerName(e.target.value)}
                />
              </Form.Group>

              <Form.Group controlId="formCustomerGender">
                <Form.Label>Customer Gender</Form.Label>
                <Form.Control
                as="select"
                  required
                  placeholder="Enter customer gender"
                  value={customerGender}
                  onChange={(e) => setCustomerGender(e.target.value)}
                >
                <option value="" disabled>Select Gender</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
                </Form.Control>
              </Form.Group>

              <Form.Group controlId="formCustomerPhone">
                <Form.Label>Customer Phone</Form.Label>
                <Form.Control
                  type="number"
                  required
                  placeholder="Enter customer Phone"
                  value={customerPhone}
                  onChange={(e) => setCustomerPhone(e.target.value)}
                />
              </Form.Group>
              <Form.Group controlId="formDiscountApplied">
                <Form.Label>Discount Applied</Form.Label>
                <Form.Control
                  type="number"
                  required
                  placeholder="Enter discount"
                  value={discountApplied}
                  onChange={(e) => setDiscountApplied(e.target.value)}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
              Close
            </Button>
            <Button variant="primary" onClick={handleCreateOrder}>
              Create Order
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    </>
  );
}

export default ViewCart;
