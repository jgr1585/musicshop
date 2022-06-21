package at.fhv.teamd.musicshop.backend;

import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Artist;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.customer.Customer;
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
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

public class TestGenerator {

    private static void generateTestData() {

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

        Song songLongLive1 = new Song(UUID.fromString("43c90862-d38e-4484-9498-bec1d8a43c86"), "Long Live A$AP", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(4), Set.of(artistLongLive1));
        Song songLongLive2 = new Song(UUID.fromString("79ec05ef-8caf-4a42-bea2-0872235d7bca"), "Goldie", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1));
        Song songLongLive3 = new Song(UUID.fromString("3f4763d5-6111-4d67-8bb4-22e4425a2f71"), "PMW (All I Really Need)", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1, artistLongLive2));
        Song songLongLive4 = new Song(UUID.fromString("fdbadc42-47f5-4c1c-9a84-a762b6026756"), "LVL", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1));
        Song songLongLive5 = new Song(UUID.fromString("d20b0c42-2eb4-4f28-84e5-1e396bad971d"), "Hell", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1, artistLongLive3));
        Song songLongLive6 = new Song(UUID.fromString("6a0f9ef0-88a0-4620-aa44-8301ba98d544"), "Pain", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1, artistLongLive4));
        Song songLongLive7 = new Song(UUID.fromString("6a01d6d6-40c4-4738-865e-98103b41f4c2"), "F**kin' Problems", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1, artistLongLive5, artistLongLive6, artistLongLive7));
        Song songLongLive8 = new Song(UUID.fromString("5eeb24db-0014-481d-9cde-6627bf29c01a"), "Wild for the Night", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1, artistLongLive8));
        Song songLongLive9 = new Song(UUID.fromString("e7d6e6d8-5143-4a26-9325-344669717b68"), "Train", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(6), Set.of(artistLongLive1, artistLongLive9, artistLongLive10, artistLongLive11, artistLongLive12, artistLongLive13));
        Song songLongLive10 = new Song(UUID.fromString("3a11d2bc-67e0-4d0d-bd8d-e0ce38769a55"), "Fashion Killa", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1));
        Song songLongLive11 = new Song(UUID.fromString("3822704c-730c-45ef-b1f9-0856729f051e"), "Phoenix", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1));
        Song songLongLive12 = new Song(UUID.fromString("a85e1248-4622-46ca-8e3a-55cb581e3ba4"), "Suddenly", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(4), Set.of(artistLongLive1));
        Song songLongLive13 = new Song(UUID.fromString("8788fd19-2e14-44e3-ac4e-19be333efa96"), "Jodye", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(4), Set.of(artistLongLive1, artistLongLive14, artistLongLive15, artistLongLive16));
        Song songLongLive14 = new Song(UUID.fromString("d89711ce-ddf6-4c1d-ab81-b4ea762b7695"), "Ghetto Symphony", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1, artistLongLive17, artistLongLive18));
        Song songLongLive15 = new Song(UUID.fromString("42248f35-5711-46c2-bf29-19c43eee0524"), "Angels", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1));
        Song songLongLive16 = new Song(UUID.fromString("7685d561-83b1-4fbe-b48f-f8f65891925c"), "I Come Apart", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(3), Set.of(artistLongLive1, artistLongLive19));
        Song songLongLive17 = new Song(UUID.fromString("2df0ce42-dab1-4193-bd12-21dbcca8dc25"), "Purple Swag (remix)", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, Duration.ofMinutes(4), Set.of(artistLongLive1, artistLongLive20, artistLongLive21, artistLongLive22));

        Album albumLongLive = new Album("Long Live A$AP",
                labelLongLive,
                releaseLongLive,
                genreLongLive,
                mbidLongLive,
                Set.of(songLongLive1, songLongLive2, songLongLive3, songLongLive4, songLongLive5, songLongLive6, songLongLive7, songLongLive8, songLongLive9, songLongLive10, songLongLive11, songLongLive12, songLongLive13, songLongLive14, songLongLive15, songLongLive16, songLongLive17));

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

        Song songUntamedDesire1 = new Song(UUID.fromString("f67b07c0-4216-4666-8e6d-c42bb069e0f7"), "Hold On", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1));
        Song songUntamedDesire2 = new Song(UUID.fromString("4b737b91-4e4b-40e8-bf38-0b8cea40040a"), "Don't Worry 'Bout It", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(4), Set.of(artistUntamedDesire1, artistUntamedDesire2));
        Song songUntamedDesire3 = new Song(UUID.fromString("cd6ad0bc-8ad5-45e2-96bd-d1a4f8175e68"), "Animal Ambition", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1));
        Song songUntamedDesire4 = new Song(UUID.fromString("48f3408c-a8e2-49a0-8cd4-4c81c7b47f94"), "Pilot", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1));
        Song songUntamedDesire5 = new Song(UUID.fromString("ba328a2c-e661-449e-9147-b18ba4d5b845"), "Smoke", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1, artistUntamedDesire3));
        Song songUntamedDesire6 = new Song(UUID.fromString("efed41e5-b78b-4d79-9522-07646e8ef34b"), "Everytime I Come Around", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1, artistUntamedDesire4));
        Song songUntamedDesire7 = new Song(UUID.fromString("33226417-e971-4a3a-bc4f-ee3723810419"), "Irregualar Heartbeat", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1, artistUntamedDesire5, artistUntamedDesire4));
        Song songUntamedDesire8 = new Song(UUID.fromString("614cc1cf-91a3-4050-b3f7-64fce0f681b9"), "I'm a Hustler", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1));
        Song songUntamedDesire9 = new Song(UUID.fromString("9ef33109-e867-4034-82d4-1bf21fa3bf8c"), "Twisted", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1, artistUntamedDesire6));
        Song songUntamedDesire10 = new Song(UUID.fromString("a74ba43e-1b5c-40f0-a4e0-9f5f8761f11b"), "Winners Circle", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1, artistUntamedDesire7));
        Song songUntamedDesire11 = new Song(UUID.fromString("362e12cb-ce3a-43fe-8dac-a8a3039c76c6"), "Chase the Paper", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, Duration.ofMinutes(3), Set.of(artistUntamedDesire1, artistUntamedDesire8, artistUntamedDesire4, artistUntamedDesire9));

        Album albumUntamedDesire = new Album("Animal Ambition: An Untamed Desire to Win",
                labelUntamedDesire,
                releaseUntamedDesire,
                genreUntamedDesire,
                mbidUntamedDesire,
                Set.of(songUntamedDesire1, songUntamedDesire2, songUntamedDesire3, songUntamedDesire4, songUntamedDesire5, songUntamedDesire6, songUntamedDesire7, songUntamedDesire8, songUntamedDesire9, songUntamedDesire10, songUntamedDesire11));

        //ALBUM 3
        Artist artistUnion1 = new Artist("Rasa");
        String mbidUnion = "da25e5c4-afcb-4f10-af3f-be2251241b35";
        String labelUnion = "Hearts of Space";
        String genreUnion = "ambient, electronic, new age, fusion";
        LocalDate releaseUnion = LocalDate.of(2001, 1, 1);
        Supplier supplier3 = new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(8));

        Song songUnion1 = new Song(UUID.fromString("18c539ea-77b5-4a1b-89e2-61b475d88f0b"), "Kabe Habe Bolo", labelUnion, releaseUnion, genreUnion, mbidUnion, Duration.ofMinutes(8), Set.of(artistUnion1));
        Song songUnion2 = new Song(UUID.fromString("ccec4e3a-bf36-43d2-bf60-758824dee2d7"), "Samsara", labelUnion, releaseUnion, genreUnion, mbidUnion, Duration.ofMinutes(7), Set.of(artistUnion1));
        Song songUnion3 = new Song(UUID.fromString("4a7cb6a5-2864-4110-ba06-ed7d9a92011c"), "Hari Haraye", labelUnion, releaseUnion, genreUnion, mbidUnion, Duration.ofMinutes(8), Set.of(artistUnion1));
        Song songUnion4 = new Song(UUID.fromString("46030470-f984-41b0-9ba3-c448f702c227"), "Sri Rupa Manjari", labelUnion, releaseUnion, genreUnion, mbidUnion, Duration.ofMinutes(7), Set.of(artistUnion1));
        Song songUnion5 = new Song(UUID.fromString("abeef724-b9cf-4d5d-bc24-6d71932d0288"), "Nrsimha Prayers", labelUnion, releaseUnion, genreUnion, mbidUnion, Duration.ofMinutes(8), Set.of(artistUnion1));
        Song songUnion6 = new Song(UUID.fromString("d8a9ecce-3847-4a83-8e81-d8cd45cca5a7"), "Sri Guru", labelUnion, releaseUnion, genreUnion, mbidUnion, Duration.ofMinutes(10), Set.of(artistUnion1));
        Song songUnion7 = new Song(UUID.fromString("292dfffc-c0e1-4214-90dc-bfac99518bbc"), "Govindam", labelUnion, releaseUnion, genreUnion, mbidUnion, Duration.ofMinutes(8), Set.of(artistUnion1));

        Album albumUnion = new Album("Union",
                labelUnion,
                releaseUnion,
                genreUnion,
                mbidUnion,
                Set.of(songUnion1, songUnion2, songUnion3, songUnion4, songUnion5, songUnion6, songUnion7));

        //ALBUM 4
        Artist artistAnti1 = new Artist("Rihanna");
        Artist artistAnti2 = new Artist("SZA");

        String mbidAnti = "a84dea70-f81b-4761-a39c-2dd3a9e985cc";
        String labelAnti = "Westbury Road Entertainment";
        String genreAnti = "contemporary r&b, pop, hip hop, soul, trap";
        LocalDate releaseAnti = LocalDate.of(2016, 2, 5);
        Supplier supplier4 = new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(6));

        Song songAnti1 = new Song(UUID.fromString("3e3a96c3-50c8-4d7a-8cf3-aa733d30d955"), "Consideration", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(2), Set.of(artistAnti1));
        Song songAnti2 = new Song(UUID.fromString("a38e35c3-1f9c-4963-aa6e-e1d8887827b2"), "James Joint", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(1), Set.of(artistAnti1));
        Song songAnti3 = new Song(UUID.fromString("85460385-55fa-419d-a07d-d909f4dc2890"), "Kiss It Better", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(4), Set.of(artistAnti1));
        Song songAnti4 = new Song(UUID.fromString("c71aae8a-cafb-4bb8-99b7-d5d00412c1cf"), "Work", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(3), Set.of(artistAnti1, artistLongLive5));
        Song songAnti5 = new Song(UUID.fromString("5c62aad9-3d4c-44ef-b28f-9f6a7c217fd0"), "Desperado", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(3), Set.of(artistAnti1));
        Song songAnti6 = new Song(UUID.fromString("6e92459d-6a58-41ad-a71b-a5403e15eff5"), "Woo", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(3), Set.of(artistAnti1));
        Song songAnti7 = new Song(UUID.fromString("35041cb6-c76c-4aa0-9512-7b84086df945"), "Needed Me", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(3), Set.of(artistAnti1));
        Song songAnti8 = new Song(UUID.fromString("2b3e4dcc-f47c-4cfa-bd49-b8157347098a"), "Yeah, I Said It", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(2), Set.of(artistAnti1));
        Song songAnti9 = new Song(UUID.fromString("d375bc12-b6f8-4f7f-a094-cb292a2612aa"), "Same Ol' Mistakes", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(6), Set.of(artistAnti1));
        Song songAnti10 = new Song(UUID.fromString("208517ad-c5bc-4b30-b636-dcab14c676b3"), "Never Ending", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(3), Set.of(artistAnti1));
        Song songAnti11 = new Song(UUID.fromString("f97a5050-28b0-4518-a08d-89ef44349181"), "Love on the Brain", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(3), Set.of(artistAnti1));
        Song songAnti12 = new Song(UUID.fromString("9ea33c8a-3054-488b-8de5-5262d7928876"), "Higher", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(2), Set.of(artistAnti1));
        Song songAnti13 = new Song(UUID.fromString("5fde9c18-2c60-4d22-bce3-ac5c26cbbaee"), "Close to You", labelAnti, releaseAnti, genreAnti, mbidAnti, Duration.ofMinutes(3), Set.of(artistAnti1));

        Album albumAnti = new Album("ANTI",
                labelAnti,
                releaseAnti,
                genreAnti,
                mbidAnti,
                Set.of(songAnti1, songAnti2, songAnti3, songAnti4, songAnti5, songAnti6, songAnti7, songAnti8, songAnti9, songAnti10, songAnti11, songAnti12, songAnti13));

        //ALBUM 5
        Artist artistLetItBe1 = new Artist("The Beatles");

        String mbidLetItBe = "12ae3a87-0ce7-416c-bb04-a1e3447031fd";
        String labelLetItBe = "Apple Records, Universal Music Group International";
        String genreLetItBe = "rock";
        LocalDate releaseLetItBe = LocalDate.of(2021, 10, 15);
        Supplier supplier5 = new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(1));

        Song songLetItBe1 = new Song(UUID.fromString("92d82b51-8346-44df-855d-f2506cd46e4b"), "Two of Us", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));
        Song songLetItBe2 = new Song(UUID.fromString("845b95af-c2bb-4b7c-8d0f-509ba545bfbb"), "Dig a Pony", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));
        Song songLetItBe3 = new Song(UUID.fromString("bd7fcc5d-a300-49dd-b084-a9e332241ad6"), "Across the Universe", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));
        Song songLetItBe4 = new Song(UUID.fromString("7fe2e70f-8a34-4840-a5a8-990e0b579b5a"), "I Me Mine", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(2), Set.of(artistLetItBe1));
        Song songLetItBe5 = new Song(UUID.fromString("17a0dd9d-91f2-4d9c-8d6e-b45c9307f36d"), "Dig It", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(1), Set.of(artistLetItBe1));
        Song songLetItBe6 = new Song(UUID.fromString("aa144d56-0fbc-494e-90b2-41d7d3ee57ec"), "Let It Be", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(4), Set.of(artistLetItBe1));
        Song songLetItBe7 = new Song(UUID.fromString("e9c88c7a-fd70-432e-8b59-dbe590f8b210"), "Maggie Mae", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(1), Set.of(artistLetItBe1));
        Song songLetItBe8 = new Song(UUID.fromString("5491c4e8-68c9-4311-9bb0-46dc13b345b5"), "I've Got a Feeling", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));
        Song songLetItBe9 = new Song(UUID.fromString("0d373c5e-41f2-4afd-8bd0-a46b0a4fec50"), "One After 909", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));
        Song songLetItBe10 = new Song(UUID.fromString("5afc4721-999a-4a7f-b4ce-b22012e3a0f6"), "The Long and Winding Road", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));
        Song songLetItBe11 = new Song(UUID.fromString("b234957c-c44a-4536-8f97-59585ce966c6"), "For You Blue", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));
        Song songLetItBe12 = new Song(UUID.fromString("53865e80-e1a1-4279-8969-4c7a4a687b2d"), "Get Back", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, Duration.ofMinutes(3), Set.of(artistLetItBe1));

        Album albumLetItBe = new Album("Let It Be",
                labelLetItBe,
                releaseLetItBe,
                genreLetItBe,
                mbidLetItBe,
                Set.of(songLetItBe1, songLetItBe2, songLetItBe3, songLetItBe4, songLetItBe5, songLetItBe6, songLetItBe7, songLetItBe8, songLetItBe9, songLetItBe10, songLetItBe11, songLetItBe12));

        //ALBUM 6
        Artist artistEverything1 = new Artist("Underground");

        String mbidEverything = "a84dea70-f81b-4761-a39c-2dd3a9e985cc";
        String labelEverything = "Junior Boy's Own";
        String genreEverything = "techno";
        LocalDate releaseEverything = LocalDate.of(2000, 1, 1);
        Supplier supplier6 = new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(1));

        Song songEverything1 = new Song(UUID.fromString("7e9b6258-ca60-4f82-b0de-23482de97a27"), "Juanita / Kiteless", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(12), Set.of(artistEverything1));
        Song songEverything2 = new Song(UUID.fromString("dc034b11-4b08-4dd6-823b-f34b509bc24a"), "Cups", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(3), Set.of(artistEverything1));
        Song songEverything3 = new Song(UUID.fromString("d30fb839-60c1-495c-b305-1f4014744899"), "Push Upstairs", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(7), Set.of(artistEverything1));
        Song songEverything4 = new Song(UUID.fromString("b6822832-399d-42a8-889e-796a4b5d31a2"), "Pearls Girl", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(8), Set.of(artistEverything1));
        Song songEverything5 = new Song(UUID.fromString("53b29bfc-d447-4995-9df1-d33549e6ffd5"), "Jumbo", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(8), Set.of(artistEverything1));
        Song songEverything6 = new Song(UUID.fromString("7c822d8c-d443-4cb3-bf45-3a3bc3921d72"), "Shudder / King of Snake", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(12), Set.of(artistEverything1));
        Song songEverything7 = new Song(UUID.fromString("a5ba8b4d-41d7-4349-bb37-d72787fb6b14"), "Born Slippy", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(10), Set.of(artistEverything1));
        Song songEverything8 = new Song(UUID.fromString("c5d672ea-4a2c-48ca-a056-89d4f732a4be"), "Rez / Cowgirl", labelEverything, releaseEverything, genreEverything, mbidEverything, Duration.ofMinutes(11), Set.of(artistEverything1));

        Album albumEverything = new Album("Everything, Everything",
                labelEverything,
                releaseEverything,
                genreEverything,
                mbidEverything,
                Set.of(songEverything1, songEverything2, songEverything3, songEverything4, songEverything5, songEverything6, songEverything7, songEverything8));

        //ALBUM 7
        Artist artistTouchBlue1 = new Artist("Miles Davis");
        Artist artistTouchBlue2 = new Artist("Bill Evans");

        String mbidTouchBlue = "eae41864-ecea-4c39-8fe9-2dabd150acfa";
        String labelTouchBlue = "RevOla Records";
        String genreTouchBlue = "Jazz";
        LocalDate releaseTouchBlue = LocalDate.of(2021, 1, 1);
        Supplier supplier7 = new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(1));

        Song songTouchBlue1 = new Song(UUID.fromString("8c5e6415-8321-4faa-bfea-f6ba26f1abf2"), "So What?", labelTouchBlue, releaseTouchBlue, genreTouchBlue, mbidTouchBlue, Duration.ofMinutes(9), Set.of(artistTouchBlue1));
        Song songTouchBlue2 = new Song(UUID.fromString("db5982ab-5d80-445c-9b87-c5a7a320097c"), "Freddie Freeloader", labelTouchBlue, releaseTouchBlue, genreTouchBlue, mbidTouchBlue, Duration.ofMinutes(9), Set.of(artistTouchBlue1));
        Song songTouchBlue3 = new Song(UUID.fromString("e5fe513d-a8a5-4e47-a078-ee1e5f6a1101"), "Blue In Green", labelTouchBlue, releaseTouchBlue, genreTouchBlue, mbidTouchBlue, Duration.ofMinutes(5), Set.of(artistTouchBlue1, artistTouchBlue2));
        Song songTouchBlue4 = new Song(UUID.fromString("e9a4cec7-64f5-4f6d-843f-dca1398d9d2c"), "All Blues", labelTouchBlue, releaseTouchBlue, genreTouchBlue, mbidTouchBlue, Duration.ofMinutes(11), Set.of(artistTouchBlue1));
        Song songTouchBlue5 = new Song(UUID.fromString("5745e51b-0a96-416b-a08a-a996e56f176a"), "Flamenco Sketches", labelTouchBlue, releaseTouchBlue, genreTouchBlue, mbidTouchBlue, Duration.ofMinutes(9), Set.of(artistTouchBlue1, artistTouchBlue2));
        Song songTouchBlue6 = new Song(UUID.fromString("2e6ddf39-d531-4972-aea4-f34ad1ae96b0"), "Flamenco Sketches (Alternate Take)", labelTouchBlue, releaseTouchBlue, genreTouchBlue, mbidTouchBlue, Duration.ofMinutes(9), Set.of(artistTouchBlue1, artistTouchBlue2));

        Album albumTouchBlue = new Album("A Touch Of Blue",
                labelTouchBlue,
                releaseTouchBlue,
                genreTouchBlue,
                mbidTouchBlue,
                Set.of(songTouchBlue1, songTouchBlue2, songTouchBlue3, songTouchBlue4, songTouchBlue5, songTouchBlue6));

        // create mediums
        Set<Medium> mediums = new LinkedHashSet<>();

        // analog mediums
        mediums.add(new Medium(BigDecimal.valueOf(12), MediumType.CD, Stock.of(Quantity.of(25)), supplier1, albumLongLive));
        mediums.add(new Medium(BigDecimal.valueOf(22), MediumType.VINYL, Stock.of(Quantity.of(5)), supplier1, albumLongLive));
        mediums.add(new Medium(BigDecimal.valueOf(10), MediumType.CD, Stock.of(Quantity.of(15)), supplier2, albumUntamedDesire));
        mediums.add(new Medium(BigDecimal.valueOf(29), MediumType.CD, Stock.of(Quantity.of(5)), supplier3, albumUnion));
        mediums.add(new Medium(BigDecimal.valueOf(13), MediumType.CD, Stock.of(Quantity.of(7)), supplier4, albumAnti));
        mediums.add(new Medium(BigDecimal.valueOf(19), MediumType.CD, Stock.of(Quantity.of(17)), supplier5, albumLetItBe));
        mediums.add(new Medium(BigDecimal.valueOf(19), MediumType.CD, Stock.of(Quantity.of(17)), supplier6, albumEverything));
        mediums.add(new Medium(BigDecimal.valueOf(19), MediumType.CD, Stock.of(Quantity.of(17)), supplier7, albumTouchBlue));

        // digital mediums
        mediums.add(new Medium(BigDecimal.valueOf(22), MediumType.DIGITAL, Stock.of(Quantity.of(1)), supplier1, albumLongLive));
        mediums.add(new Medium(BigDecimal.valueOf(10), MediumType.DIGITAL, Stock.of(Quantity.of(1)), supplier2, albumUntamedDesire));
        mediums.add(new Medium(BigDecimal.valueOf(29), MediumType.DIGITAL, Stock.of(Quantity.of(1)), supplier3, albumUnion));
        mediums.add(new Medium(BigDecimal.valueOf(13), MediumType.DIGITAL, Stock.of(Quantity.of(1)), supplier4, albumAnti));
        mediums.add(new Medium(BigDecimal.valueOf(19), MediumType.DIGITAL, Stock.of(Quantity.of(1)), supplier5, albumLetItBe));
        mediums.add(new Medium(BigDecimal.valueOf(19), MediumType.DIGITAL, Stock.of(Quantity.of(1)), supplier6, albumEverything));
        mediums.add(new Medium(BigDecimal.valueOf(19), MediumType.DIGITAL, Stock.of(Quantity.of(1)), supplier7, albumTouchBlue));

        // create Topics
        Topic topicAdministrative = new Topic("Administrative");
        Topic topicOrder = new Topic("Order");
        Topic topicHipHop = new Topic("Hip Hop");
        Topic topicPop = new Topic("Pop");
        Topic topicRockNRoll = new Topic("Rock 'n' Roll");
        Topic topicSoul = new Topic("Soul");
        Topic topicJazz = new Topic("Jazz");

        // create employees
        Set<Employee> employees = new LinkedHashSet<>();

        employees.add(new Employee("lka3333", "Lukas", "Kaufmann", Set.of(UserRole.ADMIN), Set.of(topicAdministrative, topicOrder, topicPop)));
        employees.add(new Employee("ire4657", "Ivo", "Reich", Set.of(UserRole.ADMIN), Set.of(topicAdministrative, topicHipHop, topicSoul)));
        employees.add(new Employee("jgr1585", "Julian", "Grießer", Set.of(UserRole.OPERATOR), Set.of(topicAdministrative, topicOrder, topicRockNRoll)));
        employees.add(new Employee("ssa7090", "Selcan", "Sahin", Set.of(UserRole.SELLER, UserRole.OPERATOR), Set.of(topicAdministrative, topicOrder)));
        employees.add(new Employee("ysa1064", "Yagmur", "Sagdic", Set.of(UserRole.SELLER, UserRole.OPERATOR), Set.of(topicAdministrative, topicHipHop)));
        employees.add(new Employee("bak3400", "Batuhan", "Akkus", Set.of(UserRole.SELLER), Set.of(topicAdministrative, topicHipHop, topicSoul, topicRockNRoll, topicPop, topicJazz)));
        employees.add(new Employee("tf-test", "Thomas", "Feilhauer", Set.of(UserRole.ADMIN), Set.of(topicAdministrative, topicOrder, topicSoul, topicJazz)));
        employees.add(new Employee("BACKDOOR-AUTH", "", "", Set.of(UserRole.ADMIN), Set.of(topicAdministrative, topicOrder, topicHipHop, topicPop, topicRockNRoll, topicSoul, topicJazz)));

        // create customers
        Set<Customer> customers = new HashSet<>();

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
        customers.add(new Customer(11, "test",  "1"));
        customers.add(new Customer(12, "test1",  "1"));

        Set<LineItem> lineItems = new HashSet<>();
        lineItems.add(new LineItem(Quantity.of(2), mediums.iterator().next()));
        Invoice invoice = Invoice.of(
                lineItems,
                customers.stream().filter(c -> c.getUserName().equals("JosieS")).findFirst().get().getCustomerNo());

        // persists everything
        EntityManager em = PersistenceManager.getEntityManagerInstance();
        em.getTransaction().begin();

        mediums.forEach(em::persist);
        employees.forEach(em::persist);
        customers.forEach(em::persist);
        em.persist(invoice);

        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public static void main(String[] args) {
        generateTestData();
    }
}
