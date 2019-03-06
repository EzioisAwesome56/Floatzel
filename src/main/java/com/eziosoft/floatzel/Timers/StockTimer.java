package com.eziosoft.floatzel.Timers;

import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Util.Error;
import com.eziosoft.floatzel.Util.StockUtil;

import java.util.TimerTask;

public class StockTimer extends TimerTask {

    public StockTimer() {}

    @Override
    public void run(){
        // prepare to update all the stocks in the system
        System.out.println("Floatzel is preparing to update the stocks...");
        try {
            StockUtil.updateStock();
        } catch (DatabaseException e){
            Error.CatchOld(e);
        }
    }
}
