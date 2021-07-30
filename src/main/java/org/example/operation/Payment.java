package org.example.operation;

import org.example.data.Account;
import org.example.exception.NotEnoughMoneyAccount;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Payment {

    private final static Logger LOG = Logger.getLogger(Payment.class.getName());

    private final static Lock lock;
    private final static Condition condition;

    static {
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public void aaa() {
    }

    public static void transferMoney(String uniqueID, Account from, Account to, int amount) throws NotEnoughMoneyAccount {
        LOG.fine(String.format("%s : Try : %d : %s -> %s ", uniqueID, amount, from.getName(), to.getName()));

        lockAccounts(uniqueID, from, to);
        LOG.fine(String.format("%s : Begin :", uniqueID));
        try {
            if (from.minus(amount)) {
                to.add(amount);
                LOG.fine(String.format("%s : end : %s => %d : %s => %d", uniqueID, from.getName(), from.getMoneyAccount(), to.getName(), to.getMoneyAccount()));
            } else {
                throw new NotEnoughMoneyAccount(uniqueID, from);
            }
        } finally {
            unlockAccounts(uniqueID, from, to);
        }
    }

    private static void lockAccounts(String uniqueID, Account account1, Account account2) {
        lock.lock();
        LOG.fine(String.format("%s : START LOCK ", uniqueID));
        try {
            while (account1.isLock() && account2.isLock())
                condition.await();

            LOG.fine(String.format("%s : TRY LOCK : %s && %s", uniqueID, account1.getName(), account2.getName()));
            account1.lock();
            account2.lock();
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOG.fine(String.format("%s : END LOCK ", uniqueID));
            lock.unlock();
        }
    }

    private static void unlockAccounts(String uniqueID, Account account1, Account account2) {
        lock.lock();

        LOG.fine(String.format("%s : TRY UNLOCK : %s && %s", uniqueID, account1.getName(), account2.getName()));
        account1.unlock();
        account2.unlock();

        condition.signalAll();
        lock.unlock();
    }
}
