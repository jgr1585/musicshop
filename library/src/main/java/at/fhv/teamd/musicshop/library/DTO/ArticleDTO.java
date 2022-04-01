package at.fhv.teamd.musicshop.library.DTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ArticleDTO {
    Long id();

    String descriptorName();

    String title();

    String label();

    LocalDate releaseDate();

    String genre();

    String musicbrainzId();

    Map<String, AnalogMediumDTO> analogMedium();

    List<ArtistDTO> artists();
}
