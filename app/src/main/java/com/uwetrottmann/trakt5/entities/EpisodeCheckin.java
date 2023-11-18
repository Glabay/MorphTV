package com.uwetrottmann.trakt5.entities;

public class EpisodeCheckin extends BaseCheckin {
    public SyncEpisode episode;
    public Show show;

    public static class Builder {
        protected String app_date;
        protected String app_version;
        private SyncEpisode episode;
        protected String message;
        protected ShareSettings sharing;
        private Show show;
        protected String venue_id;
        protected String venue_name;

        public Builder(SyncEpisode syncEpisode, String str, String str2) {
            if (syncEpisode == null) {
                throw new IllegalArgumentException("Episode must not be null");
            }
            this.episode = syncEpisode;
            this.app_version = str;
            this.app_date = str2;
        }

        public Builder show(Show show) {
            this.show = show;
            return this;
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

        public EpisodeCheckin build() {
            EpisodeCheckin episodeCheckin = new EpisodeCheckin();
            episodeCheckin.show = this.show;
            episodeCheckin.episode = this.episode;
            episodeCheckin.sharing = this.sharing;
            episodeCheckin.message = this.message;
            episodeCheckin.venue_id = this.venue_id;
            episodeCheckin.venue_name = this.venue_name;
            episodeCheckin.app_date = this.app_date;
            episodeCheckin.app_version = this.app_version;
            return episodeCheckin;
        }
    }
}
