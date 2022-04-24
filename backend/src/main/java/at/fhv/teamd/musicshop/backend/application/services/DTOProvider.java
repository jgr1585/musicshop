package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.article.Artist;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.Supplier;
import at.fhv.teamd.musicshop.backend.domain.message.Message;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.domain.topic.Topic;
import at.fhv.teamd.musicshop.library.DTO.*;

import java.util.Set;
import java.util.stream.Collectors;

public class DTOProvider {

    private DTOProvider() {
    }

    static TopicDTO buildTopicDTO(Topic topic) {
        return buildTopicDTO(topic.getName());
    }

    private static TopicDTO buildTopicDTO(String topicName) {
        return TopicDTO.builder()
                .withTopicData(topicName)
                .build();
    }

    static MessageDTO buildMessageDTO(Message message) {
        return MessageDTO.builder()
                .withMessageData(
                        buildTopicDTO(message.getTopicName()),
                        message.getTitle(),
                        message.getBody()
                ).build();
    }

    static ShoppingCartDTO buildShoppingCartDTO(ArticleRepository articleRepository, Set<LineItem> lineItems) {
        return ShoppingCartDTO.builder()
                .withLineItems(lineItems.stream().map(li -> buildLineItemDTO(articleRepository, li)).collect(Collectors.toUnmodifiableSet()))
                .build();
    }

    static LineItemDTO buildLineItemDTO(ArticleRepository articleRepository, LineItem lineItem) {
        Album album = (Album) articleRepository.findArticleById(lineItem.getMedium().getAlbum().getId()).orElseThrow();
        Medium medium = album.getMediums().stream()
                .filter(m -> Long.valueOf(m.getId()).equals(lineItem.getMedium().getId()))
                .findFirst()
                .orElseThrow();

        return LineItemDTO.builder()
                .withLineItemData(
                        lineItem.getId(),
                        buildArticleDTO(album),
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

    static ArticleDTO buildArticleDTO(Article article) {
        if (article instanceof Album) {
            return buildArticleDTO((Album) article, true);

        } else if (article instanceof Song) {
            return buildArticleDTO((Song) article, true);

        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static AlbumDTO buildArticleDTO(Album album, boolean firstLayer) {
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
                    album.getMediums().stream().map(DTOProvider::buildMediumDTO).collect(Collectors.toUnmodifiableSet()),
                    album.getSongs().stream()
                            .map(song -> buildArticleDTO(song, false))
                            .collect(Collectors.toUnmodifiableSet()));
        }

        return builder.build();
    }

    private static SongDTO buildArticleDTO(Song song, boolean firstLayer) {
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
                            .map(album -> buildArticleDTO(album, false))
                            .collect(Collectors.toUnmodifiableSet()));
        }

        return builder.build();
    }
}
