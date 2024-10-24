package org.sture.java.budgeting.services.job;

import org.sture.java.budgeting.IStatusBarController;

import java.util.HashMap;
import java.util.UUID;

public class BackgroundJobExecutionService {
    private final IStatusBarController statusBarController;
    private final HashMap<UUID, JobThread> jobToThreadMap;

    public BackgroundJobExecutionService(IStatusBarController statusBarController) {
        this.statusBarController = statusBarController;
        if(statusBarController == null)
            throw new RuntimeException("StatusBarController cannot be null! You most likely failed to Mock it during testing! :D");

        jobToThreadMap = new HashMap<>();
    }

    public UUID LaunchBackgroundJob(Job job) {
        UUID controllerProgressId = statusBarController.CreateNewProgression();

        job.setupMessageCallback((String msg)       -> statusBarController.SetProgressMessageTo(controllerProgressId, msg));
        job.setupProgressionCallback((Double msg)   -> statusBarController.SetProgressProgressionTo(controllerProgressId, msg));
        job.setupOnDoneCallback((String msg)        -> {
            statusBarController.SetProgressCompleted(controllerProgressId, "Completed");
            statusBarController.DeleteProgressionAfterDelay(controllerProgressId, 1d);
            jobToThreadMap.remove(job.Id);
        });

        JobThread thread = new JobThread(job, this);
        thread.start();
        statusBarController.SetProgressStarted(controllerProgressId);
        jobToThreadMap.put(job.Id, thread);

        return job.Id;
    }

    public boolean BackgroundJobIsActive(UUID jobID){
        return jobToThreadMap.containsKey(jobID);
    }

    public void WaitForJobToFinish(UUID jobId) {
        Thread t = jobToThreadMap.getOrDefault(jobId, null);
        if(t != null) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class JobThread extends Thread {
        private final Job job;

        public JobThread(Job job, BackgroundJobExecutionService ctrl){
            this.job = job;
        }

        public void run(){
            job.Execute();
            job.OnDone();
        }
    }
}

