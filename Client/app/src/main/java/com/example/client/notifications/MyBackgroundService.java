package com.example.client.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.client.MainActivity;
import com.example.client.R;
import com.example.client.data.api.ProductAPI;
import com.example.client.data.model.Product;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MyBackgroundService extends Service {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private List<Product> data;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize your background task here
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getFromDB();

        // Start the service as a foreground service
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        long userId = sharedPreferences.getLong("userId", -1);
        if (userId != -1) {
            startForeground(1, createNotification());
        }

        return START_STICKY;
    }

    private void getFromDB() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        long userId = sharedPreferences.getLong("userId", -1);
        try {
            data = ProductAPI.getProducts(userId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        data = new ArrayList<>(Arrays.asList(
//                new Product(1L, "apple", createDate(2024, 1, 25), 1000f, "cellar"),
//                new Product(2L, "pear", createDate(2024, 1, 1), 1000f, "cellar"),
//                new Product(3L, "milk", createDate(2024, 1, 3), 1000f, "fridge"),
//                new Product(4L, "yeast", createDate(2024, 1, 25), 23423f, "pantry"),
//                new Product(5L, "mere", createDate(2024, 1, 27), 1000f, "fridge"),
//                new Product(6L, "dsa", createDate(2024, 1, 25), 123f, "fridge"),
//                new Product(7L, "mere", createDate(2024, 1, 3), 1000f, "cellar"),
//                new Product(8L, "sada", createDate(2024, 1, 22), 1000f, "fridge"),
//                new Product(9L, "das", createDate(2024, 1, 27), 2342f, "pantry"),
//                new Product(10L, "asd", createDate(2024, 1, 1), 1000f, "cellar")
//        ));
    }

    private static Date createDate(int year, int month, int day) {
        // Month value is 0-based in Calendar, so subtract 1 from the provided month
        Calendar calendar = new GregorianCalendar(year, month - 1, day);
        return calendar.getTime();
    }

    private Notification createNotification() {
        // Use NotificationManager to create/update the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Create a notification
        return new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Food Saver")
                .setContentText(getValue() + " products expire today!")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build();
    }

    private long getValue() {
        return data.stream()
                .map(product -> dateFormat.format(product.expirationDate()))
                .filter(formattedDate -> formattedDate.equals(dateFormat.format(new Date())))
                .count();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
