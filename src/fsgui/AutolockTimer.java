/**
 * ************************************************************
 */
/* Copyright (c) 2016 Andrej Borovski [andrej.borovski@mail.com]

This file is part of FastSecret.

FastSecret is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/> */
/**
 * ******************************************************************
 */
package fsgui;

import fscore.Storage;



/**
 *
 * @author Andrej Borovski
 */
public class AutolockTimer  {
    int timeout;
    Timer timer;
    boolean stop;
    int m;
    
    
    AutolockTimer() {
        this.stop = false;
        this.timer = new Timer();  
    }
    
    public void start(int minutes)
    {
        m = minutes*60000;
        timeout = m;
        if (!timer.isAlive())
            timer.start();
        else timer.reset();
    }
    
    class Timer extends Thread {
    
    Timer() 
    {
        super();
    }
      
    public void reset() {
        timeout = m;
    }
    public void stopTimer()
    {
        stop = true;
    }
    @Override
    public void run() {
            while(!stop) {
                try {
                    sleep(1000);
                    if (timeout > 0) {
                        timeout -= 1000;
                        if (timeout <= 0) {
                            CommonComponents.SELECT.tableSelectionListener.SaveUpdates();
                            Storage.INSTANCE.resetPassword();
                            CommonComponents.SELECT.tableSelectionListener.showEnterPasswordPanel();
                        }
                    }
                } catch (InterruptedException ex) {
                }
            }
        }
    }
    
    public void reset()
    {
        timer.reset();
    }
    
    public void stopTimer()
    {
        timer.stopTimer();
    }
    
    public static final AutolockTimer INSTANCE = new AutolockTimer();
}
