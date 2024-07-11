package com.swp391.jewelrysalesystem.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class GoldPriceService {

    @Value("${goldapi.url}")
    private String goldApiUrl;

    @Value("${goldapi.token}")
    private String goldApiToken;

    public double getCurrentGoldPrice() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-access-token", goldApiToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GoldPriceResponse> response = restTemplate.exchange(
                goldApiUrl,
                HttpMethod.GET,
                entity,
                GoldPriceResponse.class
        );

        GoldPriceResponse goldPriceResponse = response.getBody();
        return goldPriceResponse != null ? goldPriceResponse.getPrice() : 0.0;
    }

    public double getCurrent18kGoldPrice() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-access-token", goldApiToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GoldPriceResponse> response = restTemplate.exchange(
                goldApiUrl,
                HttpMethod.GET,
                entity,
                GoldPriceResponse.class
        );

        GoldPriceResponse goldPriceResponse = response.getBody();
        return goldPriceResponse != null ? goldPriceResponse.getPricePerGram18K() : 0.0;
    }
}

class GoldPriceResponse {
    private double price;
    private double price_gram_18k;

    // Getters and setters
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPricePerGram18K() {
        return price;
    }

    public void setPricePerGram18K(double price) {
        this.price = price;
    }
}


