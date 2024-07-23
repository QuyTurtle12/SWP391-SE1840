package com.swp391.jewelrysalesystem.controllers;

import com.google.gson.JsonObject;
import com.swp391.jewelrysalesystem.config.VnpayConfig;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class VNPAYController {

    @PostMapping("/create_payment")
    public String createPayment(HttpServletRequest req, @RequestParam("amount") double amount,
            @RequestParam("totalPrice") String subtotal,
            @RequestParam("staffId") int staffId,
            @RequestParam("counterId") String counterID,
            @RequestParam("customerPhone") String customerPhone,
            @RequestParam("customerName") String customerName,
            @RequestParam("customerGender") String customerGender,
            @RequestParam("discountRate") String discountRate,
            @RequestParam("pointApplied") String pointsToApply,
            @RequestParam("discountName") String discountName) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amountInCents = (int) (amount * 100 * 25000);

        String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
        String vnp_IpAddr = VnpayConfig.getIpAddress(req);
        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;
        String vnp_Locale = "vn"; // Default to Vietnamese

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountInCents));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan hoa don JSS Store:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_ReturnUrl + "?staffId=" + staffId +
                "&totalPrice=" + subtotal +
                "&counterId=" + counterID +
                "&customerPhone=" + customerPhone +
                "&customerName=" + customerName +
                "&customerGender=" + customerGender +
                "&discountRate=" + discountRate +
                "&pointApplied=" + pointsToApply +
                "&discountName=" + discountName);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", vnp_Locale);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {

                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;

        JsonObject job = new JsonObject();
        job.addProperty("code", "00");
        job.addProperty("message", "success");
        job.addProperty("data", paymentUrl);

        return job.toString();
    }

    @GetMapping("/vnpay_return")
    public ResponseEntity<Map<String, String>> vnPayReturn(HttpServletRequest req) {
        String vnp_ResponseCode = req.getParameter("vnp_ResponseCode");
        Map<String, String> response = new HashMap<>();

        if ("24".equals(vnp_ResponseCode)) {
            response.put("code", "24");
            response.put("message", "Cancel Transaction.");
        } else if ("00".equals(vnp_ResponseCode)) {
            response.put("code", "00");
            response.put("message", "Transaction Success.");
        } else {
            response.put("code", "99");
            response.put("message", "Unknown Response Code.");
        }

        return ResponseEntity.ok(response);
    }
}