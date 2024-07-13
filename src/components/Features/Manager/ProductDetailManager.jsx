import axios from "axios";
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import { Button, Form, Col } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import 'react-toastify/dist/ReactToastify.css';
import FileUpload from "./FileUpload";

export default function ProductDetailManager() {
  const { id } = useParams();
  const navigate = useNavigate();
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
    categoryName: "",
    status: "",
    stoneName: "",
    stoneType: ""
  });
  const [categories, setCategories] = useState([]);
  const [promotions, setPromotions] = useState([]);
  const [file, setFile] = useState(null);
  const [uploadedUrl, setUploadedUrl] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(
          `https://jewelrysalesystem-backend.onrender.com/api/v2/products/${id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          }
        );
        setProduct(response.data);
      } catch (error) {
        console.error("Error fetching product data", error);
      }
    };

    const fetchCategories = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(
          "https://jewelrysalesystem-backend.onrender.com/api/categories",
          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          }
        );
        setCategories(response.data);
      } catch (error) {
        console.error("Error fetching categories", error);
      }
    };

    const fetchPromotions = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(
          "https://jewelrysalesystem-backend.onrender.com/api/v2/promotions",
          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          }
        );
        setPromotions(response.data);
      } catch (error) {
        console.error("Error fetching promotions", error);
      }
    };

    fetchProduct();
    fetchCategories();
    fetchPromotions();
  }, [id]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setProduct((prevProduct) => ({ ...prevProduct, [name]: value }));
  };

  const handleCopyUrl = () => {
    navigator.clipboard.writeText(uploadedUrl).then(() => {
      alert('URL copied to clipboard');
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const token = localStorage.getItem('token');
    if (!token) {
      toast.error("User not authenticated. Please login.");
      navigate('/login'); // Redirect to login page if token is missing
      return;
    }

    const numericFields = {
      price: product.price,
      refundPrice: product.refundPrice,
      goldWeight: product.goldWeight,
      laborCost: product.laborCost,
      stoneCost: product.stoneCost,
      stock: product.stock,
    };

    for (const [field, value] of Object.entries(numericFields)) {
      if (value < 0) {
        toast.error(`${field.charAt(0).toUpperCase() + field.slice(1)} cannot be 0 or lower`);
        return;
      }
    }

    try {
      const imageUrl = uploadedUrl || product.img;

      const params = {
        name: product.name,
        price: product.price,
        refundPrice: product.refundPrice,
        description: product.description,
        goldWeight: product.goldWeight,
        laborCost: product.laborCost,
        stoneCost: product.stoneCost,
        stock: product.stock,
        categoryName: product.categoryName,
        promotionID: product.promotionID,
        img: imageUrl,
        stoneName: product.stoneName,
        stoneType: product.stoneType
      };

      const response = await axios.put(
        `http://localhost:8080/api/v2/products/${id}`,
        null, // No request body
        {
          params: params, // These are the query parameters
          headers: {
            "Authorization": `Bearer ${token}`
          }
        }
      );

      toast.success("Product updated successfully!");
      navigate(`/productlist2`);
    } catch (error) {
      console.error("Error updating product", error);
      toast.error("Failed to update product. " + (error.response ? error.response.data : ""));
    }
  };

  return (
    <div className="bg-gray-100 py-8">
      <ToastContainer />
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex flex-col md:flex-row -mx-4">
          <div className="md:flex-1 px-4">
            <div className="h-[520px] border-4 border-gray-300 rounded-lg mb-4">
              <img
                className="w-full h-full object-cover rounded-lg"
                src={product.img}
                alt={product.name}
              />
            </div>
          </div>
          <div className="md:flex-1 px-4">
            <h2 className="text-3xl font-bold text-gray-800 mb-6">
              Edit Product
            </h2>
            <Form onSubmit={handleSubmit}>
              <Form.Group as={Col} controlId="formGridName">
                <Form.Label>Name</Form.Label>
                <Form.Control
                  type="text"
                  name="name"
                  value={product.name}
                  onChange={handleInputChange}
                />
              </Form.Group>
              <Form.Group as={Col} controlId="formGridPrice">
                <Form.Label>Price</Form.Label>
                <Form.Control
                  type="number"
                  name="price"
                  value={product.price}
                  onChange={handleInputChange}
                  readOnly
                  className="bg-gray-100 cursor-not-allowed"
                />
              </Form.Group>
              <Form.Group as={Col} controlId="formGridRefundPrice">
                <Form.Label>Refund Price</Form.Label>
                <Form.Control
                  type="number"
                  name="refundPrice"
                  value={product.refundPrice}
                  onChange={handleInputChange}
                  readOnly
                  className="bg-gray-100 cursor-not-allowed"
                />
              </Form.Group>

              <Form.Group as={Col} controlId="formGridGoldWeight">
                <Form.Label>Gold Weight</Form.Label>
                <Form.Control
                  type="number"
                  name="goldWeight"
                  value={product.goldWeight}
                  onChange={handleInputChange}
                />
              </Form.Group>
              <Form.Group as={Col} controlId="formGridLaborCost">
                <Form.Label>Labor Cost</Form.Label>
                <Form.Control
                  type="number"
                  name="laborCost"
                  value={product.laborCost}
                  onChange={handleInputChange}
                />
              </Form.Group>
              <Form.Group as={Col} controlId="formGridStoneCost">
                <Form.Label>Stone Cost</Form.Label>
                <Form.Control
                  type="number"
                  name="stoneCost"
                  value={product.stoneCost}
                  onChange={handleInputChange}
                />
              </Form.Group>
              <Form.Group as={Col} controlId="formGridStock">
                <Form.Label>Stock</Form.Label>
                <Form.Control
                  type="number"
                  name="stock"
                  value={product.stock}
                  onChange={handleInputChange}
                />
              </Form.Group>
              <Form.Group as={Col} controlId="formGridCategoryName">
                <Form.Label>Category</Form.Label>
                <Form.Control
                  as="select"
                  name="categoryName"
                  value={product.categoryName}
                  onChange={handleInputChange}
                >
                  <option>Select Category</option>
                  {categories.map(category => (
                    <option key={category.id} value={category.name}>
                      {category.name}
                    </option>
                  ))}
                </Form.Control>
              </Form.Group>
              <Form.Group as={Col} controlId="formGridPromotionID">
                <Form.Label>Promotion</Form.Label>
                <Form.Control
                  as="select"
                  name="promotionID"
                  value={product.promotionID}
                  onChange={handleInputChange}
                >
                  <option>Select Promotion</option>
                  {promotions.map(promotion => (
                    <option key={promotion.id} value={promotion.id}>
                      {promotion.description}
                    </option>
                  ))}
                </Form.Control>
              </Form.Group>
              <Form.Group as={Col} controlId="formGridDescription">
                <Form.Label>Description</Form.Label>
                <Form.Control
                  as="textarea"
                  rows={3}
                  name="description"
                  value={product.description}
                  onChange={handleInputChange}
                />
              </Form.Group>
              <Form.Group as={Col} controlId="formGridStoneName">
                <Form.Label>Stone Name</Form.Label>
                <Form.Control
                  type="text"
                  name="stoneName"
                  value={product.stoneName}
                  onChange={handleInputChange}
                />
              </Form.Group>
              <Form.Group as={Col} controlId="formGridStoneType">
                <Form.Label>Stone Type</Form.Label>
                <Form.Control
                  type="text"
                  name="stoneType"
                  value={product.stoneType}
                  onChange={handleInputChange}
                />
              </Form.Group>

              <Form.Group as={Col} controlId="formGridName">
                <Form.Label>Img</Form.Label>
                <Form.Control
                  type="string"
                  name="img"
                  value={product.img}
                  onChange={handleInputChange}
                />
              </Form.Group>
              <FileUpload setUploadedUrl={setUploadedUrl} />
              <Button variant="primary" type="submit" className="mt-4">
                Update Product
              </Button>
              <Button onClick={handleCopyUrl} className="ml-4 mt-4">
                Copy Image URL
              </Button>
            </Form>
          </div>
        </div>
      </div>
    </div>
  );
}
