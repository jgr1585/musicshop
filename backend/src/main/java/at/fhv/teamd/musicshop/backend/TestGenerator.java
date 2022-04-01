//package at.fhv.teamd.musicshop.backend;
//
//import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
//import at.fhv.teamd.musicshop.backend.domain.Quantity;
//import at.fhv.teamd.musicshop.backend.domain.article.Album;
//import at.fhv.teamd.musicshop.backend.domain.article.Artist;
//import at.fhv.teamd.musicshop.backend.domain.article.Song;
//import at.fhv.teamd.musicshop.backend.domain.medium.AnalogMedium;
//import at.fhv.teamd.musicshop.backend.domain.medium.AnalogMediumType;
//import at.fhv.teamd.musicshop.backend.domain.medium.Stock;
//import at.fhv.teamd.musicshop.backend.domain.medium.Supplier;
//
//import javax.persistence.EntityManager;
//import java.math.BigDecimal;
//import java.time.Duration;
//import java.time.LocalDate;
//import java.util.*;
//
//public class TestGenerator {
//
//    // TODO: Test data generation with real data -> Articles, Artists, Mediums, Suppliers
//    private static void generateTestData() {
//        EntityManager em = PersistenceManager.getEntityManagerInstance();
//        em.getTransaction().begin();
//
//        Map<AnalogMediumType, AnalogMedium> analogMediumMap1 = new HashMap<>();
//        AnalogMedium analogMedium1 = new AnalogMedium(BigDecimal.valueOf(12), new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(3)), AnalogMediumType.CD, Stock.of(Quantity.of(25)));
//        analogMediumMap1.put(AnalogMediumType.CD, analogMedium1);
//        analogMediumMap1.values().forEach(em::persist);
//
//        Map<AnalogMediumType, AnalogMedium> analogMediumMap2 = new HashMap<>();
//        AnalogMedium analogMedium2 = new AnalogMedium(BigDecimal.valueOf(15), new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(8)), AnalogMediumType.CD, Stock.of(Quantity.of(20)));
//        analogMediumMap2.put(AnalogMediumType.CD, analogMedium2);
//        analogMediumMap2.values().forEach(em::persist);
//
//        Map<AnalogMediumType, AnalogMedium> analogMediumMap3 = new HashMap<>();
//        AnalogMedium analogMedium3 = new AnalogMedium(BigDecimal.valueOf(18), new Supplier("ZYX Music GmbH & Co KG", Duration.ofDays(4)), AnalogMediumType.CD, Stock.of(Quantity.of(25)));
//        analogMediumMap3.put(AnalogMediumType.CD, analogMedium3);
//        analogMediumMap3.values().forEach(em::persist);
//
//        Map<AnalogMediumType, AnalogMedium> analogMediumMap4 = new HashMap<>();
//        AnalogMedium analogMedium4 = new AnalogMedium(BigDecimal.valueOf(20), new Supplier("Walding-Sound Gmbh", Duration.ofDays(6)), AnalogMediumType.CD, Stock.of(Quantity.of(50)));
//        analogMediumMap4.put(AnalogMediumType.CD, analogMedium4);
//        analogMediumMap4.values().forEach(em::persist);
//
//        Map<AnalogMediumType, AnalogMedium> analogMediumMap5 = new HashMap<>();
//        AnalogMedium analogMedium5 = new AnalogMedium(BigDecimal.valueOf(22), new Supplier("Walding-Sound Gmbh", Duration.ofDays(6)), AnalogMediumType.CD, Stock.of(Quantity.of(45)));
//        analogMediumMap5.put(AnalogMediumType.CD, analogMedium5);
//        analogMediumMap5.values().forEach(em::persist);
//
//        Map<AnalogMediumType, AnalogMedium> analogMediumMap6 = new HashMap<>();
//        AnalogMedium analogMedium6 = new AnalogMedium(BigDecimal.valueOf(15), new Supplier("Adcom Production AG", Duration.ofDays(2)), AnalogMediumType.CD, Stock.of(Quantity.of(10)));
//        analogMediumMap6.put(AnalogMediumType.CD, analogMedium6);
//        analogMediumMap6.values().forEach(em::persist);
//
//        Map<AnalogMediumType, AnalogMedium> analogMediumMap7 = new HashMap<>();
//        AnalogMedium analogMedium7 = new AnalogMedium(BigDecimal.valueOf(30), new Supplier("Adcom Production AG", Duration.ofDays(7)), AnalogMediumType.CD, Stock.of(Quantity.of(0)));
//        analogMediumMap7.put(AnalogMediumType.CD, analogMedium7);
//        analogMediumMap7.values().forEach(em::persist);
//
//        //ALBUM 1
//        Artist artistLongLive = new Artist("A$AP Rocky");
//
//        em.persist(artistLongLive);
//
//        String mbidLongLive = "ab967b6a-de5c-455b-aad2-8b2f5899b288";
//        String labelLongLive = "Sony Records International";
//        String genreLongLive = "hip hop, dubstep, east coast hip hop, underground hip hop";
//        LocalDate releaseLongLive = LocalDate.of(2013, 10,23);
//
//        Song songLongLive = new Song("Long Live A$AP", labelLongLive, releaseLongLive, genreLongLive, mbidLongLive, analogMediumMap1, Duration.ofMinutes(4), List.of(artistLongLive));
//        Album albumLongLive = new Album("Long Live A$AP",
//                labelLongLive,
//                releaseLongLive,
//                genreLongLive,
//                mbidLongLive,
//                analogMediumMap1,
//                List.of(songLongLive));
//
//        em.persist(songLongLive);
//
//        em.persist(albumLongLive);
//
//        //ALBUM 2
//        Artist artistUntamedDesire = new Artist("50 Cent");
//
//        em.persist(artistUntamedDesire);
//
//        String mbidUntamedDesire = "b4cfbcbc-b98c-4695-94c9-e095412e4a84";
//        String labelUntamedDesire = "G-Unit Records";
//        String genreUntamedDesire= "hip hop, rap";
//        LocalDate releaseUntamedDesire = LocalDate.of(2014, 6,3);
//
//        Song songUntamedDesire = new Song("Hold On", labelUntamedDesire, releaseUntamedDesire, genreUntamedDesire, mbidUntamedDesire, analogMediumMap2, Duration.ofMinutes(3), List.of(artistUntamedDesire));
//        Album albumUntamedDesire = new Album("Animal Ambition: An Untamed Desire to Win",
//                labelUntamedDesire,
//                releaseUntamedDesire,
//                genreUntamedDesire,
//                mbidUntamedDesire,
//                analogMediumMap2,
//                List.of(songUntamedDesire));
//
//        em.persist(songUntamedDesire);
//        em.persist(albumUntamedDesire);
//
//        //ALBUM 3
//        Artist artistUnion = new Artist("Rasa");
//
//        em.persist(artistUnion);
//
//        String mbidUnion = "da25e5c4-afcb-4f10-af3f-be2251241b35";
//        String labelUnion = "Hearts of Space";
//        String genreUnion = "ambient, electronic, new age, fusion";
//        LocalDate releaseUnion = LocalDate.of(2001, 1,1);
//
//        Song songUnion = new Song("Kabe Habe Bolo", labelUnion, releaseUnion, genreUnion, mbidUnion, analogMediumMap3, Duration.ofMinutes(8), List.of(artistUnion));
//
//        Album albumUnion = new Album("Union",
//                labelUnion,
//                releaseUnion,
//                genreUnion,
//                mbidUnion,
//                analogMediumMap3,
//                List.of(songUnion));
//
//        em.persist(songUnion);
//        em.persist(albumUnion);
//
//        //ALBUM 4
//        Artist artistAnti = new Artist("Rihanna");
//
//        em.persist(artistAnti);
//
//        String mbidAnti = "a84dea70-f81b-4761-a39c-2dd3a9e985cc";
//        String labelAnti = "Westbury Road Entertainment";
//        String genreAnti = "contemporary r&b, pop, hip hop, soul, trap";
//        LocalDate releaseAnti = LocalDate.of(2016, 2,5);
//
//        Song songAnti = new Song("Consideration", labelAnti, releaseAnti, genreAnti, mbidAnti, analogMediumMap4, Duration.ofMinutes(2), List.of(artistAnti));
//
//        Album albumAnti = new Album("ANTI",
//                labelAnti,
//                releaseAnti,
//                genreAnti,
//                mbidAnti,
//                analogMediumMap4,
//                List.of(songAnti));
//
//        em.persist(songAnti);
//        em.persist(albumAnti);
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
//
//        em.flush();
//        em.getTransaction().commit();
//        em.close();
//    }
//
//    public static void main(String[] args) {
//        generateTestData();
//    }
//}
