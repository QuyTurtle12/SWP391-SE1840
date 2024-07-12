import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, LineElement, PointElement, Title, Tooltip, Legend } from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, LineElement, PointElement, Title, Tooltip, Legend);

const SalesOverTimeChart = () => {
    const [chartData, setChartData] = useState({
        labels: [],
        datasets: [],
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/v2/orders');
                const data = response.data;

                // Process data to get sales over time
                const salesData = data.map(order => ({
                    date: new Date(order.date.seconds * 1000),
                    totalPrice: order.totalPrice,
                }));

                // Group by date (daily)
                const salesByDate = salesData.reduce((acc, sale) => {
                    const date = sale.date.toISOString().split('T')[0]; // Get date part only
                    if (!acc[date]) {
                        acc[date] = 0;
                    }
                    acc[date] += sale.totalPrice;
                    return acc;
                }, {});

                const dates = Object.keys(salesByDate).sort();
                const totalSales = dates.map(date => salesByDate[date]);

                setChartData({
                    labels: dates,
                    datasets: [
                        {
                            label: 'Total Sales',
                            data: totalSales,
                            backgroundColor: 'rgba(75, 192, 192, 0.6)',
                            borderColor: 'rgba(75, 192, 192, 1)',
                            borderWidth: 1,
                            fill: false, // Ensure it's a line chart
                            tension: 0.1, // Optional: add some tension to the line
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
            <h2>Sales Over Time</h2>
            <Line
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

export default SalesOverTimeChart;
