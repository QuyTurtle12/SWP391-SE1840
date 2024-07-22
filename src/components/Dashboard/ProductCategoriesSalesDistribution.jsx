import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Pie } from 'react-chartjs-2';
import { Chart as ChartJS, ArcElement, PieController, Tooltip, Legend } from 'chart.js';

ChartJS.register(ArcElement, PieController, Tooltip, Legend);

const ProductCategoriesSalesDistribution = () => {
    const [chartData, setChartData] = useState({
        labels: [],
        datasets: [],
    });
    const [loading, setLoading] = useState(true);
    const [token, setToken] = useState('');

    useEffect(() => {
        const fetchChartData = async () => {
            try {
                const tokenFromStorage = localStorage.getItem('token');
                setToken(tokenFromStorage);

                const response = await axios.get('https://jewelrysalesystem-backend.onrender.com/api/v2/orders/categories/sales', {
                    headers: {
                        Authorization: `Bearer ${tokenFromStorage}`,
                    },
                });

                const data = response.data;
                const categories = Object.keys(data);
                const sales = Object.values(data);

                setChartData({
                    labels: categories,
                    datasets: [
                        {
                            label: 'Sales Distribution',
                            data: sales,
                            backgroundColor: [
                                'rgba(255, 99, 132, 0.6)',
                                'rgba(54, 162, 235, 0.6)',
                                'rgba(255, 206, 86, 0.6)',
                            ],
                            borderColor: [
                                'rgba(255, 99, 132, 1)',
                                'rgba(54, 162, 235, 1)',
                                'rgba(255, 206, 86, 1)',
                            ],
                            borderWidth: 1,
                        },
                    ],
                });

                setLoading(false);
            } catch (error) {
                console.error('Error fetching sales data by category', error);
                setLoading(false);
            }
        };

        fetchChartData();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h2>Product Categories Sales Distribution</h2>
            <Pie
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

export default ProductCategoriesSalesDistribution;