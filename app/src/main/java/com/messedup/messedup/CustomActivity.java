package com.messedup.messedup;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rampo.updatechecker.UpdateChecker;
import com.rampo.updatechecker.UpdateCheckerResult;

/**
 * Created by saurabh on 29/9/17.
 */

public class CustomActivity extends Activity implements UpdateCheckerResult {

    public void custom_impl(View view) {
        UpdateChecker checker = new UpdateChecker(this, this);
        checker.setSuccessfulChecksRequired(1);
        checker.start();
    }
    @Override
    public void foundUpdateAndShowIt(String versionDonwloadable) {

    }

    @Override
    public void foundUpdateAndDontShowIt(String versionDonwloadable) {

    }

    @Override
    public void returnUpToDate(String versionDonwloadable) {

    }

    @Override
    public void returnMultipleApksPublished() {

    }

    @Override
    public void returnNetworkError() {

    }

    @Override
    public void returnAppUnpublished() {

        Log.e("UpdateChecker", "App is not published " + "CurrentVersion" + mVersionInstalled());
        Toast.makeText(getApplicationContext(), "App is not published", Toast.LENGTH_SHORT).show();

    }

    private String mVersionInstalled() {

        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return null;
    }

    @Override
    public void returnStoreError() {

    }


}
