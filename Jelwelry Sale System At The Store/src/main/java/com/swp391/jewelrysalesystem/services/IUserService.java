package com.swp391.jewelrysalesystem.services;

import java.util.concurrent.ExecutionException;

import java.util.List;

import com.swp391.jewelrysalesystem.models.User;

public interface IUserService {

    User getUserByField(String value, String field, String collection) throws InterruptedException, ExecutionException;

    User getUserByField(int value, String field, String collection) throws InterruptedException, ExecutionException;

    List<User> getUserListByField(String value, String field, String collection)
            throws InterruptedException, ExecutionException;

    List<User> getUserListByField(int value, String field, String collection)
            throws InterruptedException, ExecutionException;

    boolean saveUser(User user);

    boolean changeUserStatus(int id);

    List<User> searchUser(String input, String filter, List<User> userList);

    boolean isNotNullUser(int ID);

    boolean isNotExistedPhoneNum(String contactInfo);

    boolean isMyPhoneNum(int ID, String contactInfo);

    boolean registerUser(User user);

    boolean isNotExistedEmail(String email);

    boolean isMyEmail(int ID, String email);

    String isGeneralValidated(String fullName, String gender, String contactInfo, int counterID);

    int generateID();

    User getUserByEmail(String email) throws InterruptedException, ExecutionException;

    User getUserByEmailAndPassword(String email, String rawPassword)
            throws InterruptedException, ExecutionException;
}
