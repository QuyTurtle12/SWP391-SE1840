package com.swp391.jewelrysalesystem.services;

import java.util.List;

import com.swp391.jewelrysalesystem.models.Counter;

public interface ICounterService {
    boolean saveCounter(Counter counter);

    boolean removeCounter(int ID);

    List<Counter> getCountersList();

    Counter getCounter(int ID);

    boolean changeStatus(int ID);

    boolean isNotNullCounter(int ID);

    double calculateCounterSale(int counterID);


    int generateID();
}
