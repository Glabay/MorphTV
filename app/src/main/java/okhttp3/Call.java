package okhttp3;

import java.io.IOException;

public interface Call extends Cloneable {
    void cancel();

    Call clone();

    void enqueue(Callback callback);

    Response execute() throws IOException;

    boolean isCanceled();

    boolean isExecuted();

    Request request();
}
