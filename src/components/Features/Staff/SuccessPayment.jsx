import React from 'react'
import StaffMenu from './StaffMenu'

function SuccessPayment() {
  return (
    <div className='min-h-screen'>
        <StaffMenu/>
        <div class="bg-green-100 border-l-4 border-green-500 text-green-700 p-4 rounded-lg">
    <p class="text-lg font-semibold">Order Status: Confirmed</p>
    <p>Your order has been successfully confirmed and is now being processed.</p>
</div>
    </div>
  )
}

export default SuccessPayment