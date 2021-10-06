package com.kali;

import java.util.*;

/**
 * 买双色球是否中奖，中奖后判断是几等奖：
 *     1.双色球投注区分为红色球号码区和蓝色球号码区
 *     2.红色球号码区由1-33个号码组成，蓝色球号码区由1-16个号码组成
 *     3.投注时选择6个红色球号码和1个蓝色球号码组成一注进行单式投注，每注两元
 *     奖项：
 *     一等奖：投注当期号码全部相同，顺序不限，即中奖
 *     二等奖：投注号码与当期号码中6个红色球号码相同，蓝色不同
 *     三等奖：任意5个红色球与1个蓝色球号码相同
 *     四等奖：任意5个球相同或者任意4个红色球和一个蓝色球号码相同
 *     五等奖：任意4个红色球相同或者任意3个红色球与一个蓝色球相同
 *     六等奖：投注号码与当前开奖中一个蓝色号码相同
 *
 *     显示中奖结果的同时显示您一共下注人民币?元，累计中奖人民币?元
 *
 * TODO 没有实现对个人历史票号的存储，项目中采用的数据存储结构最大为二维数组，集合类结构没有使用
 *
 */
public class Terminate {
    private static boolean flag;
    private static final int SEVEN = 7;
    private static final int[] result = new int[7];
    //private static final int[][] storage = new int[100][SEVEN];
    private static final String TIPS = "输入1购买彩票\t输入2查看\t输入3回到主页\t输入4开奖\t输入5退出";

    public Terminate(){
        init(result);
    }

    //控制中心
    public void process(Terminate tm,Consumer con,Scanner scan){
        tm.showHomepage(con);
        tm.showConsumerMessage(con);

        while (true){
            if(con.getAccount() >= 0){
                System.out.println("请根据面板选项选择对应的操作:提示 1 购买彩票 ");
                int i;
                try {
                    i = Integer.parseInt(scan.next());
                    if(i < 0){
                        i = 3;
                    }
                }catch (Exception e){
                    i = 3;
                }

                switch (i){
                    case 0 :
                        //重置当期的彩票结果
                        tm.init(result);
                        System.out.println("重置成功\t重置成功\t重置成功");
                        break;
                    case 1 :
                        //需要下几注，购买3轮，每轮7次
                        System.out.println("请输入购买数量彩票的数量");
                        int tickets;
                        try {
                            if((tickets = Integer.parseInt(scan.next())) <= 0){
                                System.out.println("请正确输入购买数量彩票的数量");
                                tickets = Integer.parseInt(scan.next());
                                //累计个人购买过的票数
                                con.setTickets(con.getTickets() + tickets);

                            }else if((tickets*2) > con.getAccount()){
                                System.out.println(tickets + "张票需要" + (tickets*2)
                                        + "￥\t您的余额为"  + con.getAccount() + "￥");
                                tm.recharge(con,scan);
                            }else{
                                tm.orderTicket(con,scan,tickets);
                                System.out.println("输入2查看购买的彩票号\t输入4查看中奖情况");
                            }

                        }catch (Exception e){
                            System.out.println("请正确输入指令\t 输入1购买彩票 ");
                            System.out.println("                                  ");
                            System.out.println("                                  ");
                            tm.showHomepage(con);
                        }

                        break;
                    case 2 :
                        tm.showLotteries(con);
                        System.out.println(TIPS);
                        break;
                    case 3 :
                        tm.showHomepage(con);
                        System.out.println("请根据面板选项选择对应的操作指令:");
                        break;
                    case 4 :
                        System.out.println("————————————————开奖——————————————");
                        System.out.println("                                  ");
                        tm.runALottery(con);
                        System.out.println("输入1购买彩票\t输入3回到主页\t输入5退出");
                        break;
                    case 5 :
                        tm.exit(tm,scan,con);
                        if(flag){
                            return;
                        }
                        System.out.println(TIPS);
                    case 6 :
                        tm.recharge(con,scan);
                    default: break;
                }
            }else{
                tm.recharge(con,scan);
            }
        }

    }

    //充值
    public void recharge(Consumer con,Scanner scan){
        System.out.println("请充值!\t充值额度: ");
        double v;
        try {
            v = Double.parseDouble(scan.next());
            if(v > 0){
                con.setAccount(v + con.getAccount());
                System.out.println("充值成功!");
                showConsumerMessage(con);
                if(con.getAccount() >= 2){
                    //showConsumerMessage(con);
                    showHomepage(con);
                }else{
                    System.out.println("温馨提示:您的余额少于2元 不足购买彩票......");
                    recharge(con,scan);
                }
            }else{
                System.out.println("请正确输入充值额度......");
                recharge(con,scan);
            }
        }catch (Exception e){
            System.out.println("请正确输入充值额度......");
            recharge(con,scan);
        }

    }

