package com.wly.stock.tradeplat.simulate;

import com.wly.common.Utils;
import com.wly.database.DBPool;
import com.wly.database.DBQuery;
import com.wly.stock.tradeplat.ITradeInterface;
import com.wly.stock.common.OrderInfo;
import com.wly.stock.common.TradeBook;
import com.wly.user.UserInfo;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wuly on 2017/2/13.
 */
public class TradeSimulateImpl implements ITradeInterface
{
    private ArrayList<OrderInfo> orderInfos = new ArrayList<>();

    private UserInfo userInfo;
    @Override
    public void SetUserInfo(UserInfo userInfo)
    {
        this.userInfo = userInfo;
    }

    @Override
    public void Login(String acct, String psw)
    {
    }

    @Override
    public float GetRmbAsset()
    {
       return 99999999f;
    }

    @Override
    public void DoOrder(OrderInfo orderInfo)
    {
        try {
            orderInfo.platOrderId = GetOrderId();

            final String UpdateFormat = "insert into trade_book(user_id, plat_id, plat_order_id, code, trade_flag, " +
                    "order_price, count, counter_fee, transfer_fee, stamp_tax, time, stat) " +
                    "values(%d, %d, '%s', '%s', %d, %.2f, %d, %.2f, %.2f, %.2f, '%s', %d)";
            orderInfo.id = DBPool.GetInstance().ExecuteNoQuerySqlSync(String.format(UpdateFormat, userInfo.id, userInfo.platId, orderInfo.platOrderId,
                    orderInfo.code,  orderInfo.tradeFlag, orderInfo.orderPrice, orderInfo.count, 0f, 0f, 0f, Utils.GetDate(), OrderInfo.OrderStat_Order_Succ), true);
//            orderInfo.platOrderId = Utils.GetId();
        }
        catch (Exception ex)
        {
            Utils.LogException(ex);
        }

        orderInfos.add(orderInfo);
    }

    @Override
    public void RevokeOrder(OrderInfo orderInfo)
    {
        final String UpdateFormat = "update trade_book SET stat = %d WHERE id = %d";
        DBPool.GetInstance().ExecuteNoQuerySqlSync(String.format(UpdateFormat, OrderInfo.OrderStat_Cancel_Succ, orderInfo.id));
        orderInfo.SetOrderStat(OrderInfo.OrderStat_Cancel_Succ);
    }

    @Override
    public int DoQueryOrderStatus(String platOrderId)
    {
        int ret = OrderInfo.OrderStat_None;
        try {
            final String QueryFormat = "select stat from trade_book where plat_order_id='%s'and user_id=%d";
            DBPool dbPool = DBPool.GetInstance();
            DBQuery dbQuery = dbPool.ExecuteQuerySync(String.format(QueryFormat, platOrderId, userInfo.id));
            ResultSet rs = dbQuery.resultSet;
            while (rs.next())
            {
                ret = rs.getInt("stat");
                break;
            }
            dbQuery.Close();
        }
        catch (Exception ex)
        {
            Utils.LogException(ex);
        }

        return ret;
    }

    @Override
    public List<TradeBook> GetTradeHis()
    {
        return null;
    }

    @Override
    public int GetStockAssetCount(String code)
    {
        return 20000;
    }

    @Override
    public float CacuTradeFee(int tradeFlag, String code, float price, int count)
    {
        return 0;
    }

    private String GetOrderId()
    {
        Random random = new Random();
        return String.format("%02d%s%04d", 0, Utils.GetDateTime(), random.nextInt(10000));
    }
}
