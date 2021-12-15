package mirea.it.uzorov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String NOTIFICATION_CHANNEL_ID = "1";
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                workWithCamera();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        Button button = findViewById(R.id.notification_button);
        button.setOnClickListener(v -> showNotification());
        Button buttonCamera = findViewById(R.id.camera_button);
        int permissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        buttonCamera.setOnClickListener(v -> {
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                workWithCamera();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                {Manifest.permission.CAMERA},
                        REQUEST_CODE_PERMISSION_CAMERA);
            }
        });
    }


    private void workWithCamera() {
        Camera camera = Camera.open();
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    private void createNotificationChannel() {
        CharSequence name = "Notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new
                NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


    private void showNotification() {
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Практическая работа 4")
                .setContentText("Узоров Кирилл Александрович ИКБО-07-20")
                .setStyle(new NotificationCompat.BigTextStyle());
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        Random random = new Random();
        notificationManager.notify(random.nextInt(), builder.build());
    }

}