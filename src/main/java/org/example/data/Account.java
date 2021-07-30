package org.example.data;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private volatile Long moneyAccount;
    private final String name;
    private final Lock lock;

    public Account(String name, long startMoney) {
        this.name = name;
        this.lock = new ReentrantLock(false);
        this.moneyAccount = startMoney;
    }

    public void add(long money) {
        this.moneyAccount += money;
    }

    public boolean minus(long money) {
        if (this.moneyAccount - money >= 0) {
            this.moneyAccount -= money;
            return true;
        } else {
            return false;
        }
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public String getName() {
        return name;
    }

    public Long getMoneyAccount() {
        return moneyAccount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "moneyAccount=" + moneyAccount +
                ", name='" + name +
                '}';
    }
}
