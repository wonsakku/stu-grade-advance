package com.example.demo;

import java.util.List;

class Targets {
    private final List<User> users;

    public Targets(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return this.users;
    }
}
