package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.article.Artist;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.medium.AnalogMedium;
import at.fhv.teamd.musicshop.backend.domain.medium.AnalogMediumType;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.Supplier;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.library.DTO.*;

import java.util.*;
import java.util.stream.Collectors;

public class DTOProvider {

    private DTOProvider() {}

    static ShoppingCartDTO buildShoppingCartDTO(MediumRepository mediumRepository, List<LineItem> lineItems) {
        return ShoppingCartDTO.builder()
                .withLineItems(lineItems.stream().map(li -> buildLineItemDTO(mediumRepository, li)).collect(Collectors.toUnmodifiableList()))
                .build();
    }

    static LineItemDTO buildLineItemDTO(MediumRepository mediumRepository, LineItem lineItem) {
        Medium medium = mediumRepository.findAnalogMediumById(lineItem.getMediumId()).orElseThrow();

        return LineItemDTO.builder()
                .withLineItemData(
                        lineItem.getId(),
                        buildArticleDTO(mediumRepository, new Article() {}),
                        lineItem.getDescriptorName(),
                        lineItem.getQuantity().getValue(),
                        lineItem.getPrice(),
                        lineItem.getTotalPrice(),
                        buildMediumDTO(medium)
                )
                .build();
    }

    static MediumDTO buildMediumDTO(Medium medium) {
        if (medium instanceof AnalogMedium) {
            return buildAnalogMediumDTO((AnalogMedium) medium);

        } else {
            throw new UnsupportedOperationException();
        }
    }

    static AnalogMediumDTO buildAnalogMediumDTO(AnalogMedium analogMedium) {
        return AnalogMediumDTO.builder().
                withAnalogMediumData(
                        analogMedium.getId(),
                        analogMedium.getPrice(),
                        buildSupplierDTO(analogMedium.getSupplier()),
                        analogMedium.getType().toString(),
                        analogMedium.getStock().getQuantity().getValue()
                ).build();
    }

    static SupplierDTO buildSupplierDTO(Supplier supplier) {
        return SupplierDTO.builder().withSupplierData(
                supplier.getId(),
                supplier.getName(),
                supplier.getSupplyDuration()
        ).build();
    }

    static ArtistDTO buildArtistDTO(Artist artist) {
        return ArtistDTO.builder().withArtistData(
                artist.getId(),
                artist.getName()
        ).build();
    }

    static ArticleDTO buildArticleDTO(MediumRepository mediumRepository, Article article) {
        Map<String, AnalogMediumDTO> analogMediumDTOMap = new HashMap<>();
        for (Map.Entry<AnalogMediumType, Long> analogMediumID : article.getAnalogMediumIDs().entrySet()) {
            AnalogMedium analogMedium = mediumRepository.findAnalogMediumById(analogMediumID.getValue()).orElseThrow();

            analogMediumDTOMap.put(
                    analogMediumID.getKey().toString(),
                    buildAnalogMediumDTO(analogMedium)
            );
        }

        if (article instanceof Album) {
            Album album = (Album) article;

            List<SongDTO> songDTOs = new ArrayList<>();
            for (Song song : album.getSongs()) {
                songDTOs.add((SongDTO) buildArticleDTO(mediumRepository, song));
            }
            List<ArtistDTO> artistDTOs = new ArrayList<>();
            for (Artist artist : album.getArtists()) {
                artistDTOs.add(buildArtistDTO(artist));
            }

            return AlbumDTO.builder().withAlbumData(
                    album.getId(),
                    album.getDescriptorName(),
                    album.getTitle(),
                    album.getLabel(),
                    album.getReleaseDate(),
                    album.getGenre(),
                    album.getMusicbrainzId(),
                    Collections.unmodifiableMap(analogMediumDTOMap),
                    Collections.unmodifiableList(songDTOs),
                    Collections.unmodifiableList(artistDTOs)
            ).build();

        } else if (article instanceof Song) {
            Song song = (Song) article;

            return SongDTO.builder().withSongData(
                    song.getId(),
                    song.getDescriptorName(),
                    song.getTitle(),
                    song.getLabel(),
                    song.getReleaseDate(),
                    song.getGenre(),
                    song.getMusicbrainzId(),
                    Collections.unmodifiableMap(analogMediumDTOMap),
                    song.getLength(),
                    song.getArtists().stream()
                            .map(DTOProvider::buildArtistDTO)
                            .collect(Collectors.toUnmodifiableList())
            ).build();

        } else {
            throw new UnsupportedOperationException();
        }
    }
}
