import React from 'react';
import SalesChart from './SalesChart';
import SalesOverTimeChart from './SalesOverTimeChart';
import TopSellingProductsTable from './TopSellingProductsTable';
import CurrentStockLevelsChart from './CurrentStockLevelsChart';
import ProductCategoriesSalesDistribution from './ProductCategoriesSalesDistribution';
import TopCustomersTable from './TopCustomersTable';
import RefundTrendsChart from './RefundTrendsChart';
import SalesByStaffChart from './SalesByStaffChart';
import './Dashboard.css';
import AdminMenu from '../Features/Admin/AdminMenu';

const Dashboard = () => {
    return (
        <div className="dashboard">
            <AdminMenu/>
            <h1>Dashboard</h1>
            <div className="top-section">
                <div className="left-column">
                    <div className="chart">
                        <TopSellingProductsTable />
                    </div>
                    <div className="chart">
                        <TopCustomersTable />
                    </div>
                </div>
                <div className="right-column">
                    <div className="line-charts">
                        <div className="chart">
                            <SalesOverTimeChart />
                        </div>
                        <div className="chart">
                            <RefundTrendsChart />
                        </div>
                    </div>
                </div>
            </div>
            <div className="bottom-section">
                <div className="chart">
                    <CurrentStockLevelsChart />
                </div>
                <div className="chart">
                    <ProductCategoriesSalesDistribution />
                </div>
                <div className="chart">
                    <SalesChart />
                </div>
                <div className="chart">
                    <SalesByStaffChart />
                </div>
            </div>
        </div>
    );
};

export default Dashboard;
