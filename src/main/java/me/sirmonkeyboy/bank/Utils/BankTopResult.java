package me.sirmonkeyboy.bank.Utils;

public class BankTopResult {
    public final String[] names;
    public final double[] balances;
    public final boolean success;

    public BankTopResult(String[] names, double[] balances, boolean success) {
        this.names = names;
        this.balances = balances;
        this.success = success;
    }
}
