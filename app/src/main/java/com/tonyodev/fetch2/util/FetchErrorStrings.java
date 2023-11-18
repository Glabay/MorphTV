package com.tonyodev.fetch2.util;

import kotlin.Metadata;
import kotlin.jvm.JvmName;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0016\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u000b\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\f\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\r\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u000e\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u000f\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0010\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0011\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0012\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0013\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0014\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0015\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0016\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"CONNECTION_TIMEOUT", "", "DATABASE_DISK_FULL", "DOWNLOAD_NOT_FOUND", "EACCES", "EMPTY_RESPONSE_BODY", "ENOENT", "ENOSPC", "ETIMEDOUT", "FAILED_TO_CONNECT", "FAILED_TO_ENQUEUE_REQUEST", "FETCH_ALREADY_EXIST", "FETCH_DATABASE_ERROR", "FNC", "HOST_RESOLVE_ISSUE", "IO404", "NO_ADDRESS_HOSTNAME", "READ_TIME_OUT", "RESPONSE_NOT_SUCCESSFUL", "SOFTWARE_ABORT_CONNECTION", "UNIQUE_FILE_PATH_DATABASE", "UNIQUE_ID_DATABASE", "UNKNOWN_ERROR", "fetch2_release"}, k = 2, mv = {1, 1, 10})
@JvmName(name = "FetchErrorStrings")
/* compiled from: ErrorStrings.kt */
public final class FetchErrorStrings {
    @NotNull
    public static final String CONNECTION_TIMEOUT = "timeout";
    @NotNull
    public static final String DATABASE_DISK_FULL = "database or disk is full (code 13)";
    @NotNull
    public static final String DOWNLOAD_NOT_FOUND = "fetch download not found";
    @NotNull
    public static final String EACCES = "open failed: EACCES (Permission denied)";
    @NotNull
    public static final String EMPTY_RESPONSE_BODY = "empty_response_body";
    @NotNull
    public static final String ENOENT = "open failed: ENOENT (No such file or directory)";
    @NotNull
    public static final String ENOSPC = "write failed: ENOSPC (No space left on device)";
    @NotNull
    public static final String ETIMEDOUT = "recvfrom failed: ETIMEDOUT (Connection timed out)";
    @NotNull
    public static final String FAILED_TO_CONNECT = "Failed to connect";
    @NotNull
    public static final String FAILED_TO_ENQUEUE_REQUEST = "UNIQUE constraint failed: requests._id (code 1555)";
    @NotNull
    public static final String FETCH_ALREADY_EXIST = "already exists. You cannot have more than one active instance of Fetch with the same namespace. Did your forget to call close the old instance?";
    @NotNull
    public static final String FETCH_DATABASE_ERROR = "Fetch data base error";
    @NotNull
    public static final String FNC = "FNC";
    @NotNull
    public static final String HOST_RESOLVE_ISSUE = "Unable to resolve host";
    @NotNull
    public static final String IO404 = "java.io.IOException: 404";
    @NotNull
    public static final String NO_ADDRESS_HOSTNAME = "No address associated with hostname";
    @NotNull
    public static final String READ_TIME_OUT = "Read timed out at";
    @NotNull
    public static final String RESPONSE_NOT_SUCCESSFUL = "request_not_successful";
    @NotNull
    public static final String SOFTWARE_ABORT_CONNECTION = "Software caused connection abort";
    @NotNull
    public static final String UNIQUE_FILE_PATH_DATABASE = "UNIQUE constraint failed: requests._file";
    @NotNull
    public static final String UNIQUE_ID_DATABASE = "UNIQUE constraint failed: requests._id";
    @NotNull
    public static final String UNKNOWN_ERROR = "unknown";
}
