import axios from "axios";
import React, { useEffect, useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import { ToastContainer, toast } from "react-toastify";
import { useLocation } from "react-router-dom";

const token = localStorage.getItem("token"); // Fetch the token from local storage

// Create an Axios instance with default headers
const axiosInstance = axios.create({
  headers: {
    Authorization: `Bearer ${token}`,
  },
});

function ViewCart() {
  const [cart, setCart] = useState([]);
  const [subtotal, setSubtotal] = useState(0);
  const [showModal, setShowModal] = useState(false);
  const [showModalVoucher, setShowModalVoucher] = useState(false);
  const [showModalPoint, setShowModalPoint] = useState(false);
  const [staff, setStaff] = useState();
  const [discountRate, setDiscountRate] = useState(0);
  const [counterID, setCounterID] = useState("");
  const [customerPhone, setCustomerPhone] = useState("");
  const [discountApplied, setDiscountApplied] = useState(0);
  const [vouchers, setVouchers] = useState([]);
  const [selectedVoucher, setSelectedVoucher] = useState("");
  const [selectedPoint, setSelectedPoint] = useState("");
  const [pointsToApply, setPointsToApply] = useState(0);
  const [customerGender, setCustomerGender] = useState("");
  const [customerName, setCustomerName] = useState("");
  const [discountName, setDiscountName] = useState("None");
  const [point, setPoint] = useState(0);
  const [cusphone, setCusphone] = useState(0);
  const location = useLocation();
  const [paymentMethod, setPaymentMethod] = useState("");
  const queryParams = new URLSearchParams(location.search);
  const staffId = queryParams.get("staffId");

  useEffect(() => {
    fetchCartData();
    fetchStaff();
  }, []);

  const fetchCartData = () => {
    axiosInstance
      .get(
        `https://jewelrysalesystem-backend.onrender.com/cart?staffId=${staffId}`
      )
      .then((response) => {
        setCart(response.data);
        calculateSubtotal(response.data);
      })
      .catch((error) => {
        console.error("Error fetching cart data", error);
      });
  };

  const fetchDiscounts = () => {
    axiosInstance
      .get(
        `http://localhost:8080/api/customer-promotions/customer-coupons?totalPrice=${subtotal}`,
        {}
      )
      .then((response) => {
        setVouchers(response.data);

        console.log(response.data.discountRate);
        console.log(response.data);
      })
      .catch((error) => {
        console.error("Error fetching discount data", error);
      });
  };
  const fetchPoint = () => {
    axiosInstance
      .get(
        `http://localhost:8080/api/v2/customers/customer/point?customerPhone=${cusphone}`,
        {}
      )
      .then((response) => {
        setPoint(response.data);
        console.log(response.data);
      })
      .catch((error) => {
        console.error("Error fetching discount data", error);
      });
  };
  const handlePointApply = () => {
    fetchPoint();
    if (pointsToApply === 0 || pointsToApply > point) {
      toast.error("Invalid points input. Points must be greater than 0 and less than or equal to available points.");
      return;
    }
    // Apply points to the subtotal
    // setSubtotal(subtotal - pointsToApply);  // 1 point = 1$
    // Show success message or further action
    toast.success(`Points applied successfully with ${pointsToApply} points`);
  
    // Close the points modal
    handleClosePoint();
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
        setCounterID(response.data.counterID);
        setStaff(response.data);
        fetchCartData(response.data.id); // Fetch cart data with the staff ID after setting the staff state
      } catch (error) {
        console.error("Error fetching staff:", error);
      }
    } else {
      console.error("No token found");
    }
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

  const handleVoucher = () => {
    fetchDiscounts();
    setShowModalVoucher(true);
  };
  const handlePoint = () => {
    // fetchDiscounts();
    setShowModalPoint(true);
  };
  const handleClose = () => {
    setShowModal(false);
  };

  const handleCloseVoucher = () => {
    setShowModalVoucher(false);
  };
  const handleClosePoint = () => {
    setShowModalPoint(false);
  };
  const handleVoucherApply = () => {
    if (!selectedVoucher) {
      toast.error("Please select a voucher");
      return;
    }

    // Find the selected voucher
    const selectedVoucherObj = vouchers.find(
      (voucher) => voucher.id === parseInt(selectedVoucher)
    );

    if (!selectedVoucherObj) {
      toast.error("Voucher not found");
      return;
    }

    // Calculate discount based on voucher rate
    const discountRate = selectedVoucherObj.discountRate;
    const discountName = selectedVoucherObj.discountName;
    setDiscountRate(discountRate);
    setDiscountName(discountName);
    const discountAmount = subtotal * discountRate;
    // Update discountApplied state
    setDiscountApplied(discountAmount);

    // Show success message or further action
    toast.success(
      `Voucher applied successfully with discount of $${discountAmount.toLocaleString(
        "en-US"
      )}`
    );

    // Close the voucher modal
    handleCloseVoucher();
  };

  const handleCreatePayment = (amount) => {
    axiosInstance
      .post(`http://localhost:8080/api/create_payment?amount=${amount}`)
      .then((response) => {
        const { data } = response;
        const paymentUrl = data.data;
        window.location.href = paymentUrl;
      })
      .catch((error) => {
        console.log(error);
        toast.error("Error creating payment URL", error);
      });
  };

  const handleCreateOrder = () => {
    if (!subtotal || !customerPhone) {
      toast.error("Fill all the fields");
      return;
    }

    axiosInstance
      .post(
        `https://jewelrysalesystem-backend.onrender.com/api/v2/orders?totalPrice=${subtotal}&staffID=${staffId}&counterID=${counterID}&customerPhone=${customerPhone}&customerName=${customerName}&customerGender=${customerGender}&discountRate=${discountRate}&pointApplied=${point}&discountName=${discountName}`,
        cart
      )

      
      .then(() => {
        toast.success("Order created successfully!");
      
        if (paymentMethod === "online") {
          const amountInVND = Math.floor(finalPrice); // Convert to integer
          handleCreatePayment(amountInVND);        }
        handleClearCart();
        setShowModal(false)
      })
      .catch((error) => {
        const errorMessage = error.response.data
          ? error.response.data
          : error.message;
        console.error(errorMessage);
        toast.error(errorMessage);
      });
  };

  const handleClearCart = () => {
    axiosInstance
      .put(
        `https://jewelrysalesystem-backend.onrender.com/cart/clear?staffId=${staffId}`
      )
      .then((response) => {
        setCart([]);
        setSubtotal(0);
        console.log(response.data);
      })
      .catch((error) => {
        console.error("Error clearing cart", error);
      });
  };
  const handleRemoveVoucher = () => {
    setDiscountRate(0);
    // setDiscountName("None");
    setDiscountApplied(0);
    setSelectedVoucher("");
    toast.success("Voucher removed successfully");
  };
  const handleRemovePoint = () => {
    setPoint(0);
    setPointsToApply(0);
    toast.success("Point removed successfully");
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
    axiosInstance
      .put(
        `https://jewelrysalesystem-backend.onrender.com/cart?staffId=${staffId}&productID=${productID}&quantity=${newQuantity}`,
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
  const finalPrice = (subtotal-discountApplied-pointsToApply)
  const handleRemoveItem = (productID) => {
    axiosInstance
      .delete(
        `https://jewelrysalesystem-backend.onrender.com/cart?staffId=${staffId}&productID=${productID}`
      )
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
                            </button>
                            <button
                              className="bg-blue-500 text-white py-1 px-3 ml-2 rounded"
                              onClick={() =>
                                handleUpdateQuantity(
                                  parseInt(item.product.id),
                                  item.quantity
                                )
                              }
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
                  <h2 className="text-xl font-semibold mb-4">Order Summary</h2>
                  <div className="mb-4">
                    <div className="flex justify-between">
                      <span className="font-semibold">Subtotal</span>
                      <span>${subtotal.toLocaleString("en-US")}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="font-semibold">Discount with voucher</span>
                      <span>- ${discountApplied.toLocaleString("en-US")}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="font-semibold">Discount with point</span>
                      <span>- ${pointsToApply.toLocaleString("en-US")}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="font-semibold">Total</span>
                      <span>
                        ${(finalPrice).toLocaleString("en-US")}
                      </span>
                    </div>
                  </div>

                  <button
                    className="mb-4 bg-red-500 text-white py-2 px-4 rounded w-full"
                    onClick={handleVoucher}
                  >
                    Add Voucher
                  </button>
                  <button
                    className="mb-4 bg-pink-500 text-white py-2 px-4 rounded w-full"
                    onClick={handlePoint}
                  >
                    Add Customer Point
                  </button>
                  {discountApplied > 0 && (
                    <button
                      className="mb-4 bg-red-300 text-white py-2 px-4 rounded w-full"
                      onClick={handleRemoveVoucher}
                    >
                      Remove Voucher
                    </button>
                  )}
                  {point > 0 && (
                    <button
                      className="mb-4 bg-red-300 text-white py-2 px-4 rounded w-full"
                      onClick={handleRemovePoint}
                    >
                      Remove Point
                    </button>
                  )}
                  <button
                    className=" bg-black text-white py-2 px-4 rounded w-full"
                    onClick={handleCheckout}
                  >
                    Checkout
                  </button>
                  
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
      <Modal show={showModal} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Checkout</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="formPhoneNumber">
              <Form.Label>Phone Number</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter customer phone number"
                value={customerPhone}
                onChange={(e) => setCustomerPhone(e.target.value)}
              />
            </Form.Group>
            <Form.Group controlId="formCustomerName">
              <Form.Label>Customer Name</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter customer name"
                value={customerName}
                onChange={(e) => setCustomerName(e.target.value)}
              />
            </Form.Group>
            <Form.Group controlId="formCustomerGender">
              <Form.Label>Customer Gender</Form.Label>
              <Form.Control
                as="select"
                value={customerGender}
                onChange={(e) => setCustomerGender(e.target.value)}
              >
                <option value="">Select Gender</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
              </Form.Control>
              <Form.Group controlId="paymentMethod">
              <Form.Label>Payment Method</Form.Label>
              <Form.Control
                as="select"
                value={paymentMethod}
                onChange={(e) => setPaymentMethod(e.target.value)}
              >
                <option value="">Select Payment Method</option>
                <option value="online">Online</option>
                <option value="offline">Offline</option>
              </Form.Control>
            </Form.Group>
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
      <Modal show={showModalVoucher} onHide={handleCloseVoucher}>
        <Modal.Header closeButton>
          <Modal.Title>Select Voucher</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="formVoucher">
              <Form.Label>Voucher</Form.Label>
              <Form.Control
                as="select"
                value={selectedVoucher}
                onChange={(e) => setSelectedVoucher(e.target.value)}
              >
                <option>Select Voucher</option>
                {vouchers.map((voucher) => (
                  <option key={voucher.id} value={voucher.id}>
                    {voucher.discountName}:{" "}
                    {Math.floor(voucher.discountRate * 100)}%
                  </option>
                ))}
              </Form.Control>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseVoucher}>
            Close
          </Button>
          <Button variant="primary" onClick={handleVoucherApply}>
            Apply Voucher
          </Button>
        </Modal.Footer>
      </Modal>
      <Modal show={showModalPoint} onHide={handleClosePoint}>
  <Modal.Header closeButton>
    <Modal.Title>Apply Points</Modal.Title>
  </Modal.Header>
  <Modal.Body>
    <Form.Group controlId="customerPhonePoints">
      <Form.Label>Customer Phone</Form.Label>
      <Form.Control
        type="text"
        placeholder="Enter customer phone"
        value={cusphone}
        onChange={(e) => setCusphone(e.target.value)}
      />
    </Form.Group>
    <Button variant="primary" onClick={fetchPoint}>
      Fetch Points
    </Button>
    {point > 0 && (
      <>
        <p>Customer Points: {point}</p>
        <Form.Group controlId="pointsToApply">
          <Form.Label>Points to Apply</Form.Label>
          <Form.Control
            type="number"
            placeholder="Enter points to apply"
            value={pointsToApply}
            onChange={(e) => setPointsToApply(parseInt(e.target.value))}
          />
        </Form.Group>
      </>
    )}
  </Modal.Body>
  <Modal.Footer>
    <Button variant="secondary" onClick={handleClosePoint}>
      Close
    </Button>
    <Button variant="primary" onClick={handlePointApply}>
      Apply Points
    </Button>
  </Modal.Footer>
</Modal>


    </>
  );
}

export default ViewCart;
