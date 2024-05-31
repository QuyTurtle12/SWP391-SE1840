package com.swp391.jewelrysalesystem.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.swp391.jewelrysalesystem.models.Counter;
import com.swp391.jewelrysalesystem.models.Promotion;
import com.swp391.jewelrysalesystem.services.ICounterService;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api")
public class CounterController {
    private ICounterService counterService;

    @Autowired
    public CounterController(ICounterService counterService){
        this.counterService = counterService;
    }

    @PostMapping("/counter/add")
    public Counter addCounter(@RequestParam int id) {
        Counter newCounter = new Counter();
        newCounter.setID(id);
        newCounter.setSale(0); //VND
        newCounter.setStatus(true);

        return counterService.saveCounter(newCounter);
    }

    @PostMapping("/counter/delete")
    public void deleteCounter(@RequestParam int id) {
        counterService.removeCounter(id);
    }

    @PutMapping("/counter/{id}/change-status")
    public Counter changeCounterStatus(@PathVariable int id) {
        return counterService.changeStatus(id);
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
}
