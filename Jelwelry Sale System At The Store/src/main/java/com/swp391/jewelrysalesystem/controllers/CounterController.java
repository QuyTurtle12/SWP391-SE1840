package com.swp391.jewelrysalesystem.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.swp391.jewelrysalesystem.models.Counter;
import com.swp391.jewelrysalesystem.services.ICounterService;
import com.swp391.jewelrysalesystem.services.IOrderService;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CounterController(ICounterService counterService)  {
        this.counterService = counterService;
    }

    @PostMapping("/v2/counters")
    public ResponseEntity<String> addCounterV2() {
        int id = counterService.generateID();

        Counter newCounter = new Counter();
        newCounter.setID(id);
        newCounter.setSale(0); //  VND
        newCounter.setStatus(true);

        return counterService.saveCounter(newCounter)
                ? ResponseEntity.status(HttpStatus.SC_CREATED).body("Create Successfully")
                : ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error creating counter");
    }

    @DeleteMapping("/v2/counters")
    public ResponseEntity<String> deleteCounterV2(@RequestParam int id) {
        if (!counterService.isNotNullCounter(id)) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Counter ID " + id +  " is not existing");
        }

        return counterService.removeCounter(id) ? ResponseEntity.ok().body("Deleting Successfully")
                : ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error deleting counter");

    }

    @PutMapping("/v2/counters/{id}/status")
    public ResponseEntity<String> changeCounterStatusV2(@PathVariable int id) {
        if (!counterService.isNotNullCounter(id)) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Counter ID " + id +  " is not existing");
        }

        return counterService.changeStatus(id) ? ResponseEntity.ok().body("Changing status Successfully")
                : ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error chaging status");
    }

    @GetMapping("/v2/counters")
    public ResponseEntity<List<Counter>> getCounterListV2() {
        try {
            List<Counter> counterList = counterService.getCountersList();
            if (counterList == null && counterList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);

            }

            return ResponseEntity.ok(counterList);
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

    //Old endpoints version below here
    @PostMapping("/counter/add")
    public Counter addCounter(@RequestParam int id) {
        Counter newCounter = new Counter();
        newCounter.setID(id);
        newCounter.setSale(0); //VND
        newCounter.setStatus(true);

        counterService.saveCounter(newCounter);
        return null;
    }

    @PostMapping("/counter/delete")
    public void deleteCounter(@RequestParam int id) {
        counterService.removeCounter(id);
    }

    @PutMapping("/counter/{id}/change-status")
    public Counter changeCounterStatus(@PathVariable int id) {
        counterService.changeStatus(id);
        return null;
    }

    @GetMapping("/counter/list")
    public ResponseEntity<List<Counter>> getCounterList() {
        try {
            List<Counter> counterList = counterService.getCountersList();
            if (counterList != null && !counterList.isEmpty()) {
                return ResponseEntity.ok(counterList);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/counter/get")
    public ResponseEntity<Counter> getCounter(@RequestParam int id) {
        try {
            Counter counter = counterService.getCounter(id);
            if (counter != null) {
                return ResponseEntity.ok(counter);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
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
}
