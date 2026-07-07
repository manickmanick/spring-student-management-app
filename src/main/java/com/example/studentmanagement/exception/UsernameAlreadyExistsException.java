package com.example.studentmanagement.exception;

public class UsernameAlreadyExistsException extends RuntimeException{

    public UsernameAlreadyExistsException(String username){
        super("User already exists with this username : " + username);
    }
}
