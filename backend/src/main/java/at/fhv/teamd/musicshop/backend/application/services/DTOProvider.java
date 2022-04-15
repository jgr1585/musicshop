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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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
        Medium medium = mediumRepository.findMediumById(lineItem.getMedium().getId()).orElseThrow();
        Article article = articleRepository.findArticleById(lineItem.getMedium().getArticle().getId()).orElseThrow();

        return LineItemDTO.builder()
                .withLineItemData(
                        lineItem.getId(),
                        buildArticleDTO(mediumRepository, article),
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
        if (article instanceof Album) {
            return buildArticleDTO(mediumRepository, (Album) article, true);

        } else if (article instanceof Song) {
            return buildArticleDTO(mediumRepository, (Song) article, true);

        } else {
            throw new UnsupportedOperationException();
        }
    }

    // TODO: remove this nasty medium repository at this point (this is not needed at all because if we invent medium backreference album/medium (bidirectional) we can get it from there
    private static AlbumDTO buildArticleDTO(MediumRepository mediumRepository, Album album, boolean firstLayer) {
        AlbumDTO.Builder builder = AlbumDTO.builder()
                .withArticleSpecificData(
                        album.getId(),
                        album.getTitle(),
                        album.getLabel(),
                        album.getReleaseDate(),
                        album.getGenre(),
                        album.getMusicbrainzId(),
                        album.getArtists()
                                .stream()
                                .map(DTOProvider::buildArtistDTO)
                                .collect(Collectors.toUnmodifiableSet()));

        if (firstLayer) {
            builder.withAlbumSpecificData(
                    mediumRepository.findMediumsByArticleId(album.getId()).stream().map(DTOProvider::buildMediumDTO).collect(Collectors.toUnmodifiableSet()),
                    album.getSongs().stream()
                            .map(song -> buildArticleDTO(mediumRepository, song, false))
                            .collect(Collectors.toUnmodifiableSet()));
        }

        return builder.build();
    }

    private static SongDTO buildArticleDTO(MediumRepository mediumRepository, Song song, boolean firstLayer) {
        SongDTO.Builder builder = SongDTO.builder()
                .withArticleSpecificData(
                        song.getId(),
                        song.getTitle(),
                        song.getLabel(),
                        song.getReleaseDate(),
                        song.getGenre(),
                        song.getMusicbrainzId(),
                        song.getArtists().stream()
                                .map(DTOProvider::buildArtistDTO)
                                .collect(Collectors.toUnmodifiableSet()));

        if (firstLayer) {
            builder.withSongSpecificData(
                    song.getLength(),
                    song.getAlbums().stream()
                            .map(album -> buildArticleDTO(mediumRepository, album, false))
                            .collect(Collectors.toUnmodifiableSet()));
        }

        return builder.build();
    }
}
