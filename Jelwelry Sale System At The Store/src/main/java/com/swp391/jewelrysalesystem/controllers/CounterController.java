package com.swp391.jewelrysalesystem.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.swp391.jewelrysalesystem.models.Counter;
import com.swp391.jewelrysalesystem.models.Order;
import com.swp391.jewelrysalesystem.services.ICounterService;
import com.swp391.jewelrysalesystem.services.IOrderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class CounterController {
    private ICounterService counterService;
    private IOrderService orderService;

    @Autowired
    public CounterController(ICounterService counterService, IOrderService orderService) {
        this.counterService = counterService;
        this.orderService = orderService;
    }

    @PostMapping("/v2/counters")
    public ResponseEntity<String> addCounterV2() {
        int id = counterService.generateID();

        Counter newCounter = new Counter();
        newCounter.setID(id);
        newCounter.setSale(0); // VND
        newCounter.setStatus(true);

        return counterService.saveCounter(newCounter)
                ? ResponseEntity.status(HttpStatus.SC_CREATED).body("Create Successfully")
                : ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error creating counter");
    }

    @DeleteMapping("/v2/counters")
    public ResponseEntity<String> deleteCounterV2(@RequestParam int id) {
        if (!counterService.isNotNullCounter(id)) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Counter ID " + id + " is not existing");
        }

        return counterService.removeCounter(id) ? ResponseEntity.ok().body("Deleting Successfully")
                : ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error deleting counter");

    }

    @PutMapping("/v2/counters/{id}/status")
    public ResponseEntity<String> changeCounterStatusV2(@PathVariable int id) {
        if (!counterService.isNotNullCounter(id)) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Counter ID " + id + " is not existing");
        }

        return counterService.changeStatus(id) ? ResponseEntity.ok().body("Changing status Successfully")
                : ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error chaging status");
    }

    @GetMapping("/v2/counters")
    public ResponseEntity<List<Map<String, Object>>> getCounterListV2(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Counter> counterList = counterService.getCountersList();
            if (counterList == null && counterList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }

            List<Order> orders = orderService.getOrderListInSpecificTime(startDate, endDate);
            
            for (Order order : orders) {
                for (Counter counter : counterList) {
                    if (order.getCounterID() == counter.getID()) {
                        double currentSale = counter.getSale();
                        counter.setSale(currentSale + order.getTotalPrice());
                        break;
                    }
                }
            }

            List<Map<String, Object>> response = new ArrayList<>();

            for (Counter counter : counterList) {
                Map<String, Object> map = counter.toMap();
                response.add(map);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    

    @GetMapping("/v2/counters/no-sale")
    public ResponseEntity<List<Map<String, Object>>> getCounterListWithNoSaleV2() {
        try {
            List<Counter> counterList = counterService.getCountersList();
            if (counterList == null && counterList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }

            List<Map<String, Object>> response = new ArrayList<>();

            for (Counter counter : counterList) {
                Map<String, Object> map = counter.toMap();
                response.add(map);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/v2/counters/counter")
    public ResponseEntity<Counter> getCounterV2(@RequestParam int id) {
        try {
            Counter counter = counterService.getCounter(id);
            if (counter == null) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(counter);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    
    @GetMapping("/v2/counters/{id}/calculate-sale")
    public ResponseEntity<Double> calculateCounterSaleV2(@PathVariable int id) {
        if (!counterService.isNotNullCounter(id)) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
        }

        try {
            double totalSale = counterService.calculateCounterSale(id);
            return ResponseEntity.ok(totalSale);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/v2/counters/sales")
    public ResponseEntity<List<Map<String, Object>>> getTotalSalesByCounter() {
        try {
            List<Counter> counters = counterService.getCountersList();
            List<Map<String, Object>> responseList = new ArrayList<>();

            for (Counter counter : counters) {
                Map<String, Object> counterMap = new HashMap<>();
                double totalSales = counterService.calculateCounterSale(counter.getID());
                counterMap.put("counterID", counter.getID());
                counterMap.put("totalSales", totalSales);
                responseList.add(counterMap);
            }

            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
