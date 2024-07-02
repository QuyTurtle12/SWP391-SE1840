package com.swp391.jewelrysalesystem.services;

import java.util.List;
import java.util.concurrent.ExecutionException;


public interface IGenericService<T> {
    T getByField(String value, String field, String collection, Class<T> clazz) throws InterruptedException, ExecutionException;

    T getByField(int value, String field, String collection, Class<T> clazz) throws InterruptedException, ExecutionException;

    List<T> getListByField(String value, String field, String collection, Class<T> clazz) throws InterruptedException, ExecutionException;

    List<T> getListByField(int value, String field, String collection, Class<T> clazz) throws InterruptedException, ExecutionException;

    List<T> getList(String collection, Class<T> clazz) throws InterruptedException, ExecutionException;

    boolean saveObject(T object, String collection, int documentID);

    boolean deleteObject(int documentID, String collection);

    boolean changeStatus(int ID, String collection, Class<T> clazz);

    int generateID(String collectionName, Class<T> documentClass, IDExtractor<T> idExtractor);
}
