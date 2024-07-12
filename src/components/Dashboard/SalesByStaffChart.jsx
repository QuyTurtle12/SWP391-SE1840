import React, { useEffect, useState } from 'react';
import { Bar } from 'react-chartjs-2';
import axios from 'axios';

const SalesByStaffChart = () => {
    const [chartData, setChartData] = useState({
        labels: [],
        datasets: [
            {
                label: 'Total Sales',
                data: [],
                backgroundColor: 'rgba(75,192,192,0.6)',
            }
        ]
    });

    const [error, setError] = useState(null);

    useEffect(() => {
        axios.get('http://localhost:8080/api/v2/orders')
            .then(response => {
                const data = response.data;

                // Process data to get total sales by each staff member
                const salesByStaff = {};
                data.forEach(item => {
                    const staffName = item.staffName;
                    const totalPrice = item.totalPrice;
                    if (salesByStaff[staffName]) {
                        salesByStaff[staffName] += totalPrice;
                    } else {
                        salesByStaff[staffName] = totalPrice;
                    }
                });

                const labels = Object.keys(salesByStaff);
                const totalSales = Object.values(salesByStaff);

                setChartData({
                    labels: labels,
                    datasets: [
                        {
                            label: 'Total Sales (By $)',
                            data: totalSales,
                            backgroundColor: 'rgba(75,192,192,0.6)',
                        }
                    ]
                });
            })
            .catch(error => {
                console.error(`Error: ${error}`);
                setError('Failed to fetch data');
            });
    }, []);

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div>
            <h2>Sales by Staff:</h2>
            <Bar
                data={chartData}
                options={{
                    responsive: true,
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

export default SalesByStaffChart;
