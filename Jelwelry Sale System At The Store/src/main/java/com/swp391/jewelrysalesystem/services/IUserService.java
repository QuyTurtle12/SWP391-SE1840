package com.swp391.jewelrysalesystem.services;

import java.util.concurrent.ExecutionException;

import com.google.firebase.auth.FirebaseAuthException;
import com.swp391.jewelrysalesystem.models.User;

public interface IUserService {
    String login(String idToken) throws FirebaseAuthException, InterruptedException, ExecutionException;

    User getUserData(String userId) throws InterruptedException, ExecutionException;

    String getUserList();

    User saveUser(User user);

    User changeManagerStatus(int id);
}
