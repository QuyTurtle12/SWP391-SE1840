package com.swp391.jewelrysalesystem.services;

import java.util.concurrent.ExecutionException;
import java.util.List;

import com.google.firebase.auth.FirebaseAuthException;
import com.swp391.jewelrysalesystem.models.User;

public interface IUserService {
    String login(String idToken) throws FirebaseAuthException, InterruptedException, ExecutionException;

    User getUserData(String userId) throws InterruptedException, ExecutionException;

    String getUserList();

    User getUserByUserID(int userID) throws InterruptedException, ExecutionException;

    List<User> getUserByUserRole(String role) throws InterruptedException, ExecutionException;

    User saveUser(User user);

    User changeUserStatus(int id);

    List<User> searchUser(String input, String filter, List<User> userList);
}
