package org.example;

import org.example.data.Account;
import org.example.exception.NotEnoughMoneyAccount;
import org.example.operation.Payment;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

    private final static Logger LOG;

    private final static int COUNT_ACCOUNTS = 10;
    private final static int START_MONEY_ON_ACCOUNT = 1000;
    private final static int COUNT_OPERATION = 100;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        Logger.getLogger("").setLevel(Level.FINE);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);
        LOG = Logger.getLogger(Application.class.getName());
    }

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService threadsPool = new ScheduledThreadPoolExecutor(5);

        List<Account> accountList = new LinkedList<>();
        for (int i = 0; i < COUNT_ACCOUNTS; i++) {
            accountList.add(new Account(Integer.toString(i), START_MONEY_ON_ACCOUNT));
        }

        for (int i = 0; i < COUNT_OPERATION; i++) {
            int finalI = i;
            threadsPool.execute(new Runnable() {
                @Override
                public void run() {
                    int indexFrom = getRandomIndex();
                    int indexTo = indexFrom;
                    int amount = (int) (1 + Math.random() * (START_MONEY_ON_ACCOUNT - 1));
                    while (indexFrom == indexTo) {
                        indexTo = getRandomIndex();
                    }
                    Account from = accountList.get(indexFrom);
                    Account to = accountList.get(indexTo);

                    try {
                        Payment.transferMoney(String.valueOf(finalI), from, to, amount);
                    } catch (NotEnoughMoneyAccount notEnoughMoneyAccount) {
                        LOG.warning(notEnoughMoneyAccount.getMessage());
                    }
                }

                private int getRandomIndex() {
                    return (int) (Math.random() * COUNT_ACCOUNTS);
                }
            });
        }

        threadsPool.shutdown();
    }
}
