package com.we.suck.at.java.budgettingiguess.services;

import com.we.suck.at.java.budgettingiguess.utils.ProgressOperator;
import java.util.Random;

public class DownloadService {
    private static boolean downloadInProgress;

    public static void InitializeDownload(ProgressOperator operator) throws Exception{
        if(downloadInProgress)
            throw new Exception("Download already in progress");
        Thread t = new DownloadThread(new ProgressOperator() {
            @Override
            public void OnStartProgress() {
                downloadInProgress = true;
                operator.OnStartProgress();
            }

            @Override
            public void OnProgressUpdate(String status, double progress) {
                operator.OnProgressUpdate(status, progress);
            }

            @Override
            public void OnProgressComplete() {
                operator.OnProgressComplete();
                onDownloadComplete();
            }
        });
        t.start();
    }

    public static void onDownloadComplete()
    {
        downloadInProgress = false;
    }

    static class DownloadThread extends Thread {
        private final ProgressOperator operator;
        private final Random random;
        private double progress = 0;

        public DownloadThread(ProgressOperator po)
        {
            operator = po;
            random = new Random();
        }

        public void run()
        {
            operator.OnStartProgress();
            while(progress < 1d)
            {
                double randomProgress = random.nextDouble(0.01, 0.10);
                progress += randomProgress;

                operator.OnProgressUpdate(getStatus(), progress);

                long randomSleep = random.nextLong(
                        (long)(randomProgress*2500),
                        (long)(randomProgress*5000));
                try {
                    Thread.sleep(randomSleep);
                } catch (InterruptedException e) {
                    break;
                }
            }
            operator.OnProgressComplete();
        }

        private String getStatus()
        {
            if(progress < 0.5)
                return "Downloading...";
            if(progress < 0.9)
                return "Unpacking";
            return "Finishing up...";
        }
    }
}
