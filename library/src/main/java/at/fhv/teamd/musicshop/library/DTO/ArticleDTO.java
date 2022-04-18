package at.fhv.teamd.musicshop.library.DTO;

import java.time.LocalDate;
import java.util.Set;

public interface ArticleDTO {
    Long id();

    String title();

    String label();

    LocalDate releaseDate();

    String genre();

    String musicbrainzId();

    Set<ArtistDTO> artists();
}
