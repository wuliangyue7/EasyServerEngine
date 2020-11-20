package com.wly.stock.common;

/**
 * Created by Administrator on 2017/1/21.
 */
public class StockConst
{
    static  public final int TradeBuy = 0;
    static public  final int TradeSell = 1;

    static public final String PrefixSH = "sh";
    static public final String PrefixSZ = "sz";

    static public  final String[]  DebtCodeSH = new String[]{"11"};
    static public  final String[]  SpecialCodeSH = new String[]{"000001"};
    static public  final String[]  DebtCodeSZ = new String[]{"12"};
    static public  final String[]  SpecialCodeSZ = new String[]{""};

    static public  final String RmbCode = "000000";
    static public  final String RmbName = "人民币";

    static public final int PlatSimulate = 0;
    static public final int PlatEastmoney = 1;
}
