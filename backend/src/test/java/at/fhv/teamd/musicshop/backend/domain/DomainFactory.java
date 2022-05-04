package at.fhv.teamd.musicshop.backend.domain;

import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.article.Artist;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.medium.Stock;
import at.fhv.teamd.musicshop.backend.domain.medium.Supplier;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.domain.topic.Topic;

import java.lang.reflect.Field;
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
        Song song = createPrivateSong();
        Album album = new Album("Album" + uuid, "Label" + uuid, LocalDate.now(), "Genre", uuid.toString(), Set.of(song));
        setObjField(album, "mediums", Set.of(createMedium(MediumType.CD, album)));
        setSongAlbums(song, Set.of(album));

        return album;
    }

    private static void setSongAlbums(Song song, Set<Album> albums) {
        setObjField(song, "albums", albums);
    }

    private static void setAlbumMediums(Album album, Set<Medium> mediums) {
        setObjField(album, "mediums", mediums);
    }

    // set (private) object field by reflection
    private static void setObjField(Object obj, String fieldName, Object fieldValue) {
        try {
            // reference field of obj
            Field field = obj.getClass().getDeclaredField(fieldName);
            // suppress checks for Java language access control for field (required to access private field)
            field.setAccessible(true);
            // set field of object to value
            field.set(obj, fieldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Song createSong() {
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

    public static Medium createMedium(MediumType mediumType) {
        Album album = createAlbum();
        return createMedium(mediumType, album);
    }

    public static Medium createMedium(MediumType mediumType, Album album) {
        UUID uuid = UUID.randomUUID();
        Medium medium = new Medium(BigDecimal.TEN, mediumType, Stock.of(Quantity.of(5)), createSupplier(), album);
        setObjField(medium, "id", uuid.getLeastSignificantBits());
        setAlbumMediums(album, Set.of(medium));
        return medium;
    }

    public static Supplier createSupplier() {
        UUID uuid = UUID.randomUUID();
        return new Supplier("Supplier " + uuid, Duration.ofHours(24));
    }

    public static LineItem createLineItem() {
        LineItem lineItem = new LineItem(Quantity.of(3), createMedium(MediumType.CD));
        setObjField(lineItem, "id", UUID.randomUUID().getMostSignificantBits());
        return lineItem;
    }

    public static Invoice createInvoice() {
        Invoice invoice = Invoice.of(Set.of(createLineItem()));
        setObjField(invoice, "id", UUID.randomUUID().getMostSignificantBits());
        return invoice;
    }

    public static Topic createTopic() {
        UUID uuid = UUID.randomUUID();
        return createTopic("Topic" + uuid);
    }

    public static Topic createTopic(String name) {
        return new Topic(name);
    }
}
