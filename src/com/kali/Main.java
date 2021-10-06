package com.kali;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Terminate tm = new Terminate();
        Consumer con = new Consumer("lisa",3,0);
        Scanner scan = new Scanner(System.in);

        if(con.getAccount() >= 0){
            tm.process(tm,con,scan);
        }
    }
}