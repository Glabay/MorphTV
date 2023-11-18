package okio;

import com.tonyodev.fetch2.util.FetchErrorStrings;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

class Okio$4 extends AsyncTimeout {
    final /* synthetic */ Socket val$socket;

    Okio$4(Socket socket) {
        this.val$socket = socket;
    }

    protected IOException newTimeoutException(@Nullable IOException iOException) {
        IOException socketTimeoutException = new SocketTimeoutException(FetchErrorStrings.CONNECTION_TIMEOUT);
        if (iOException != null) {
            socketTimeoutException.initCause(iOException);
        }
        return socketTimeoutException;
    }

    protected void timedOut() {
        Logger logger;
        Level level;
        StringBuilder stringBuilder;
        try {
            this.val$socket.close();
        } catch (Throwable e) {
            logger = Okio.logger;
            level = Level.WARNING;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to close timed out socket ");
            stringBuilder.append(this.val$socket);
            logger.log(level, stringBuilder.toString(), e);
        } catch (Throwable e2) {
            if (Okio.isAndroidGetsocknameError(e2)) {
                logger = Okio.logger;
                level = Level.WARNING;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to close timed out socket ");
                stringBuilder.append(this.val$socket);
                logger.log(level, stringBuilder.toString(), e2);
                return;
            }
            throw e2;
        }
    }
}
