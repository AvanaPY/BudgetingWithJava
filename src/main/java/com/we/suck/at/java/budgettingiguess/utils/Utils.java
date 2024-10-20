package com.we.suck.at.java.budgettingiguess.utils;

import com.we.suck.at.java.budgettingiguess.DownloadController;

import java.util.Random;

public class Utils {
    public static void StartProgressionThread(DownloadController controller)
    {
        ProgressThread pt = new ProgressThread(controller);
        pt.start();
    }

    public static Double parseTextAsDoubleOrNull(String textToParse)
    {
        try {
            return Double.parseDouble(textToParse);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static class ProgressThread extends Thread {
        private DownloadController controller;
        private double progress = 0.0;
        private double targetProgress = 100.0;
        private Random random;
        public ProgressThread(DownloadController controller)
        {
            this.controller = controller;
            this.random = new Random();
        }

        public void run()
        {
            try
            {
                while(progress < targetProgress)
                {
                    double randomProgressGain = random.nextDouble(5);
                    progress += randomProgressGain;
                    controller.setCurrentProgress(progress / targetProgress);
                    long randomSleep = random.nextLong(400); // Max 400ms of sleep
                    Thread.sleep(randomSleep);
                }
            } catch (InterruptedException _) { }

            controller.onProgressComplete();
        }
    }
}

