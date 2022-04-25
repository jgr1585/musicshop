package at.fhv.teamd.musicshop.backend.domain;

import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.article.Artist;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.medium.Stock;
import at.fhv.teamd.musicshop.backend.domain.medium.Supplier;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class DomainFactory {

    public static Article createArticle() throws NoSuchFieldException, IllegalAccessException {
        return createSong();
    }

    public static Album createAlbum() throws NoSuchFieldException, IllegalAccessException {
        UUID uuid = UUID.randomUUID();
        Song song = createPrivateSong();
        Album album = new Album("Album" + uuid, "Label" + uuid, LocalDate.now(), "Genre", uuid.toString(), Set.of(song));
        setSongAlbums(song, Set.of(album));

        return album;
    }

    private static void setSongAlbums(Song song, Set<Album> albums) throws IllegalAccessException, NoSuchFieldException {
        setObjField(song, "albums", albums);
    }

    private static void setAlbumMediums(Album album, Set<Medium> mediums) throws IllegalAccessException, NoSuchFieldException {
        setObjField(album, "mediums", mediums);
    }

    // set (private) object field by reflection
    private static void setObjField(Object obj, String fieldName, Object fieldValue) throws IllegalAccessException, NoSuchFieldException {
        // reference field of obj
        Field field = obj.getClass().getDeclaredField(fieldName);
        // suppress checks for Java language access control for field (required to access private field)
        field.setAccessible(true);
        // set field of object to value
        field.set(obj, fieldValue);
    }

    public static Song createSong() throws NoSuchFieldException, IllegalAccessException {
        Album album = createAlbum();
        return album.getSongs().iterator().next();
    }

    private static Song createPrivateSong() {
        UUID uuid = UUID.randomUUID();
        return new Song("Song " + uuid, "Label " + uuid, LocalDate.now(), "Generic",  uuid.toString(), Duration.ofSeconds(521), Set.of(createArtist()));
    }

    public static Artist createArtist() {
        UUID uuid = UUID.randomUUID();
        return new Artist("Artist " + uuid);
    }

    public static Medium createMedium(MediumType mediumType) throws NoSuchFieldException, IllegalAccessException {
        UUID uuid = UUID.randomUUID();
        Album album = createAlbum();
        Medium medium = new Medium(uuid.getMostSignificantBits(), mediumType, BigDecimal.TEN, Stock.of(Quantity.of(5)), createSupplier(), album);
        setAlbumMediums(album, Set.of(medium));
        return medium;
    }

    public static Supplier createSupplier() {
        UUID uuid = UUID.randomUUID();
        return new Supplier("Supplier " + uuid, Duration.ofHours(24));
    }
}
