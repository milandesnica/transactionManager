package se.midlogic.bookingtransactions.controller;

public class Transaction {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final double amount;
    private final String transactionId;

    public Transaction(String firstName, String lastName, String email, double amount, String transactionId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.amount = amount;
        this.transactionId = transactionId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", amount=" + amount +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
