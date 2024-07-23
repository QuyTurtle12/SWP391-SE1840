import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const CurrentStockLevelsChart = () => {
    const [chartData, setChartData] = useState({
        labels: [],
        datasets: [],
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            const token = localStorage.getItem('token'); // Retrieve the token from localStorage

            try {
                const response = await axios.get('https://jewelrysalesystem-backend.onrender.com/api/v2/products', {
                    headers: {
                        Authorization: `Bearer ${token}`, // Add the token to the headers
                    },
                });
                const data = response.data;

                const productNames = data.map(item => item.name);
                const stockLevels = data.map(item => item.stock);

                setChartData({
                    labels: productNames,
                    datasets: [
                        {
                            label: 'Stock Levels (pcs)',
                            data: stockLevels,
                            backgroundColor: stockLevels.map(stock => stock <= 10 ? 'rgba(255, 99, 132, 0.6)' : 'rgba(75, 192, 192, 0.6)'),
                            borderColor: stockLevels.map(stock => stock <= 10 ? 'rgba(255, 99, 132, 1)' : 'rgba(75, 192, 192, 1)'),
                            borderWidth: 1,
                        },
                    ],
                });
                setLoading(false);
            } catch (error) {
                console.error('Error fetching product data', error);
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
            <h2>Current Stock Levels (pcs)</h2>
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

export default CurrentStockLevelsChart;
