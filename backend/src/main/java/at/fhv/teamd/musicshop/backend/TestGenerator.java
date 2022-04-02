package at.fhv.teamd.musicshop.backend;

import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Artist;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.medium.Stock;
import at.fhv.teamd.musicshop.backend.domain.medium.Supplier;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

public class TestGenerator {

    // TODO: Test data generation with real data -> Articles, Artists, Mediums, Suppliers
    private static void generateTestData() {
        EntityManager em = PersistenceManager.getEntityManagerInstance();
        em.getTransaction().begin();

        // ALBUM 1
        Set<Medium> mediums1 = new HashSet<>();
        mediums1.add(new Medium(BigDecimal.valueOf(12), MediumType.CD, Stock.of(Quantity.of(25)), new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(3))));

        Artist artistLongLive = new Artist("A$AP Rocky");

        String mbidLongLive = "ab967b6a-de5c-455b-aad2-8b2f5899b288";
        String labelLongLive = "Sony Records International";
        String genreLongLive = "hip hop, dubstep, east coast hip hop, underground hip hop";
        String descriptorNameLongLive = "";
        LocalDate releaseLongLive = LocalDate.of(2013, 10,23);

        Song songLongLive = new Song("Long Live A$AP", labelLongLive, releaseLongLive, genreLongLive, "", mbidLongLive, mediums1, Duration.ofMinutes(4), Set.of(artistLongLive));
        Album albumLongLive = new Album("Long Live A$AP",
                labelLongLive,
                releaseLongLive,
                genreLongLive,
                mbidLongLive,
                descriptorNameLongLive,
                mediums1,
                Set.of(songLongLive));

        em.persist(albumLongLive);

        //ALBUM 2
        Set<Medium> mediums2 = new HashSet<>();
        mediums2.add(new Medium(BigDecimal.valueOf(10), MediumType.CD, Stock.of(Quantity.of(15)), new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(7))));

        Artist artistUntamedDesire = new Artist("50 Cent");

        String mbidUntamedDesire = "b4cfbcbc-b98c-4695-94c9-e095412e4a84";
        String labelUntamedDesire = "G-Unit Records";
        String genreUntamedDesire= "hip hop, rap";
        LocalDate releaseUntamedDesire = LocalDate.of(2014, 6,3);

        Song songUntamedDesire = new Song("Hold On", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, "", mbidUntamedDesire, mediums2, Duration.ofMinutes(3), Set.of(artistUntamedDesire));
        Album albumUntamedDesire = new Album("Animal Ambition: An Untamed Desire to Win",
                labelUntamedDesire,
                releaseUntamedDesire,
                genreUntamedDesire,
                mbidUntamedDesire,
                "",
                mediums2,
                Set.of(songUntamedDesire));

        em.persist(albumUntamedDesire);

        //ALBUM 3
        Set<Medium> mediums3 = new HashSet<>();
        mediums3.add(new Medium(BigDecimal.valueOf(29), MediumType.CD, Stock.of(Quantity.of(5)), new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(8))));

        Artist artistUnion = new Artist("Rasa");

        String mbidUnion = "da25e5c4-afcb-4f10-af3f-be2251241b35";
        String labelUnion = "Hearts of Space";
        String genreUnion = "ambient, electronic, new age, fusion";
        LocalDate releaseUnion = LocalDate.of(2001, 1,1);

        Song songUnion = new Song("Kabe Habe Bolo", labelUnion, releaseUnion, genreUnion, "", mbidUnion, mediums1, Duration.ofMinutes(8), Set.of(artistUnion));
        Album albumUnion = new Album("Union",
                labelUnion,
                releaseUnion,
                genreUnion,
                "",
                mbidUnion,
                mediums3,
                Set.of(songUnion));

        em.persist(albumUnion);

        //ALBUM 4
        Set<Medium> mediums4 = new HashSet<>();
        mediums4.add(new Medium(BigDecimal.valueOf(19), MediumType.CD, Stock.of(Quantity.of(17)), new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(1))));

        Artist artistAnti = new Artist("Rihanna");

        String mbidAnti = "a84dea70-f81b-4761-a39c-2dd3a9e985cc";
        String labelAnti = "Westbury Road Entertainment";
        String genreAnti = "contemporary r&b, pop, hip hop, soul, trap";
        LocalDate releaseAnti = LocalDate.of(2016, 2,5);

        Song songAnti = new Song("Consideration", labelAnti, releaseAnti, genreAnti, "", mbidAnti, mediums4, Duration.ofMinutes(2), Set.of(artistAnti));

        Album albumAnti = new Album("ANTI",
                labelAnti,
                releaseAnti,
                genreAnti,
                "",
                mbidAnti,
                mediums4,
                Set.of(songAnti));

        em.persist(albumAnti);
