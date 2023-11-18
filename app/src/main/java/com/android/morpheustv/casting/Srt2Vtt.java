package com.android.morpheustv.casting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class Srt2Vtt {

    enum State {
        Number,
        TimeStamp,
        Text
    }

    public void convert(Reader reader, Writer writer) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        writer.write("WEBVTT\n\n");
        State state = State.Number;
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                switch (state) {
                    case Number:
                        state = State.TimeStamp;
                        break;
                    case TimeStamp:
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(readLine.replace(',', '.'));
                        stringBuilder.append("\n");
                        writer.write(stringBuilder.toString());
                        state = State.Text;
                        break;
                    case Text:
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(readLine);
                        stringBuilder2.append("\n");
                        writer.write(stringBuilder2.toString());
                        if (readLine.length() != 0) {
                            break;
                        }
                        state = State.Number;
                        break;
                    default:
                        break;
                }
            }
            writer.close();
            reader.close();
            return;
        }
    }
}
