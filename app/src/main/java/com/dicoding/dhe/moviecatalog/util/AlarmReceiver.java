/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.dicoding.dhe.moviecatalog.BuildConfig;
import com.dicoding.dhe.moviecatalog.MainActivity;
import com.dicoding.dhe.moviecatalog.R;
import com.dicoding.dhe.moviecatalog.activity.ReleaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TYPE_ONE_TIME = "OneTimeAlarm";
    public static final String TYPE_REPEATING = "RepeatingAlarm";
    public static final String TYPE_UPCOMING = "UpcomingAlarm";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    private final int NOTIF_ID_ONETIME = 100;
    private final int NOTIF_ID_REPEATING = 101;
    private final int NOTIF_ID_UPCOMING = 102;

    private static final String API_KEY = BuildConfig.API_KEY;
    public static final String TAG = "ContentValues";
    private String BASE_URL = "https://api.themoviedb.org/";
    private String BASE_URL2 = "https://private-5d1b4-themoviedbmock.apiary-mock.com/";
    private int i=0;


    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        String title = context.getResources().getString(R.string.app_name);
        int notifId = type.equalsIgnoreCase(TYPE_ONE_TIME) ? NOTIF_ID_ONETIME : (type.equalsIgnoreCase(TYPE_REPEATING) ? NOTIF_ID_REPEATING : NOTIF_ID_UPCOMING);

        if(notifId == NOTIF_ID_UPCOMING) {
            getMovie(context);
        } else {
            showAlarmNotification(context, title, message, notifId);
        }
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_movies_white_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        notificationManagerCompat.notify(notifId, builder.build());
    }

    public void setRepeatingAlarm(Context context, String type, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) calendar.add(Calendar.DATE, 1);

        int requestCode = NOTIF_ID_REPEATING;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void setMovieAlarm(Context context, String type, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) calendar.add(Calendar.DATE, 1);

        int requestCode = NOTIF_ID_UPCOMING;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        int requestCode = type.equalsIgnoreCase(TYPE_ONE_TIME) ? NOTIF_ID_ONETIME : (type.equalsIgnoreCase(TYPE_REPEATING) ? NOTIF_ID_REPEATING : NOTIF_ID_UPCOMING);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    private void getMovie(final Context context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateNow = dateFormat.format(new Date());
        AndroidNetworking.get(BASE_URL+"3/discover/movie?api_key="+API_KEY+"&primary_release_date.gte="+dateNow+"&primary_release_date.lte="+dateNow)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JSONArray list = response.getJSONArray("results");

                            for (int i = 0; i < 5; i++) {
                                JSONObject movie = list.getJSONObject(i);
                                if(!movie.equals(""))
                                showNotification(context,movie);
                                SystemClock.sleep(500);
                            }
                        } catch (Exception e) {
                            Log.d("Exception", e.getMessage());
                            if(i < 6){
                                SystemClock.sleep(200);
                                if(i>=3 && i<6) {
                                    BASE_URL = BASE_URL2;
                                }
                                getMovie(context);
                            }
                            i++;
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d(TAG, "onError: " + error); //untuk log pada onerror
                    }
                });
    }


    /**
     * Menampilkan datanya ke dalam notification
     *
     * @param context context dari notification
     */
    private void showNotification(Context context, JSONObject movies) {
        String overview="", title="", rating="", popularity="", image="", release="", language="", imageDetail="";
        int movieid = 0;
        String CHANNEL_ID = "Channel_"+movieid;
        String CHANNEL_NAME = context.getResources().getString(R.string.label_release_reminder);


        try {
            overview = movies.getString("overview") ;
            title = movies.getString("title");
            rating = movies.getString("vote_average");
            popularity = movies.getString("popularity");
            image = movies.getString("poster_path");
            imageDetail = movies.getString("backdrop_path");
            release = movies.getString("release_date");
            language = movies.getString("original_language");
            movieid = movies.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(context, ReleaseActivity.class);
        intent.putExtra(ReleaseActivity.EXTRA_ID, String.valueOf(movieid));
        intent.putExtra(ReleaseActivity.EXTRA_NAME, title);
        intent.putExtra(ReleaseActivity.EXTRA_OVERVIEW, overview);
        intent.putExtra(ReleaseActivity.EXTRA_RATING, rating);
        intent.putExtra(ReleaseActivity.EXTRA_RUNTIME, release);
        intent.putExtra(ReleaseActivity.EXTRA_LANGUAGE, language);
        intent.putExtra(ReleaseActivity.EXTRA_POPULARITY, popularity);
        intent.putExtra(ReleaseActivity.EXTRA_IMAGE, image);
        intent.putExtra(ReleaseActivity.EXTRA_IMAGE_DETAIL, imageDetail);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, movieid, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_movies_white_24dp)
                .setContentText(overview)
                .setColor(ContextCompat.getColor(context, android.R.color.black))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(movieid, notification);
        }
    }
}
