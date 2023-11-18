package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.error_code;

public final class ErrorCode {
    private final error_code ec;

    public ErrorCode(error_code error_code) {
        this.ec = error_code;
    }

    public ErrorCode() {
        this(new error_code());
    }

    public error_code swig() {
        return this.ec;
    }

    public void clear() {
        this.ec.clear();
    }

    public int value() {
        return this.ec.value();
    }

    public String message() {
        return this.ec.message();
    }
}
