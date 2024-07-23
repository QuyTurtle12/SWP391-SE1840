import React, { useEffect, useState } from 'react';
import { Line } from 'react-chartjs-2';
import axios from 'axios';
import moment from 'moment';

const RefundTrendsChart = () => {
    const [chartData, setChartData] = useState({
        labels: [],
        datasets: [
            {
                label: 'Refund Amount ($)',
                data: [],
                borderColor: 'rgba(75,192,192,1)',
                backgroundColor: 'rgba(75,192,192,0.2)',
            }
        ]
    });

    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchChartData = async () => {
            try {
                const token = localStorage.getItem('token');
                const response = await axios.get('https://jewelrysalesystem-backend.onrender.com/api/refunds', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                const data = response.data;

                // Process data to get refund amounts over time
                const refundAmounts = {};
                data.forEach(item => {
                    const date = moment.unix(item.date.seconds).format('YYYY-MM-DD');
                    if (refundAmounts[date]) {
                        refundAmounts[date] += item.totalPrice;
                    } else {
                        refundAmounts[date] = item.totalPrice;
                    }
                });

                const labels = Object.keys(refundAmounts).sort();
                const totalPrices = labels.map(date => refundAmounts[date]);

                setChartData({
                    labels: labels,
                    datasets: [
                        {
                            label: 'Refund Amount',
                            data: totalPrices,
                            borderColor: 'rgba(75,192,192,1)',
                            backgroundColor: 'rgba(75,192,192,0.2)',
                        }
                    ]
                });
            } catch (error) {
                console.error(`Error: ${error}`);
                setError('Failed to fetch data');
            }
        };

        fetchChartData();
    }, []);

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div>
            <h2>Refund Trends Over Time:</h2>

            <Line
                data={chartData}
                options={{
                    responsive: true,
                    title: { text: 'Refund Trends Over Time', display: true },
                    scales: {
                        y: {
                            beginAtZero: true,
                        }
                    }
                }}
            />
        </div>
    );
};

export default RefundTrendsChart;
