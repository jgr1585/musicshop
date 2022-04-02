package at.fhv.teamd.musicshop.library.DTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ArticleDTO {
    Long id();

    String descriptorName();

    String title();

    String label();

    LocalDate releaseDate();

    String genre();

    String musicbrainzId();

    Set<MediumDTO> mediums();

    Set<ArtistDTO> artists();
}