//
//        //ALBUM 5
//        Artist artistLetItBe = new Artist("The Beatles");
//
//        em.persist(artistLetItBe);
//
//        String mbidLetItBe = "12ae3a87-0ce7-416c-bb04-a1e3447031fd";
//        String labelLetItBe = "Apple Records, Universal Music Group International";
//        String genreLetItBe = "rock";
//        LocalDate releaseLetItBe = LocalDate.of(2021, 10,15);
//
//        Song songLetItBe = new Song("Two of Us", labelLetItBe, releaseLetItBe, genreLetItBe, mbidLetItBe, analogMediumMap5, Duration.ofMinutes(3), List.of(artistLetItBe));
//
//        Album albumLetItBe = new Album("Let It Be",
//                labelLetItBe,
//                releaseLetItBe,
//                genreLetItBe,
//                mbidLetItBe,
//                analogMediumMap5,
//                List.of(songLetItBe));
//
//        em.persist(songLetItBe);
//        em.persist(albumLetItBe);
//
//        //ALBUM 6
//        Artist artistEverything = new Artist("Underground");
//
//        em.persist(artistEverything);
//
//        String mbidEverything = "a84dea70-f81b-4761-a39c-2dd3a9e985cc";
//        String labelEverything = "Junior Boy's Own";
//        String genreEverything = "techno";
//        LocalDate releaseEverything = LocalDate.of(2000, 1,1);
//
//        Song songEverything = new Song("Juanita / Kiteless", labelEverything, releaseEverything, genreEverything, mbidEverything, analogMediumMap6, Duration.ofMinutes(12), List.of(artistEverything));
//
//        Album albumEverything = new Album("Everything, Everything",
//                labelEverything,
//                releaseEverything,
//                genreEverything,
//                mbidEverything,
//                analogMediumMap6,
//                List.of(songEverything));
//
//        em.persist(songEverything);
//        em.persist(albumEverything);
//
//        //ALBUM 7
//        Artist artistTouchBlue = new Artist("Miles Davis");
//
//        em.persist(artistTouchBlue);
//
//        String mbidTouchBlue = "283bae1f-1a6d-4ac9-80d8-68818d8016e3";
//        String labelTouchBlue = "RevOla Records";
//        String genreTouchBlue = "Jazz";
//        LocalDate releaseTouchBlue = LocalDate.of(2021, 1,1);
//
//        Song songTouchBlue = new Song("So What?", labelTouchBlue, releaseTouchBlue, genreTouchBlue, mbidTouchBlue, analogMediumMap7, Duration.ofMinutes(9), List.of(artistTouchBlue));
//
//        Album albumTouchBlue = new Album("A Touch Of Blue",
//                labelTouchBlue,
//                releaseTouchBlue,
//                genreTouchBlue,
//                mbidTouchBlue,
//                analogMediumMap7,
//                List.of(songTouchBlue));
//
//        em.persist(songTouchBlue);
//        em.persist(albumTouchBlue);

        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public static void main(String[] args) {
        generateTestData();
    }
}
