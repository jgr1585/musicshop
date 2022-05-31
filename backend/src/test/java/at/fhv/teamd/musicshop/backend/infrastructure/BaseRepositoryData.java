package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.TestGenerator;
import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.article.Artist;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.customer.CreditCardType;
import at.fhv.teamd.musicshop.backend.domain.customer.Customer;
import at.fhv.teamd.musicshop.backend.domain.customer.Gender;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.medium.Stock;
import at.fhv.teamd.musicshop.backend.domain.medium.Supplier;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.domain.topic.Topic;
import at.fhv.teamd.musicshop.backend.domain.employee.Employee;
import at.fhv.teamd.musicshop.library.permission.UserRole;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseRepositoryData {

    private static final Set<Album> albums;
    private static final Set<Artist> artists;
    private static final Set<Song> songs;
    private static final Set<Medium> media;
    private static final Set<Supplier> suppliers;
    private static final Set<Employee> employees;
    private static final Set<Customer> customers;
    private static final Set<Topic> topics;

    private static final Set<Invoice> invoices;

    static {
        albums = new HashSet<>();
        artists = new HashSet<>();
        songs = new HashSet<>();
        media = new HashSet<>();
        suppliers = new HashSet<>();
        employees = new HashSet<>();
        customers = new HashSet<>();
        topics = new HashSet<>();
        invoices = new HashSet<>();

        init();
    }

    public static Set<Album> getAlbums() {
        return Collections.unmodifiableSet(albums);
    }

    public static Set<Song> getSongs() {
        return Collections.unmodifiableSet(songs);
    }

    public static Set<Medium> getMedia() {
        return Collections.unmodifiableSet(media);
    }

    public static Set<Supplier> getSuppliers() {
        return Collections.unmodifiableSet(suppliers);
    }

    public static Set<Invoice> getInvoices() {
        return Collections.unmodifiableSet(invoices);
    }

    public static Set<Article> getArticles() {
        return Stream.concat(albums.stream(), songs.stream())
                .collect(Collectors.toUnmodifiableSet());
    }

    public static Set<Topic> getTopics() {
        return Collections.unmodifiableSet(topics);
    }

    public static Set<Employee> getEmployees() {
        return Collections.unmodifiableSet(employees);
    }

    public static Set<Customer> getCustomers() { return Collections.unmodifiableSet(customers); }

    public static void init() {
        // Reset the database
        deleteDatabase();

        albums.clear();
        artists.clear();
        songs.clear();
        media.clear();
        suppliers.clear();
        employees.clear();
        customers.clear();
        topics.clear();
        invoices.clear();


        // ALBUM 1
        Artist artistLongLive1 = new Artist("A$AP Rocky");
        Artist artistLongLive2 = new Artist("ScHoolboy Q");
        Artist artistLongLive3 = new Artist("Santigold");
        Artist artistLongLive4 = new Artist("OverDoz");
        Artist artistLongLive5 = new Artist("Drake");
        Artist artistLongLive6 = new Artist("2 Chainz");
        Artist artistLongLive7 = new Artist("Kendrick Lamar");
        Artist artistLongLive8 = new Artist("Skrillex");
        Artist artistLongLive9 = new Artist("Joey Bada$$");
        Artist artistLongLive10 = new Artist("Yelawolf");
        Artist artistLongLive11 = new Artist("Danny Brown");
        Artist artistLongLive12 = new Artist("Action Bronson");
        Artist artistLongLive13 = new Artist("Big K.R.I.T");
        Artist artistLongLive14 = new Artist("Gucci Mane");
        Artist artistLongLive15 = new Artist("Waka Flocka Flame");
        Artist artistLongLive16 = new Artist("Pharrell");
        Artist artistLongLive17 = new Artist("Gunplay");
        Artist artistLongLive18 = new Artist("A$AP Ferg");
        Artist artistLongLive19 = new Artist("Florence Welch");
        Artist artistLongLive20 = new Artist("Bun B");
        Artist artistLongLive21 = new Artist("Paul Wall");
        Artist artistLongLive22 = new Artist("Killa Kyleon");

        String mbidLongLive = "ab967b6a-de5c-455b-aad2-8b2f5899b288";
        String labelLongLive = "Sony Records International";
        String genreLongLive = "hip hop, dubstep, east coast hip hop, underground hip hop";
        LocalDate releaseLongLive = LocalDate.of(2013, 10, 23);
        Supplier supplier1 = new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(3));

        Song songLongLive1 = new Song("Long Live A$AP", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(4), Set.of(artistLongLive1));
        Song songLongLive2 = new Song("Goldie", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1));
        Song songLongLive3 = new Song("PMW (All I Really Need)", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1, artistLongLive2));
        Song songLongLive4 = new Song("LVL", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1));
        Song songLongLive5 = new Song("Hell", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1, artistLongLive3));
        Song songLongLive6 = new Song("Pain", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1, artistLongLive4));
        Song songLongLive7 = new Song("F**kin' Problems", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1, artistLongLive5, artistLongLive6, artistLongLive7));
        Song songLongLive8 = new Song("Wild for the Night", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1, artistLongLive8));
        Song songLongLive9 = new Song("Train", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(6), Set.of(artistLongLive1, artistLongLive9, artistLongLive10, artistLongLive11, artistLongLive12, artistLongLive13));
        Song songLongLive10 = new Song("Fashion Killa", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1));
        Song songLongLive11 = new Song("Phoenix", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1));
        Song songLongLive12 = new Song("Suddenly", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(4), Set.of(artistLongLive1));
        Song songLongLive13 = new Song("Jodye", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(4), Set.of(artistLongLive1, artistLongLive14, artistLongLive15, artistLongLive16));
        Song songLongLive14 = new Song("Ghetto Symphony", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1, artistLongLive17, artistLongLive18));
        Song songLongLive15 = new Song("Angels", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1));
        Song songLongLive16 = new Song("I Come Apart", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1, artistLongLive19));
        Song songLongLive17 = new Song("Purple Swag (remix)", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(4), Set.of(artistLongLive1, artistLongLive20, artistLongLive21, artistLongLive22));

        Album albumLongLive = new Album("Long Live A$AP",
                labelLongLive,
                releaseLongLive,
                genreLongLive,
                mbidLongLive,
                Set.of(songLongLive1, songLongLive2, songLongLive3, songLongLive4, songLongLive5, songLongLive6, songLongLive7, songLongLive8, songLongLive9, songLongLive10, songLongLive11, songLongLive12, songLongLive13, songLongLive14, songLongLive15, songLongLive16, songLongLive17));

        albumLongLive.getSongs()
                .forEach(song -> setObjField(song, "albums", Set.of(albumLongLive)));

        //ALBUM 2
        Artist artistUntamedDesire1 = new Artist("50 Cent");
        Artist artistUntamedDesire2 = new Artist("Yo Gotti");
        Artist artistUntamedDesire3 = new Artist("Trey Songz");
        Artist artistUntamedDesire4 = new Artist("Kidd Kidd");
        Artist artistUntamedDesire5 = new Artist("Jadakiss");
        Artist artistUntamedDesire6 = new Artist("Mr. Probz");
        Artist artistUntamedDesire7 = new Artist("Guordan Banks");
        Artist artistUntamedDesire8 = new Artist("Prodigy");
        Artist artistUntamedDesire9 = new Artist("Styles P");

        String mbidUntamedDesire = "b4cfbcbc-b98c-4695-94c9-e095412e4a84";
        String labelUntamedDesire = "G-Unit Records";
        String genreUntamedDesire = "hip hop, rap";
        LocalDate releaseUntamedDesire = LocalDate.of(2014, 6, 3);
        Supplier supplier2 = new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(7));

        Song songUntamedDesire1 = new Song("Hold On", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1));
        Song songUntamedDesire2 = new Song("Don't Worry 'Bout It", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(4), Set.of(artistUntamedDesire1, artistUntamedDesire2));
        Song songUntamedDesire3 = new Song("Animal Ambition", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1));
        Song songUntamedDesire4 = new Song("Pilot", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1));
        Song songUntamedDesire5 = new Song("Smoke", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1, artistUntamedDesire3));
        Song songUntamedDesire6 = new Song("Everytime I Come Around", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1, artistUntamedDesire4));
        Song songUntamedDesire7 = new Song("Irregualar Heartbeat", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1, artistUntamedDesire5, artistUntamedDesire4));
        Song songUntamedDesire8 = new Song("I'm a Hustler", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1));
        Song songUntamedDesire9 = new Song("Twisted", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1, artistUntamedDesire6));
        Song songUntamedDesire10 = new Song("Winners Circle", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1, artistUntamedDesire7));
        Song songUntamedDesire11 = new Song("Chase the Paper", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1, artistUntamedDesire8, artistUntamedDesire4, artistUntamedDesire9));

        Album albumUntamedDesire = new Album("Animal Ambition: An Untamed Desire to Win",
                labelUntamedDesire,
                releaseUntamedDesire,
                genreUntamedDesire,
                mbidUntamedDesire,
                Set.of(songUntamedDesire1, songUntamedDesire2, songUntamedDesire3, songUntamedDesire4, songUntamedDesire5, songUntamedDesire6, songUntamedDesire7, songUntamedDesire8, songUntamedDesire9, songUntamedDesire10, songUntamedDesire11));

        albumUntamedDesire.getSongs()
                .forEach(song -> setObjField(song, "albums", Set.of(albumUntamedDesire)));

        //ALBUM 3
        Artist artistUnion1 = new Artist("Rasa");
        String mbidUnion = "da25e5c4-afcb-4f10-af3f-be2251241b35";
        String labelUnion = "Hearts of Space";
        String genreUnion = "ambient, electronic, new age, fusion";
        LocalDate releaseUnion = LocalDate.of(2001, 1, 1);
        Supplier supplier3 = new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(8));

        Song songUnion1 = new Song("Kabe Habe Bolo", labelUnion, releaseUnion, genreUnion, mbidUnion, Duration.ofMinutes(8), Set.of(artistUnion1));
        Song songUnion2 = new Song("Samsara", labelUnion, releaseUnion, genreUnion, mbidUnion, Duration.ofMinutes(7), Set.of(artistUnion1));
        Song songUnion3 = new Song("Hari Haraye", labelUnion, releaseUnion, genreUnion, mbidUnion, Duration.ofMinutes(8), Set.of(artistUnion1));
        Song songUnion4 = new Song("Sri Rupa Manjari", labelUnion, releaseUnion, genreUnion, mbidUnion, Duration.ofMinutes(7), Set.of(artistUnion1));
        Song songUnion5 = new Song("Nrsimha Prayers", labelUnion, releaseUnion, genreUnion, mbidUnion, Duration.ofMinutes(8), Set.of(artistUnion1));
        Song songUnion6 = new Song("Sri Guru", labelUnion, releaseUnion, genreUnion, mbidUnion, Duration.ofMinutes(10), Set.of(artistUnion1));
        Song songUnion7 = new Song("Govindam", labelUnion, releaseUnion, genreUnion, mbidUnion, Duration.ofMinutes(8), Set.of(artistUnion1));

        Album albumUnion = new Album("Union",
                labelUnion,
                releaseUnion,
                genreUnion,
                mbidUnion,
                Set.of(songUnion1, songUnion2, songUnion3, songUnion4, songUnion5, songUnion6, songUnion7));

        albumUnion.getSongs()
                .forEach(song -> setObjField(song, "albums", Set.of(albumUnion)));

        //ALBUM 4
        Artist artistAnti1 = new Artist("Rihanna");

        String mbidAnti = "a84dea70-f81b-4761-a39c-2dd3a9e985cc";
        String labelAnti = "Westbury Road Entertainment";
        String genreAnti = "contemporary r&b, pop, hip hop, soul, trap";
        LocalDate releaseAnti = LocalDate.of(2016, 2, 5);
        Supplier supplier4 = new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(6));

        Song songAnti1 = new Song("Consideration", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(2), Set.of(artistAnti1));
        Song songAnti2 = new Song("James Joint", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(1), Set.of(artistAnti1));
        Song songAnti3 = new Song("Kiss It Better", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(4), Set.of(artistAnti1));
        Song songAnti4 = new Song("Work", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(3), Set.of(artistAnti1, artistLongLive5));
        Song songAnti5 = new Song("Desperado", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(3), Set.of(artistAnti1));
        Song songAnti6 = new Song("Woo", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(3), Set.of(artistAnti1));
        Song songAnti7 = new Song("Needed Me", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(3), Set.of(artistAnti1));
        Song songAnti8 = new Song("Yeah, I Said It", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(2), Set.of(artistAnti1));
        Song songAnti9 = new Song("Same Ol' Mistakes", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(6), Set.of(artistAnti1));
        Song songAnti10 = new Song("Never Ending", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(3), Set.of(artistAnti1));
        Song songAnti11 = new Song("Love on the Brain", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(3), Set.of(artistAnti1));
        Song songAnti12 = new Song("Higher", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(2), Set.of(artistAnti1));
        Song songAnti13 = new Song("Close to You", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(3), Set.of(artistAnti1));

        Album albumAnti = new Album("ANTI",
                labelAnti,
                releaseAnti,
                genreAnti,
                mbidAnti,
                Set.of(songAnti1, songAnti2, songAnti3, songAnti4, songAnti5, songAnti6, songAnti7, songAnti8, songAnti9, songAnti10, songAnti11, songAnti12, songAnti13));

        albumAnti.getSongs()
                .forEach(song -> setObjField(song, "albums", Set.of(albumAnti)));

        //ALBUM 5
        Artist artistLetItBe1 = new Artist("The Beatles");

        String mbidLetItBe = "12ae3a87-0ce7-416c-bb04-a1e3447031fd";
        String labelLetItBe = "Apple Records, Universal Music Group International";
        String genreLetItBe = "rock";
        LocalDate releaseLetItBe = LocalDate.of(2021, 10, 15);
        Supplier supplier5 = new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(1));

        Song songLetItBe1 = new Song("Two of Us", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));
        Song songLetItBe2 = new Song("Dig a Pony", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));
        Song songLetItBe3 = new Song("Across the Universe", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));
        Song songLetItBe4 = new Song("I Me Mine", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(2), Set.of(artistLetItBe1));
        Song songLetItBe5 = new Song("Dig It", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(1), Set.of(artistLetItBe1));
        Song songLetItBe6 = new Song("Let It Be", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(4), Set.of(artistLetItBe1));
        Song songLetItBe7 = new Song("Maggie Mae", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(1), Set.of(artistLetItBe1));
        Song songLetItBe8 = new Song("I've Got a Feeling", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));
        Song songLetItBe9 = new Song("One After 909", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));
        Song songLetItBe10 = new Song("The Long and Winding Road", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));
        Song songLetItBe11 = new Song("For You Blue", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));
        Song songLetItBe12 = new Song("Get Back", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));

        Album albumLetItBe = new Album("Let It Be",
                labelLetItBe,
                releaseLetItBe,
                genreLetItBe,
                mbidLetItBe,
                Set.of(songLetItBe1, songLetItBe2, songLetItBe3, songLetItBe4, songLetItBe5, songLetItBe6, songLetItBe7, songLetItBe8, songLetItBe9, songLetItBe10, songLetItBe11, songLetItBe12));

        albumLetItBe.getSongs()
                .forEach(song -> setObjField(song, "albums", Set.of(albumLetItBe)));

        //ALBUM 6
        Artist artistEverything1 = new Artist("Underground");

        String mbidEverything = "a84dea70-f81b-4761-a39c-2dd3a9e985cc";
        String labelEverything = "Junior Boy's Own";
        String genreEverything = "techno";
        LocalDate releaseEverything = LocalDate.of(2000, 1, 1);
        Supplier supplier6 = new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(1));

        Song songEverything1 = new Song("Juanita / Kiteless", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(12), Set.of(artistEverything1));
        Song songEverything2 = new Song("Cups", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(3), Set.of(artistEverything1));
        Song songEverything3 = new Song("Push Upstairs", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(7), Set.of(artistEverything1));
        Song songEverything4 = new Song("Pearls Girl", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(8), Set.of(artistEverything1));
        Song songEverything5 = new Song("Jumbo", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(8), Set.of(artistEverything1));
        Song songEverything6 = new Song("Shudder / King of Snake", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(12), Set.of(artistEverything1));
        Song songEverything7 = new Song("Born Slippy", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(10), Set.of(artistEverything1));
        Song songEverything8 = new Song("Rez / Cowgirl", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(11), Set.of(artistEverything1));

        Album albumEverything = new Album("Everything, Everything",
                labelEverything,
                releaseEverything,
                genreEverything,
                mbidEverything,
                Set.of(songEverything1, songEverything2, songEverything3, songEverything4, songEverything5, songEverything6, songEverything7, songEverything8));

        albumEverything.getSongs()
                .forEach(song -> setObjField(song, "albums", Set.of(albumEverything)));

        //ALBUM 7
        Artist artistTouchBlue1 = new Artist("Miles Davis");
        Artist artistTouchBlue2 = new Artist("Bill Evans");

        String mbidTouchBlue = "283bae1f-1a6d-4ac9-80d8-68818d8016e3";
        String labelTouchBlue = "RevOla Records";
        String genreTouchBlue = "Jazz";
        LocalDate releaseTouchBlue = LocalDate.of(2021, 1, 1);
        Supplier supplier7 = new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(1));

        Song songTouchBlue1 = new Song("So What?", labelTouchBlue, releaseTouchBlue, genreTouchBlue, mbidTouchBlue, Duration.ofMinutes(9), Set.of(artistTouchBlue1));
        Song songTouchBlue2 = new Song("Freddie Freeloader", labelTouchBlue, releaseTouchBlue, genreTouchBlue, mbidTouchBlue, Duration.ofMinutes(9), Set.of(artistTouchBlue1));
        Song songTouchBlue3 = new Song("Blue In Green", labelTouchBlue, releaseTouchBlue, genreTouchBlue, mbidTouchBlue, Duration.ofMinutes(5), Set.of(artistTouchBlue1, artistTouchBlue2));
        Song songTouchBlue4 = new Song("All Blues", labelTouchBlue, releaseTouchBlue, genreTouchBlue, mbidTouchBlue, Duration.ofMinutes(11), Set.of(artistTouchBlue1));
        Song songTouchBlue5 = new Song("Flamenco Sketches", labelTouchBlue, releaseTouchBlue, genreTouchBlue, mbidTouchBlue, Duration.ofMinutes(9), Set.of(artistTouchBlue1, artistTouchBlue2));
        Song songTouchBlue6 = new Song("Flamenco Sketches (Alternate Take)", labelTouchBlue, releaseTouchBlue, genreTouchBlue, mbidTouchBlue, Duration.ofMinutes(9), Set.of(artistTouchBlue1, artistTouchBlue2));

        Album albumTouchBlue = new Album("A Touch Of Blue",
                labelTouchBlue,
                releaseTouchBlue,
                genreTouchBlue,
                mbidTouchBlue,
                Set.of(songTouchBlue1, songTouchBlue2, songTouchBlue3, songTouchBlue4, songTouchBlue5, songTouchBlue6));

        // create album
        albums.add(albumLongLive);
        albums.add(albumUntamedDesire);
        albums.add(albumUnion);
        albums.add(albumAnti);
        albums.add(albumLetItBe);
        albums.add(albumEverything);
        albums.add(albumTouchBlue);

        // create song
        songs.addAll(albums.stream()
                .map(Album::getSongs)
                .flatMap(Set::stream)
                .collect(Collectors.toSet()));

        // create artist
        artists.addAll(songs.stream()
                .map(Song::getArtists)
                .flatMap(Set::stream)
                .collect(Collectors.toSet()));

        albumTouchBlue.getSongs()
                .forEach(song -> setObjField(song, "albums", Set.of(albumTouchBlue)));

        // create mediums
        Medium mediumLongLive1 = new Medium(BigDecimal.valueOf(12), MediumType.CD, Stock.of(Quantity.of(25)), supplier1, albumLongLive);
        Medium mediumLongLive2 = new Medium(BigDecimal.valueOf(22), MediumType.VINYL, Stock.of(Quantity.of(5)), supplier1, albumLongLive);
        Medium mediumUntamedDesire = new Medium(BigDecimal.valueOf(10), MediumType.CD, Stock.of(Quantity.of(15)), supplier2, albumUntamedDesire);
        Medium mediumUnion = new Medium(BigDecimal.valueOf(29), MediumType.CD, Stock.of(Quantity.of(5)), supplier3, albumUnion);
        Medium mediumAnti = new Medium(BigDecimal.valueOf(13), MediumType.CD, Stock.of(Quantity.of(7)), supplier4, albumAnti);
        Medium mediumLetItBe = new Medium(BigDecimal.valueOf(19), MediumType.CD, Stock.of(Quantity.of(17)), supplier5, albumLetItBe);
        Medium mediumEverything = new Medium(BigDecimal.valueOf(19), MediumType.CD, Stock.of(Quantity.of(17)), supplier6, albumEverything);
        Medium mediumTouchBlue = new Medium(BigDecimal.valueOf(19), MediumType.CD, Stock.of(Quantity.of(17)), supplier7, albumTouchBlue);

        media.addAll(Set.of(mediumLongLive1, mediumLongLive2, mediumUntamedDesire, mediumUnion, mediumAnti, mediumLetItBe, mediumEverything, mediumTouchBlue));

        media.forEach(medium -> setObjField(medium.getAlbum(), "mediums", media.stream()
                .filter(medium1 -> medium1.getAlbum().equals(medium.getAlbum()))
                .collect(Collectors.toSet())));

        // create Topics
        Topic topicAdministrative = new Topic("Administrative");
        Topic topicOrder = new Topic("Order");
        Topic topicHipHop = new Topic("Hip Hop");
        Topic topicPop = new Topic("Pop");
        Topic topicRockNRoll = new Topic("Rock 'n' Roll");
        Topic topicSoul = new Topic("Soul");
        Topic topicJazz = new Topic("Jazz");
        topics.addAll(Set.of(topicAdministrative, topicOrder, topicHipHop, topicPop, topicRockNRoll, topicSoul, topicJazz));

        // create employees
        employees.add(new Employee("lka3333", "Lukas", "Kaufmann", Set.of(UserRole.ADMIN), Set.of(topicAdministrative, topicOrder, topicPop)));
        employees.add(new Employee("ire4657", "Ivo", "Reich", Set.of(UserRole.ADMIN), Set.of(topicAdministrative, topicHipHop, topicSoul)));
        employees.add(new Employee("jgr1585", "Julian", "Grießer", Set.of(UserRole.OPERATOR), Set.of(topicAdministrative, topicOrder, topicRockNRoll)));
        employees.add(new Employee("ssa7090", "Selcan", "Sahin", Set.of(UserRole.SELLER, UserRole.OPERATOR), Set.of(topicAdministrative, topicOrder)));
        employees.add(new Employee("ysa1064", "Yagmur", "Sagdic", Set.of(UserRole.SELLER, UserRole.OPERATOR), Set.of(topicAdministrative, topicHipHop)));
        employees.add(new Employee("bak3400", "Batuhan", "Akkus", Set.of(UserRole.SELLER), Set.of(topicAdministrative, topicHipHop, topicSoul, topicRockNRoll, topicPop, topicJazz)));
        employees.add(new Employee("tf-test", "Thomas", "Feilhauer", Set.of(UserRole.ADMIN), Set.of(topicAdministrative, topicOrder, topicSoul, topicJazz)));

        //create customers
        customers.add(new Customer(1, "JosieS", "5497056331911690"));
        customers.add(new Customer(2, "JonasN", "4412614992618410"));
        customers.add(new Customer(3, "MiroH",  "5425110212013160"));
        customers.add(new Customer(4, "EnnoK", "4149159917878530"));
        customers.add(new Customer(5, "TammeK", "5256110980293610"));
        customers.add(new Customer(6, "EvelinaR", "4541979993294440"));
        customers.add(new Customer(7, "AzadG", "5232969453861870"));
        customers.add(new Customer(8, "SammyB", "4344721596616160"));
        customers.add(new Customer(9, "KatrinK", "5486293928490720"));
        customers.add(new Customer(10, "DanaH",  "5310021062469900"));

        //create suppliers
        suppliers.addAll(media.stream()
                .map(Medium::getSupplier)
                .collect(Collectors.toSet()));

        // create invoices
        invoices.add(Invoice.of(Set.of(new LineItem(Quantity.of(2), mediumLongLive2)), 2));
        invoices.add(Invoice.of(Set.of(new LineItem(Quantity.of(1), mediumLongLive1)), 1));
        invoices.add(Invoice.of(Set.of(new LineItem(Quantity.of(3), mediumUntamedDesire)), 1));
        invoices.add(Invoice.of(Set.of(new LineItem(Quantity.of(1), mediumUnion), new LineItem(Quantity.of(3), mediumEverything)), 10));

        // persists everything
        EntityManager em = PersistenceManager.getEntityManagerInstance();
        em.getTransaction().begin();

        media.forEach(em::persist);
        employees.forEach(em::persist);
        customers.forEach(em::persist);
        invoices.forEach(em::persist);

        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    private static void deleteDatabase() {
        //Read SQL File
        try {
            EntityManager em = PersistenceManager.getEntityManagerInstance();
            BufferedReader sqlFile = new BufferedReader(new InputStreamReader(Objects.requireNonNull(TestGenerator.class.getClassLoader().getResourceAsStream("deleteDatabase.sql"))));
            String line;
            em.getTransaction().begin();

            while ((line = sqlFile.readLine()) != null) {
                em.createNativeQuery(line).executeUpdate();
            }

            em.flush();
            em.getTransaction().commit();
            em.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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


}
