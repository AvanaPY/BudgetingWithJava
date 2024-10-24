package org.sture.java.budgeting.services.job;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Job {
    public final UUID Id;

    private final ArrayList<JobCallback<String>> messageChangedCallbacks;
    private final ArrayList<JobCallback<Double>> progressionChangedCallbacks;
    private final ArrayList<JobCallback<String>> onDoneCallbacks;

    protected Job(UUID uuid){
        this.Id = uuid;
        messageChangedCallbacks = new ArrayList<>();
        progressionChangedCallbacks = new ArrayList<>();
        onDoneCallbacks = new ArrayList<>();
    }
    public abstract void Execute();

    protected void OnDone(){
        UpdateMessage("Complete");
        UpdateProgression(1.0d);
        for(var callback : onDoneCallbacks)
            callback.Run("Done");
    }

    protected void UpdateMessage(String message){
        for(var callback : messageChangedCallbacks)
            callback.Run(message);
    }

    protected void UpdateProgression(double progression){
        if(0 <= progression && progression <= 1){
            for(var callback : progressionChangedCallbacks)
                callback.Run(progression);
        } else {
            throw new RuntimeException("Tried to set progression to " + progression);
        }
    }

    public void setupMessageCallback(JobCallback<String> job){
        messageChangedCallbacks.add(job);
    }

    public void setupProgressionCallback(JobCallback<Double> job){
        progressionChangedCallbacks.add(job);
    }

    public void setupOnDoneCallback(JobCallback<String> job) {
        onDoneCallbacks.add(job);
    }
}

