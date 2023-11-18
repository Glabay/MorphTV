package com.uwetrottmann.trakt5.entities;

public class SyncResponse {
    public SyncStats added;
    public SyncStats deleted;
    public SyncStats existing;
    public SyncErrors not_found;
}
