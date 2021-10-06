package com.kali;

import java.util.Objects;
import java.util.UUID;

/**
 * 每位彩民的个人信息
 */
public class Consumer {
    /*UUID暂时没有使用*/
    private UUID cid = UUID.randomUUID();
    private static int tickets;
    private String name;
    private double bonus;
    private double account;
    private int[][] lotteries;

    public Consumer(String name, double account,double bonus) {
        this.name = name;
        this.bonus = bonus;
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }

    public int[][] getLotteries() {
        return lotteries;
    }

    public void setLotteries(int[][] lotteries) {
        this.lotteries = lotteries;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public UUID getCid() {
        return cid;
    }

    public void setCid(UUID cid) {
        this.cid = cid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Consumer)) return false;
        Consumer consumer = (Consumer) o;
        return cid.equals(consumer.cid) &&
                name.equals(consumer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cid, name);
    }
}
