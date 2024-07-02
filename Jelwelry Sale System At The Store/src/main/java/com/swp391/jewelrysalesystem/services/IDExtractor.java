package com.swp391.jewelrysalesystem.services;

@FunctionalInterface
public interface IDExtractor<T> {
    int extractID(T document);
}