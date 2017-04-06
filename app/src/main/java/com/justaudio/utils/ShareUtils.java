package com.justaudio.utils;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.justaudio.activities.BaseActivity;

import java.util.List;

/**
 * Created by ${VIDYA}
 */

public class ShareUtils {


    /*
   * OPEN THE G.MAIL APP
   * */
    public static void shareWithGMAIL(BaseActivity parent) {

        String subject = "Feedback ?";
        String body = "";
        String toMail = "bandla7@gmail.com";

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{toMail});
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            final PackageManager pm = parent.getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
            ResolveInfo best = null;
            for (final ResolveInfo info : matches) {
                if (info.activityInfo.packageName.endsWith(".gm")
                        || info.activityInfo.name.toLowerCase().contains("gmail"))
                    best = info;
            }

            if (best != null) {
                emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                parent.startActivityForResult(emailIntent, 1);
            } else {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_SUBJECT, subject);
                i.putExtra(Intent.EXTRA_TEXT, body);
                parent.startActivity(Intent.createChooser(emailIntent,
                        "Send Email Using: "));
            }
        } catch (Exception e) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_SUBJECT, subject);
            i.putExtra(Intent.EXTRA_TEXT, body);
            parent.startActivity(Intent.createChooser(emailIntent,
                    "Send Email Using: "));
        }
    }
}
