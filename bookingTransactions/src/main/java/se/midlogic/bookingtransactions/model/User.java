package se.midlogic.bookingtransactions.model;

import se.midlogic.bookingtransactions.controller.Transaction;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private double creditLimit;
    private List<Transaction> transactions;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public User(String firstName, String lastName, String email, double creditLimit) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.creditLimit = creditLimit;
        this.transactions = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", creditLimit=" + creditLimit +
                '}';
    }
}