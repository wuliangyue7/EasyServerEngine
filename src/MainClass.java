import com.wly.common.Utils;
import com.wly.database.DBPool;
import com.wly.stock.common.StockRuntimeInfo;
import com.wly.stock.infoplat.sina.StockInfoProviderSina;
import com.wly.stock.common.StockUtils;
//import com.wly.stock.strategy.PolicyStep;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/11/22.
 */
public class MainClass
{
    static public void main(String[] agrs) throws Exception
    {
        Init();
        Timer timer = new Timer();
        timer.schedule(new TaskQueryStock(), 0, 1000);

//        Thread thread = new Thread(new TaskUpdatePolicy());
//        thread.start();
    }

    static private void Init()
    {
        try
        {
            DBPool dbPool = DBPool.GetInstance();
            dbPool.Init("jdbc:mysql://db4free.net/wuly", "wuliangyue7", "wly19870120");

//            PolicyStep.Init();
        } catch (Exception ex)
        {
            Utils.LogException(ex);
        }
    }

    static public void PrccessStockInfo(ArrayList<StockRuntimeInfo> ArrayList)
    {
//        int i;
////        PolicyStep policyStep;
//        int len = ArrayList.size();
//        for (i = 0; i < ArrayList.size(); ++i)
//        {
//            if (PolicyStep.PolicyStepHashMap.containsKey(ArrayList.get(i).code))
//            {
//                policyStep = PolicyStep.PolicyStepHashMap.get(ArrayList.get(i).code);
//                policyStep.PrcessPrice(ArrayList.get(i));
//            }
//        }
    }
}

class TaskQueryStock extends TimerTask
{
    @Override
    public void run()
    {
        try
        {
            StockInfoProviderSina provider = new StockInfoProviderSina();
            MainClass.PrccessStockInfo(provider.GetStockInfoByCode(StockUtils.QueryCodeList));
        } catch (Exception ex)
        {
            Utils.LogException(ex);
        }
    }
}

class TaskUpdatePolicy implements Runnable
{
    @Override
    public void run()
    {
        try
        {
            Utils.Log("TaskUpdatePolicy");
            Thread.sleep(1000);
            final String UpdateFormat = "update policy_step SET price_last = %.2f WHERE id = %d";
            DBPool.GetInstance().ExecuteNoQuerySqlAsync(String.format(UpdateFormat, 45.49, 10005));
        } catch (Exception ex)
        {
            Utils.LogException(ex);
        }
        Utils.Log("TaskUpdatePolicy end");
    }
}
