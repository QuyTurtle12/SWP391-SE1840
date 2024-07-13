import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './TopSellingProductsTable.css';

const TopSellingProductsTable = () => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('token'); // Get token from localStorage

        const fetchData = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/v2/orders/order/products/top', {
                    headers: {
                        Authorization: `Bearer ${token}`, // Include token in headers
                    },
                });
                const data = response.data;

                // Sort data by totalAmount and take the top 10
                const sortedData = data.sort((a, b) => b.totalAmount - a.totalAmount).slice(0, 10);

                setProducts(sortedData);
                setLoading(false);
            } catch (error) {
                console.error('Error fetching top-selling products data', error);
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div className="top-selling-products-container">
            <h2>Top 10 Selling Products</h2>
            <table className="top-selling-products-table">
                <thead>
                    <tr>
                        <th>Rank</th>
                        <th>Product Name</th>
                        <th>Total Sales</th>
                    </tr>
                </thead>
                <tbody>
                    {products.map((product, index) => (
                        <tr key={index}>
                            <td>{index + 1}</td>
                            <td>{product.productName}</td>
                            <td>{product.totalAmount}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default TopSellingProductsTable;
