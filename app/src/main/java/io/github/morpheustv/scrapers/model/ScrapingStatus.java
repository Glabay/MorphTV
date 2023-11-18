package io.github.morpheustv.scrapers.model;

public class ScrapingStatus {
    public long abort_time = 0;
    public long elapsed_time = 0;
    public int fullhd_sources = 0;
    public int hd_sources = 0;
    public int sd_sources = 0;
    public long started_time = 0;
    public int total_sources = 0;
    public int ukn_sources = 0;

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ScrapingStatus{total_sources=");
        stringBuilder.append(this.total_sources);
        stringBuilder.append(", fullhd_sources=");
        stringBuilder.append(this.fullhd_sources);
        stringBuilder.append(", hd_sources=");
        stringBuilder.append(this.hd_sources);
        stringBuilder.append(", sd_sources=");
        stringBuilder.append(this.sd_sources);
        stringBuilder.append(", ukn_sources=");
        stringBuilder.append(this.ukn_sources);
        stringBuilder.append(", started_time=");
        stringBuilder.append(this.started_time);
        stringBuilder.append(", abort_time=");
        stringBuilder.append(this.abort_time);
        stringBuilder.append(", elapsed_time=");
        stringBuilder.append(this.elapsed_time);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
