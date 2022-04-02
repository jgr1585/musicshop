package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.article.Artist;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.Supplier;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.library.DTO.*;

import java.util.*;
import java.util.stream.Collectors;

public class DTOProvider {

    private DTOProvider() {
    }

    static ShoppingCartDTO buildShoppingCartDTO(MediumRepository mediumRepository, List<LineItem> lineItems) {
        return ShoppingCartDTO.builder()
                .withLineItems(lineItems.stream().map(li -> buildLineItemDTO(mediumRepository, li)).collect(Collectors.toUnmodifiableList()))
                .build();
    }

    static LineItemDTO buildLineItemDTO(MediumRepository mediumRepository, LineItem lineItem) {
        Medium medium = mediumRepository.findMediumById(lineItem.getMediumId()).orElseThrow();

        return LineItemDTO.builder()
                .withLineItemData(
                        lineItem.getId(),
                        buildArticleDTO(mediumRepository, new Article() {
                        }),
                        lineItem.getDescriptorName(),
                        lineItem.getQuantity().getValue(),
                        lineItem.getPrice(),
                        lineItem.getTotalPrice(),
                        buildMediumDTO(medium)
                )
                .build();
    }

    static MediumDTO buildMediumDTO(Medium medium) {
        return MediumDTO.builder().
                withMediumData(
                        medium.getId(),
                        medium.getPrice(),
                        buildSupplierDTO(medium.getSupplier()),
                        medium.getType().toString(),
                        medium.getStock().getQuantity().getValue()
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
        Set<MediumDTO> mediumDTOs = new HashSet<>();
        article.getMediumIDs().forEach(id ->
                mediumDTOs.add(buildMediumDTO(mediumRepository.findMediumById(id).get()))
        );

        if (article instanceof Album) {
            Album album = (Album) article;

            Set<SongDTO> songDTOs = new HashSet<>();
            for (Song song : album.getSongs()) {
                songDTOs.add((SongDTO) buildArticleDTO(mediumRepository, song));
            }

            Set<ArtistDTO> artistDTOs = new HashSet<>();
            for (Song song : album.getSongs()) {
                artistDTOs.addAll(song.getArtists()
                        .stream()
                        .map(artist -> buildArtistDTO(artist))
                        .collect(Collectors.toUnmodifiableSet()));
            }

            return AlbumDTO.builder().withAlbumData(
                    album.getId(),
                    album.getDescriptorName(),
                    album.getTitle(),
                    album.getLabel(),
                    album.getReleaseDate(),
                    album.getGenre(),
                    album.getMusicbrainzId(),
                    Collections.unmodifiableSet(mediumDTOs),
                    Collections.unmodifiableSet(songDTOs),
                    Collections.unmodifiableSet(artistDTOs)
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
                    Collections.unmodifiableSet(mediumDTOs),
                    song.getLength(),
                    song.getArtists().stream()
                            .map(DTOProvider::buildArtistDTO)
                            .collect(Collectors.toUnmodifiableSet())
            ).build();

        } else {
            throw new UnsupportedOperationException();
        }
    }
}
