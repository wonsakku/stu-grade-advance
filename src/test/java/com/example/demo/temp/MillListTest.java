package com.example.demo.temp;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

public class MillListTest {


    @Test
    void name(){
        long before = Runtime.getRuntime().freeMemory() / 1024 / 1024;
        System.out.println("before -> " + before);
        List<User> users = new LinkedList<>();
        for(int i = 1 ; i <= 1_500_000 ; i++){
            users.add(new User(i, 0));
        }
        long after = Runtime.getRuntime().freeMemory() / 1024 / 1024;
        System.out.println("after -> " + after);

    }


    public class User {
        int id;
        int grade;

        public User(int id, int grade) {
            this.id = id;
            this.grade = grade;
        }
    }
}
