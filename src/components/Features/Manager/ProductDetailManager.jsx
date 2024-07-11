import axios from "axios";
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import { Button, Form, Col } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import 'react-toastify/dist/ReactToastify.css';

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
  });
  const [categories, setCategories] = useState([]);
  const [promotions, setPromotions] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);

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

  const handleFileChange = (e) => {
    setSelectedFile(e.target.files[0]);
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
      if (value <= 0) {
        toast.error(`${field.charAt(0).toUpperCase() + field.slice(1)} cannot be 0 or lower`);
        return;
      }
    }

    try {
      let imageUrl = product.img;

      if (selectedFile) {
        const formData = new FormData();
        formData.append("file", selectedFile);

        const uploadResponse = await axios.post(
          "https://jewelrysalesystem-backend.onrender.com/upload/image",
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
              "Authorization": `Bearer ${token}`
            },
          }
        );

        imageUrl = uploadResponse.data;
      }

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
      };
      
      const updateResponse = await axios.put(
        `https://jewelrysalesystem-backend.onrender.com/api/v2/products/${id}`,
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
    <div className="h-full">
      <ToastContainer />
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
                  />
                </Form.Group>
                <Form.Group as={Col} controlId="formGridRefundPrice">
                  <Form.Label>Refund Price</Form.Label>
                  <Form.Control
                    type="number"
                    name="refundPrice"
                    value={product.refundPrice}
                    onChange={handleInputChange}
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
                <Form.Group as={Col} controlId="formGridPromotionID">
                  <Form.Label>Promotion</Form.Label>
                  <Form.Control
                    as="select"
                    name="promotionID"
                    value={product.promotionID}
                    onChange={handleInputChange}
                  >
                    {promotions.map((promotion) => (
                      <option key={promotion.id} value={promotion.id}>
                        {promotion.description}
                      </option>
                    ))}
                  </Form.Control>
                </Form.Group>
                <Form.Group as={Col} controlId="formGridCategory">
                  <Form.Label>Category</Form.Label>
                  <Form.Control
                    as="select"
                    name="categoryName"
                    value={product.categoryName}
                    onChange={handleInputChange}
                  >
                    {categories.map((category) => (
                      <option key={category.id} value={category.name}>
                        {category.name}
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
                <Form.Group as={Col} controlId="formFile">
                  <Form.Label>Upload Image</Form.Label>
                  <Form.Control type="file" onChange={handleFileChange} />
                </Form.Group>
                <Button variant="primary" type="submit" className="mt-4">
                  Update Product
                </Button>
              </Form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
