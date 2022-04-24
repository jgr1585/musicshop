package at.fhv.teamd.musicshop.backend.domain.article;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

public class AlbumTest extends Article {

    @Test
    void given_setsongs_when_findartistsfromsongs_then_returnartists() {

        //given
        Song song = DomainFactory.createSong();
        Set<Artist> artistsExpected = song.getArtists();

        //when
        Album album = new Album("", "", LocalDate.now(), "", "", Set.of(song));
        Set<Artist> artistsActual = album.getArtists();

        //then
        Assertions.assertEquals(artistsExpected, artistsActual);
    }
}
