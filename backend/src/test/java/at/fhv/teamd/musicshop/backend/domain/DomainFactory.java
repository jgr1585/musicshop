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

@SuppressWarnings("deprecation")
public class DomainFactory {

    public static Article createArticle() {
        return createSong();
    }

    public static Album createAlbum() {
        UUID uuid = UUID.randomUUID();

        return new Album("Ablum" + uuid, "Label" + uuid, LocalDate.now(), "Genre", uuid.toString(), Set.of(createSong()));
    }

    public static Song createSong() {
        UUID uuid = UUID.randomUUID();
        return new Song("Song " + uuid, "Label " + uuid, LocalDate.now(), "Generic",  uuid.toString(), Duration.ofSeconds(521), Set.of(createArtist()));
    }

    public static Artist createArtist() {
        UUID uuid = UUID.randomUUID();
        return new Artist("Artist " + uuid);
    }

    public static Medium createMedium(MediumType mediumType) {
        UUID uuid = UUID.randomUUID();

        return new Medium(uuid.getMostSignificantBits(), mediumType, BigDecimal.TEN, Stock.of(Quantity.of(5)), createSupplier(), createArticle());
    }

    public static Supplier createSupplier() {
        UUID uuid = UUID.randomUUID();

        return new Supplier("Supplier " + uuid, Duration.ofHours(24));
    }
}