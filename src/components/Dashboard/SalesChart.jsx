// src/components/Dashboard/SalesChart.jsx

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const SalesChart = () => {
    const [chartData, setChartData] = useState({
        labels: [],
        datasets: [],
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('token'); // Lấy token từ localStorage

        const fetchData = async () => {
            try {
                const response = await axios.get('https://jewelrysalesystem-backend.onrender.com/api/v2/counters/sales', {
                    headers: {
                        Authorization: `Bearer ${token}`, // Thêm token vào header
                    },
                });
                const data = response.data;

                const counterIDs = data.map(item => item.counterID);
                const totalSales = data.map(item => item.totalSales);

                setChartData({
                    labels: counterIDs,
                    datasets: [
                        {
                            label: 'Total Sales (By $)',
                            data: totalSales,
                            backgroundColor: 'rgba(75, 192, 192, 0.6)',
                            borderColor: 'rgba(75, 192, 192, 1)',
                            borderWidth: 1,
                        },
                    ],
                });
                setLoading(false);
            } catch (error) {
                console.error('Error fetching sales data', error);
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h2>Total Sales by Counter</h2>
            <Bar
                data={chartData}
                options={{
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                    },
                }}
            />
        </div>
    );
};

export default SalesChart;
