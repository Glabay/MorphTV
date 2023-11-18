package okhttp3.internal.cache2;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import okio.Buffer;

final class FileOperator {
    private static final int BUFFER_SIZE = 8192;
    private final byte[] byteArray = new byte[8192];
    private final ByteBuffer byteBuffer = ByteBuffer.wrap(this.byteArray);
    private final FileChannel fileChannel;

    FileOperator(FileChannel fileChannel) {
        this.fileChannel = fileChannel;
    }

    public void write(long j, Buffer buffer, long j2) throws IOException {
        if (j2 >= 0) {
            if (j2 <= buffer.size()) {
                while (j2 > 0) {
                    try {
                        long write;
                        int min = (int) Math.min(PlaybackStateCompat.ACTION_PLAY_FROM_URI, j2);
                        buffer.read(this.byteArray, 0, min);
                        this.byteBuffer.limit(min);
                        while (true) {
                            write = j + ((long) this.fileChannel.write(this.byteBuffer, j));
                            if (this.byteBuffer.hasRemaining() == null) {
                                break;
                            }
                            j = write;
                        }
                        long j3 = j2 - ((long) min);
                        this.byteBuffer.clear();
                        j2 = j3;
                        j = write;
                    } catch (long j4) {
                        this.byteBuffer.clear();
                        throw j4;
                    }
                }
                return;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    public void read(long j, Buffer buffer, long j2) throws IOException {
        if (j2 < 0) {
            throw new IndexOutOfBoundsException();
        }
        while (j2 > 0) {
            try {
                this.byteBuffer.limit((int) Math.min(PlaybackStateCompat.ACTION_PLAY_FROM_URI, j2));
                if (this.fileChannel.read(this.byteBuffer, j) == -1) {
                    throw new EOFException();
                }
                int position = this.byteBuffer.position();
                buffer.write(this.byteArray, 0, position);
                long j3 = (long) position;
                long j4 = j + j3;
                j = j2 - j3;
                this.byteBuffer.clear();
                j2 = j;
                j = j4;
            } catch (long j5) {
                this.byteBuffer.clear();
                throw j5;
            }
        }
    }
}
