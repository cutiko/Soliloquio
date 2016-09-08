package cl.cutiko.soliloquio.background;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class PlayerService extends Service {

    private final IBinder binder = new LocalBinder();

    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {

        public PlayerService getService() {
            return PlayerService.this;
        }

    }

    public void testMethod() {
        Log.d("BIND", "binded");
    }
}
