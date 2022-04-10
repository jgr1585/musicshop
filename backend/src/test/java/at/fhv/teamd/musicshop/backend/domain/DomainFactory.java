package at.fhv.teamd.musicshop.backend.domain;

import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.article.Artist;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.medium.Stock;
import at.fhv.teamd.musicshop.backend.domain.medium.Supplier;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class DomainFactory {

    public static Article createArticle() {
        return createSong();
    }

    public static Album createAlbum() {
        UUID uuid = UUID.randomUUID();

        return new Album("Ablum" + uuid, "Label" + uuid, LocalDate.now(), "Genre", "Album desription" + uuid, uuid.toString(), Set.of(createMedium(MediumType.CD)), Set.of(createSong()));
    }

    public static Song createSong() {
        UUID uuid = UUID.randomUUID();

        return new Song("Song " + uuid, "Label " + uuid, LocalDate.now(), "Generic", "Song description " + uuid, uuid.toString(), Set.of(createMedium(MediumType.DIGITAL)), Duration.ofSeconds(521), Set.of(createArtist()));
    }

    public static Artist createArtist() {
        UUID uuid = UUID.randomUUID();

        return new Artist("Artist " + uuid);
    }

    public static Medium createMedium(MediumType mediumType) {
        UUID uuid = UUID.randomUUID();

        return new Medium(BigDecimal.TEN, mediumType, Stock.of(Quantity.of(5)), createSupplier());
    }

    public static Supplier createSupplier() {
        UUID uuid = UUID.randomUUID();

        return new Supplier("Supplier " + uuid, Duration.ofHours(24));
    }
}
