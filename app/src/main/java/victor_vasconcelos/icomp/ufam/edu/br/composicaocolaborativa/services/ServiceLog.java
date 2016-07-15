package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Victor Freitas Vasconcelos on 13/07/2016.
 */
public class ServiceLog extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        super.onStartCommand(intent,flags,startId);
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        stopSelf();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
