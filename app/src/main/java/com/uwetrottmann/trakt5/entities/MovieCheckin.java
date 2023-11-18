package com.uwetrottmann.trakt5.entities;

public class MovieCheckin extends BaseCheckin {
    public SyncMovie movie;

    public static class Builder {
        protected String app_date;
        protected String app_version;
        protected String message;
        private SyncMovie movie;
        protected ShareSettings sharing;
        protected String venue_id;
        protected String venue_name;

        public Builder(SyncMovie syncMovie, String str, String str2) {
            if (syncMovie == null) {
                throw new IllegalArgumentException("Movie must not be null");
            }
            this.movie = syncMovie;
            this.app_version = str;
            this.app_date = str2;
        }

        public Builder sharing(ShareSettings shareSettings) {
            this.sharing = shareSettings;
            return this;
        }

        public Builder message(String str) {
            this.message = str;
            return this;
        }

        public Builder venueId(String str) {
            this.venue_id = str;
            return this;
        }

        public Builder venueName(String str) {
            this.venue_name = str;
            return this;
        }

        public MovieCheckin build() {
            MovieCheckin movieCheckin = new MovieCheckin();
            movieCheckin.movie = this.movie;
            movieCheckin.sharing = this.sharing;
            movieCheckin.message = this.message;
            movieCheckin.venue_id = this.venue_id;
            movieCheckin.venue_name = this.venue_name;
            movieCheckin.app_date = this.app_date;
            movieCheckin.app_version = this.app_version;
            return movieCheckin;
        }
    }
}
