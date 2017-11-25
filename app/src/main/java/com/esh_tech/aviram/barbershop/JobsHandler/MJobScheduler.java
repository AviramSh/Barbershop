package com.esh_tech.aviram.barbershop.JobsHandler;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Aviram on 19/11/2017.
 */

public class MJobScheduler extends JobService {
    private static final String TAG = "Programmer";
    MJobExecuter mJobExecuter;

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {


        mJobExecuter = new MJobExecuter(getApplicationContext()){
            @Override
            protected void onPostExecute(String s) {
//                Log.d(TAG,"Background Long Running Task Finished...");
                Toast.makeText(getApplicationContext(), s+"", Toast.LENGTH_SHORT).show();
                jobFinished(jobParameters,true);
            }
        };


        mJobExecuter.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG,"onStopJob");

        mJobExecuter.cancel(true);
        return true;
    }
}
