import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './TopCustomersTable.css';

const TopCustomersTable = () => {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/v2/customers/top');
                const data = response.data;

                // Sort data by totalPurchases
                const sortedData = data.sort((a, b) => b.totalPurchases - a.totalPurchases);

                setCustomers(sortedData);
                setLoading(false);
            } catch (error) {
                console.error('Error fetching top customers data', error);
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div className="top-customers-container">
            <h2>Top Customers Based on Purchases</h2>
            <table className="top-customers-table">
                <thead>
                    <tr>
                        <th>Rank</th>
                        <th>Customer Name</th>
                        <th>Total Purchases</th>
                    </tr>
                </thead>
                <tbody>
                    {customers.map((customer, index) => (
                        <tr key={index}>
                            <td>{index + 1}</td>
                            <td>{customer.name}</td>
                            <td>{customer.totalPurchases}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default TopCustomersTable;
