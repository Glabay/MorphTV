package retrofit2;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;

class Platform$Android$MainThreadExecutor implements Executor {
    private final Handler handler = new Handler(Looper.getMainLooper());

    Platform$Android$MainThreadExecutor() {
    }

    public void execute(Runnable runnable) {
        this.handler.post(runnable);
    }
}
