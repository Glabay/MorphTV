package retrofit2;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

class OkHttpCall$1 implements Callback {
    final /* synthetic */ OkHttpCall this$0;
    final /* synthetic */ Callback val$callback;

    OkHttpCall$1(OkHttpCall okHttpCall, Callback callback) {
        this.this$0 = okHttpCall;
        this.val$callback = callback;
    }

    public void onResponse(Call call, Response response) throws IOException {
        try {
            callSuccess(this.this$0.parseResponse(response));
        } catch (Call call2) {
            callFailure(call2);
        }
    }

    public void onFailure(Call call, IOException iOException) {
        try {
            this.val$callback.onFailure(this.this$0, iOException);
        } catch (Call call2) {
            call2.printStackTrace();
        }
    }

    private void callFailure(Throwable th) {
        try {
            this.val$callback.onFailure(this.this$0, th);
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    private void callSuccess(Response<T> response) {
        try {
            this.val$callback.onResponse(this.this$0, response);
        } catch (Response<T> response2) {
            response2.printStackTrace();
        }
    }
}