    //打印客户信息
    public void showConsumerMessage(Consumer con){
        System.out.println();
        System.out.println("亲爱的 " + con.getName()
                + " 您好,您当前卡上余额为 "
                + con.getAccount() + "￥\t"
                + "当前中奖金额" + con.getBonus());
        System.out.println();
    }

    //开奖
    public void runALottery(Consumer con){
        boolean findRed;
        int[][] lotteries = con.getLotteries();

        if(lotteries != null){
            for(int i = 0;i < lotteries.length;i++){
                System.out.println("       第" + (i+1) + "组号码比对情况      ");
                System.out.println("                                          ");
                //red blue分别代表红蓝球相同数量
                int red = 0;
                int blue = 0;
                //将相同数字的下标拼接
                String cat = "";
                //用拷贝的数组进行比对，不影响源数组
                int[] usr = Arrays.copyOfRange(result,0,result.length);
                for (int j = 0; j < lotteries[i].length - 1; j++) {
                    /*匹配红球号码*/
                    findRed = false;
                    for (int k = 0; k < usr.length - 1; k++) {
                        if(lotteries[i][j] == usr[k]){
                            if(!findRed){
                                red ++;
                                usr[k] = -1;
                                cat += k + " ";
                                findRed = true;
                            }
                        }
                    }
                }

                /*匹配蓝球号码*/
                if(lotteries[i][6] == result[6]){
                    cat += 6;
                    blue = 1;
                }
                //打印号码相同的位置
                System.out.println("相同位置 " + cat);
                showSameNumber(cat,lotteries[i]);
                //根据红篮球的数量显示中奖情况
                matchResult(red,blue,con);
                System.out.println("——————————————————————————————————");
            }
        }else{
            //没有彩票的前提下直接开奖显示当期的彩票结果
            showTodayResult();
        }

        showConsumerMessage(con);
    }

    //把每组中中奖的数字用[]标记
    public void showSameNumber(String cat,int[] lottery){
        if(cat.equals("")){
            return;
        }

        showTodayResult();
        String[] s = cat.split(" ");
        int[] arr = new int[s.length];
        for(int i = 0;i < s.length;i++){
            arr[i] = Integer.parseInt(s[i]);
        }

        boolean find;
        System.out.print("\t[");
        for(int i = 0;i < lottery.length;i++){
            find = false;
            int k = -1;
            for (int value : arr) {
                if (i == value) {
                    if (!find) {
                        k = i;
                        find = true;
                    }
                }
            }
            if(i == k){
                System.out.print(" [" + lottery[k] + "] ");
            }else{
                System.out.print(" " + lottery[i] + " ");
            }
        }
        System.out.println("]");
    }


    /*
    根据红蓝的数量匹配奖项
    找出对应的中奖号码并且上边显示当期彩票中奖号码和自己所买号码的比对信息
    相同的话记录并且输出加[]
    彩民消费与中奖情况的输出
    */
    //根据开奖结果匹配奖项
    public void matchResult(int red,int blue,Consumer con){
        if(red == 7){
            System.out.println("一等奖 金额 5,000,000￥");
            con.setBonus(con.getBonus() + 5000000);
            con.setAccount((con.getAccount() - 2) + con.getBonus());
        }else if(red == 6 && blue == 0){
            System.out.println("二等奖 金额 500，000￥");
            con.setBonus(con.getBonus() + 500000);
            con.setAccount((con.getAccount() - 2) + con.getBonus());
        }else if(red == 5 && blue == 1){
            System.out.println("三等奖 金额 50，000￥");
            con.setBonus(con.getBonus() + 50000);
            con.setAccount((con.getAccount() - 2) + con.getBonus());
        }else if(red == 4 && blue == 1){
            System.out.println("四等奖 金额 5000￥");
            con.setBonus(con.getBonus() + 5000);
            con.setAccount((con.getAccount() - 2) + con.getBonus());
        }else if(red == 3 && blue == 1){
            System.out.println("五等奖 金额 500￥");
            con.setBonus(con.getBonus() + 500);
            con.setAccount((con.getAccount() - 2) + con.getBonus());
        }else if(red == 0 && blue == 1){
            System.out.println("六等奖 金额 50￥");
            con.setBonus(con.getBonus() + 50);
            con.setAccount((con.getAccount() - 2) + con.getBonus());
        }else{
            System.out.println("\t红色球相同 " + red + " 蓝色球相同 " + blue);
            System.out.println("                                  ");
            System.out.println("         谢谢惠顾    祝您好运     ");
            System.out.println("——————————————————————————————————");
            System.out.println("                                  ");
            con.setAccount((con.getAccount() - 2) + con.getBonus());
        }
    }

