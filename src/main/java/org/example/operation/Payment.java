package org.example.operation;

import org.example.data.Account;
import org.example.exception.NotEnoughMoneyAccount;

import java.util.logging.Logger;

public class Payment {

    private final static Logger LOG = Logger.getLogger(Payment.class.getName());

    private final static Object lockAccounts = new Object();
    private final static Object unlocAccounts = new Object();

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

    private synchronized static void lockAccounts(String uniqueID, Account... accounts) {
        LOG.fine(String.format("%s : LOCK ", uniqueID));
        for (Account account : accounts) {
            LOG.fine(String.format("%s : TRY LOCK : %s", uniqueID, account.getName()));
            account.lock();
        }
    }

    private static void unlockAccounts(String uniqueID, Account... accounts) {
        LOG.fine(String.format("%s : UNLOCK ", uniqueID));
        for (Account account : accounts) {
            LOG.fine(String.format("%s : TRY UNLOCK : %s", uniqueID, account.getName()));
            account.unlock();
        }
    }
}
