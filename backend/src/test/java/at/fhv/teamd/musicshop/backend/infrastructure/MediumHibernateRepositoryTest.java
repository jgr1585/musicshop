package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.Stock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class MediumHibernateRepositoryTest {

    MediumHibernateRepository mediumHibernateRepository;

    @BeforeEach
    public void init() {
        this.mediumHibernateRepository = new MediumHibernateRepository();

        BaseRepositoryData.init();
    }

    @Test
    void given_id_when_findMediumById_returnMedium() {
        //given
        Set<Medium> media = BaseRepositoryData.getMedia();
        long id = media.iterator().next().getId();

        Optional<Medium> exceptedMedia = media.stream()
                .filter(medium -> medium.getId() == id)
                .findFirst();

        //when
        Optional<Medium> actualMedia = this.mediumHibernateRepository.findMediumById(id);

        //then
        Assertions.assertEquals(exceptedMedia, actualMedia);
    }

    @Test
    void given_nonExistingId_when_findMediumById_returnEmpty() {
        //given
        Set<Medium> media = BaseRepositoryData.getMedia();
        long id = media.stream()
                .mapToLong(Medium::getId)
                .max().orElseThrow() + 1;

        Optional<Medium> exceptedMedia = Optional.empty();

        //when
        Optional<Medium> actualMedia = this.mediumHibernateRepository.findMediumById(id);

        //then
        Assertions.assertEquals(exceptedMedia, actualMedia);
    }

    @Test
    void given_medium_when_update_then_updateMedium() {
        //given
        Medium medium = BaseRepositoryData.getMedia().iterator().next();
        Stock originalStock = medium.getStock();
        Stock expectedNewStock = Stock.of(originalStock.getQuantity().decreaseBy(Quantity.of(1)));

        //when
        medium.setStock(expectedNewStock);
        this.mediumHibernateRepository.update(medium);
        Stock actualStock = this.mediumHibernateRepository.findMediumById(medium.getId()).orElseGet(Assertions::fail).getStock();

        //then
        Assertions.assertNotEquals(originalStock, medium.getStock());
        Assertions.assertNotEquals(originalStock, actualStock);
        Assertions.assertEquals(expectedNewStock, actualStock);
    }
}