package org.openjdk.jmh;

import org.example.data.Account;
import org.example.exception.NotEnoughMoneyAccount;
import org.example.operation.Payment;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class SampleBenchmark {
    @Benchmark
    public void fibClassic(Blackhole bh) {
        Account account1 = new Account("1", 3000);
        Account account2 = new Account("2", 3000);
        int amount = 1000;
        try {
            Payment.transferMoney("1111", account1, account2, amount);
        } catch (NotEnoughMoneyAccount notEnoughMoneyAccount) {
            notEnoughMoneyAccount.printStackTrace();
        }
    }
}
