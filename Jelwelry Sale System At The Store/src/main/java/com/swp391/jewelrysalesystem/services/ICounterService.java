package com.swp391.jewelrysalesystem.services;

import java.util.List;

import com.swp391.jewelrysalesystem.models.Counter;

public interface ICounterService {
    Counter saveCounter(Counter counter);

    void removeCounter(int ID);
    
    List<Counter> getCountersList();

    Counter getCounter(int ID);

    Counter changeStatus(int ID);
}
