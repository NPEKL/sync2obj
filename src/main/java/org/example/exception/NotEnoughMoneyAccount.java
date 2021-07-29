package org.example.exception;

import org.example.data.Account;

public class NotEnoughMoneyAccount extends Exception {

    public NotEnoughMoneyAccount(String uniqueID,Account from) {
        super(String.format("%s : haven't money on account: %s",uniqueID,from));
    }
}
