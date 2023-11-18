package com.uwetrottmann.trakt5.entities;

import java.util.List;
import org.threeten.bp.OffsetDateTime;

public class BaseShow {
    public Integer aired;
    public Integer completed;
    public List<Season> hidden_seasons;
    public OffsetDateTime last_collected_at;
    public OffsetDateTime last_watched_at;
    public OffsetDateTime listed_at;
    public Episode next_episode;
    public Integer plays;
    public List<BaseSeason> seasons;
    public Show show;
}
