import React from 'react';
import StaffMenu from './StaffMenu';

export default function StaffPolicy() {
    return (
  <>
        <StaffMenu/>
        <div className=" p-8 bg-gray-100 text-gray-800">
            
            <div className="bg-white p-6 rounded-lg shadow-md">
                <h1 className="text-3xl font-bold mb-6">Store Policies</h1>

                <div className="mb-8">
                    <h2 className="text-2xl font-semibold mb-4">1. Points Policy</h2>
                    <p className="mb-2">
                        The payment will be turned into points, with every $100 = 1 point.
                    </p>
                    <p>
                        Points can be used in various activities such as discounts.
                    </p>
                </div>

                <div className="mb-8">
                    <h2 className="text-2xl font-semibold mb-4">2. Discount Policy</h2>
                    <p className="mb-2">
                        Discount Money = Discount during sale promotion + Discount for exclusive customer + Discount money per point (optional)
                    </p>
                    <p className="mb-2">There are 2 types of discount:</p>
                    <ul className="list-disc list-inside ml-4 mb-4">
                        <li>Discount during sale promotion.</li>
                        <li>Discount for customers.</li>
                    </ul>
                    <p className="mb-2">
                        At least, 10 points = $10 discount.
                    </p>
                    <p>
                        limit, 100 points = $100.
                    </p>
                </div>

                <div className="mb-8">
                    <h2 className="text-2xl font-semibold mb-4">3. Refund Policy</h2>
                    <ul className="list-disc list-inside ml-4 mb-4">
                        <li>For women’s accessories + normal stone, Shop will only buy the real gold part.</li>
                        <li>Reclaim product with jewels which customers had bought with a 70% of sold price.</li>
                        <li>The gold price will be based on a real-time price chart (18k gold).</li>
                        <li>If product's purity is below 90%, we won't accept it.</li>
                    </ul>
                </div>

                <div className="mb-8">
                    <h2 className="text-2xl font-semibold mb-4">4. Warranty Policy</h2>
                    <p className="mb-2">
                        Every accessory can be returned to the shop within 7 days under these conditions:
                    </p>
                    <ul className="list-disc list-inside ml-4 mb-4">
                        <li>The accessory’s purity must be greater or equal than 99%.</li>
                    </ul>
                    <p className="mb-2">Return procedure:</p>
                    <ul className="list-disc list-inside ml-4">
                        <li>Step 1: Contact the staff to request returning product. (To facilitate the process, customer should provide warranty of the returning product)</li>
                        <li>Step 2: We will check the product’s warranty and product’s purity then respond to the customer.</li>
                        <li>Step 3: We will refund 100% customer’s money based on order’s total price or replace a new similar product.</li>
                    </ul>
                </div>
            </div>
        </div>
        </>
    );
}