    //打印当期注码
    public void showTodayResult(){
        System.out.println("——————————本期双色球结果——————————");
        System.out.println("\t" + Arrays.toString(result));
    }

    //打印彩民购买的彩票号码  蓝色号码用 * 标记
    public void showLotteries(Consumer con){
        int[][] lotteries = con.getLotteries();

        if(lotteries != null){
            for(int i = 0;i < lotteries.length;i++){
                System.out.println("第" + (i+1) + "组彩票号码");
                for (int j = 0; j < lotteries[i].length; j++) {
                    if(j == lotteries[i].length - 1){
                        System.out.print(lotteries[i][j] + "*\t");
                    }else{
                        System.out.print(lotteries[i][j] + "\t");
                    }
                }
                System.out.println();
            }
        }else{
            System.out.println("请先购买彩票");
            showHomepage(con);
        }

        /*使用增强for循环同样可以遍历二维数组*/
        /*if(lotteries != null){
            int i = 1;
            for(int[] l : lotteries){
                System.out.println("第" + (i++) + "组彩票号码");
                System.out.println(Arrays.toString(l));
            }
        }else{
            System.out.println("请先购买彩票");
            showHomepage();
        }*/

    }

    //退出系统
    public void exit(Terminate tm,Scanner scan,Consumer con){
        System.out.println("   您确定要退出吗？  Y/N             ");
        String signal = scan.next().toUpperCase();
        if("N".equals(signal)){
            flag = false;
            System.out.println("        请输入    Y/N              ");
            tm.showHomepage(con);
        }else if("Y".equals(signal)) {
            flag = true;
            System.out.println("————————感谢您购买双色球彩票————————");
        }else {
            System.out.println("请正确输入");
            exit(tm, scan,con);
        }
    }
    
    /*先买几张票然后处理，中途发生异常，保存原有状态*/
    public void orderTicket(Consumer con,Scanner scan,int rounds){
        int[][] h = new int[rounds][SEVEN];

        for(int i = 0;i < rounds;i++){
            System.out.println("现在购买第" + (i+1) + "张彩票");
            int[] ticket = orderOneTicket(scan);
            System.arraycopy(ticket, 0, h[i], 0, ticket.length);
        }

        con.setLotteries(h);
    }

    //购买一张彩票
    public int[] orderOneTicket(Scanner scan){

        int[] singleTicket = new int[SEVEN];
        for (int i = 0; i < SEVEN; i++) {
            int num;

            if(i != 6){
                System.out.println("请选择第" + (i+1) + "个红球号码[1-33]");
                num = Integer.parseInt(scan.next());
                if(num < 1 || num > 33){
                    System.out.println("请重新输入号码");
                    i--;
                }else{
                    singleTicket[i] = num;
                }
            }else{
                System.out.println("请选择第" + (i+1) + "个蓝球号码[1-16]");
                num = Integer.parseInt(scan.next());
                if(num < 1 || num > 16){
                    System.out.println("请重新输入号码");
                    i--;
                }else{
                    singleTicket[i] = num;
                }
            }
        }

        //System.out.println(Arrays.toString(singleTicket));
        return singleTicket;
    }

    //主页
    public void showHomepage(Consumer con){
        System.out.println("————————欢迎购买双色球彩票—————————");
        System.out.println("    1   购买彩票                  ");
        System.out.println("    2   查看                      ");
        System.out.println("    3   主页                      ");
        System.out.println("    4   开奖                      ");
        System.out.println("    5   退出                      ");
        if(con.getAccount() >= 2){
            System.out.println("    6   充值                      ");
        }
        System.out.println("    提示:请输入对应序号进行操作    ");
        System.out.println("——————————————————————————————————");
    }

    /*随机产生本期中奖号码*/
    public void init(int[] result){

        Random ran = new Random();
        //生成红色球号码
        for(int i = 0;i < result.length - 1;i++){
            int seed = ran.nextInt(33) + 1;
            result[i] = seed;
        }

        //生成蓝色球号码
        result[result.length - 1] = ran.nextInt(16) + 1;
        System.out.println("Beta message >> ");
        System.out.println(Arrays.toString(result));
    }

}
