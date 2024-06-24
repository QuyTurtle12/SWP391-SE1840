import axios from "axios";
import React, { useEffect, useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import { ToastContainer, toast } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate } from "react-router-dom";

function RefundViewCart() {
  const [cart, setCart] = useState([]);
  const [subtotal, setSubtotal] = useState(0);
  const [showModal, setShowModal] = useState(false);
  const [orderID, setOrderID] = useState(1);
  const [staffID, setStaffID] = useState(""); 
  const [customerPhone, setCustomerPhone] = useState(""); 
  const navigate = useNavigate();
  useEffect(() => {
    fetchCartData();
  }, []);

  const fetchCartData = () => {
    axios
      .get("http://localhost:8080/cart")
      .then((response) => {
        setCart(response.data);
        calculateSubtotal(response.data);
      })
      .catch((error) => {
        console.error("Error fetching cart data", error);
      });
  };

  const calculateSubtotal = (cartItems) => {
    let total = 0;
    cartItems.forEach((item) => {
      if (item.product.refundPrice && item.quantity) {
        total += item.product.refundPrice * item.quantity;
      }
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
    axios
      .post(
        `http://localhost:8080/api/refunds?ID=${orderID}&totalPrice=${subtotal}&customerPhone=${customerPhone}&staffID=${staffID}`,
        cart
      )
      .then((response) => {
        console.log(response.data);
        toast.success("Create order successfully!");

        // Handle successful order creation
        handleClearCart(); // clear cart after success checkout
        setShowModal(false);
          navigate(`/refund-form/${orderID}`);
       
      
      })
      .catch((error) => {
        console.error("Error creating order", error);
        const errorMessage = error.response ? error.response.data : error.message;
        toast.error(`${errorMessage}`);
      });
  };

  const handleClearCart = () => {
    axios
      .put("http://localhost:8080/cart/clear")
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
    const updatedCart = cart.map((item) => {
      if (item.product.id === productId) {
        return { ...item, quantity: newQuantity }; // Update quantity for the specific product
      }
      return item;
    });
    setCart(updatedCart); // Update the cart state with the updated quantity
    calculateSubtotal(updatedCart); // Recalculate subtotal based on updated quantities
  };

  const handleUpdateQuantity = (productID, newQuantity) => {
    axios
      .put(
        `http://localhost:8080/cart?productID=${productID}&quantity=${newQuantity}`,
        {}
      )
      .then((response) => {
        console.log(response);
        console.log("Quantity updated successfully" + newQuantity);
        fetchCartData(); // Fetch updated cart data after update
      })
      .catch((error) => {
        console.error("Error updating quantity", error);
      });
  };

  const handleRemoveItem = (productID) => {
    axios
      .delete(`http://localhost:8080/cart?productID=${productID}`)
      .then((response) => {
        console.log("Product removed successfully");
        fetchCartData(); // Fetch updated cart data after removal
      })
      .catch((error) => {
        console.error("Error removing product", error);
      });
  };

  return (
    <div className="bg-white h-screen py-8">
      <ToastContainer/>
      <div className="container mx-auto px-4">
        <h1 className="text-2xl font-bold mb-4">Refund Cart</h1>
        {cart.length === 0 ? (
          <>
            <div className="bg-white text-red-900 rounded-lg font-bold shadow-md p-6 mb-4">
              cart refund is empty
            </div>
            <a href="/refund-list">
              <button className="bg-white text-black py-1 px-3 font-bold border-2 mb-4 border-black rounded">
                Continue adding refund product{" "}
              </button>
            </a>
          </>
        ) : (
          <div className="flex flex-col md:flex-row gap-4">
            <div className="md:w-3/4">
              <a href="/refund-list">
                <button className="bg-white text-black py-1 px-3 font-bold border-2 mb-4 border-black rounded">
                  Continue adding refund product{" "}
                </button>
              </a>
              <div className="bg-white rounded-lg shadow-md p-6 mb-4">
                <table className="w-full">
                  <thead>
                    <tr>
                      <th className="text-left font-bold">Product</th>
                      <th className="text-left font-bold">Refund Price</th>
                      <th className="text-left font-bold">Quantity</th>
                      <th className="text-left font-bold">Total</th>
                      <th className="text-left font-bold">Actions</th>
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
                          ${item.product.refundPrice.toLocaleString("en-US")}
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
                                parseInt(e.target.value) // Ensure to parse the value correctly
                              )
                            }
                          />
                        </td>
                        <td className="py-4">
                          ${(item.product.refundPrice * item.quantity).toLocaleString("en-US")}
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
                              console.log(item.product.id);
                              console.log(item.quantity);
                              handleUpdateQuantity(
                                item.product.id,
                                item.quantity // Use newQuantity here
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
                  <span>Total Refund Price</span>
                  <span>${subtotal.toLocaleString("en-US")}</span>
                </div>
                <hr className="my-2" />
                <div className="flex justify-between mb-2">
                  <span className="font-semibold">Total Refund Price</span>
                  <span className="font-semibold">${subtotal.toLocaleString("en-US")}</span>
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
            <Form.Group controlId="formOrderID">
              <Form.Label>Order Refund ID</Form.Label>
              <Form.Control
                type="number"
                placeholder="Enter order ID"
                value={orderID}
                onChange={(e) => setOrderID(e.target.value)}
              />
            </Form.Group>
            <Form.Group controlId="formStaffID">
              <Form.Label>Staff ID</Form.Label>
              <Form.Control
                type="number"
                placeholder="Enter staff ID"
                value={staffID}
                onChange={(e) => setStaffID(e.target.value)}
              />
            </Form.Group>

            <Form.Group controlId="formCounterID">
              <Form.Label>Customer Phone</Form.Label>
              <Form.Control
                type="number"
                placeholder="Enter counter ID"
                value={customerPhone}
                onChange={(e) => setCustomerPhone(e.target.value)}
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
  );
}

export default RefundViewCart;
