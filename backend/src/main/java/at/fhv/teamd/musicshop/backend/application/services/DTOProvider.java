package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.article.Artist;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.Supplier;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.library.DTO.*;

import java.util.*;
import java.util.stream.Collectors;

public class DTOProvider {

    private DTOProvider() {
    }

    static ShoppingCartDTO buildShoppingCartDTO(ArticleRepository articleRepository, MediumRepository mediumRepository, Set<LineItem> lineItems) {
        return ShoppingCartDTO.builder()
                .withLineItems(lineItems.stream().map(li -> buildLineItemDTO(articleRepository, mediumRepository, li)).collect(Collectors.toUnmodifiableSet()))
                .build();
    }

    static LineItemDTO buildLineItemDTO(ArticleRepository articleRepository, MediumRepository mediumRepository, LineItem lineItem) {
        Medium medium = mediumRepository.findMediumById(lineItem.getMediumId()).orElseThrow();
        Article article = articleRepository.findArticleById(lineItem.getArticleId()).orElseThrow();

        return LineItemDTO.builder()
                .withLineItemData(
                        lineItem.getId(),
                        buildArticleDTO(mediumRepository, article),
                        lineItem.getDescriptorName(),
                        lineItem.getQuantity().getValue(),
                        lineItem.getPrice(),
                        lineItem.getTotalPrice(),
                        buildMediumDTO(medium)
                )
                .build();
    }

    static MediumDTO buildMediumDTO(Medium medium) {
        Set<Long> ids = new HashSet<>();
        medium.getArticles().forEach(article -> ids.addAll(article.getMediumIDs()));

        return MediumDTO.builder().
                withMediumData(
                        medium.getId(),
                        medium.getPrice(),
                        buildSupplierDTO(medium.getSupplier()),
                        medium.getType().toString(),
                        medium.getStock().getQuantity().getValue(),
                        ids
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
                mediumDTOs.add(buildMediumDTO(mediumRepository.findMediumById(id).orElseThrow()))
        );

        if (article instanceof Album) {
            Album album = (Album) article;

            Set<SongDTO> songDTOs = new HashSet<>();
            for (Song song : album.getSongs()) {
                songDTOs.add((SongDTO) buildArticleDTO(mediumRepository, song));
            }

            Set<ArtistDTO> artistDTOs = album.getArtists()
                    .stream()
                    .map(DTOProvider::buildArtistDTO)
                    .collect(Collectors.toUnmodifiableSet());

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
