package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.article.Artist;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.medium.Supplier;
import at.fhv.teamd.musicshop.backend.domain.message.Message;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.domain.topic.Topic;
import at.fhv.teamd.musicshop.library.dto.*;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DTOProvider {

    private DTOProvider() {
    }

    static TopicDTO buildTopicDTO(Topic topic) {
        return buildTopicDTO(topic.getTopicName());
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
                        message.getId(),
                        message.getTitle(),
                        message.getBody()
                )
                .withMessageSentOnTimestamp(Objects.requireNonNull(message.getSentOnTimestamp()))
                .build();
    }

    static ShoppingCartDTO buildShoppingCartDTO(Set<LineItem> lineItems) {
        return ShoppingCartDTO.builder()
                .withLineItems(lineItems.stream().map(DTOProvider::buildLineItemDTO).collect(Collectors.toUnmodifiableSet()))
                .build();
    }

    static LineItemDTO buildLineItemDTO(LineItem lineItem) {
        Album album = lineItem.getMedium().getAlbum();
        Medium medium = album.getMediums().stream()
                .filter(m -> Long.valueOf(m.getId()).equals(lineItem.getMedium().getId()))
                .findFirst()
                .orElseThrow();

        return LineItemDTO.builder()
                .withLineItemData(
                        lineItem.getId(),
                        buildArticleDTO(album),
                        lineItem.getQuantity().getValue(),
                        lineItem.getQuantityReturn().getValue(),
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

    static InvoiceDTO buildInvoiceDTO(Invoice invoice) {
        return InvoiceDTO.builder().withInvoiceData(
                invoice.getId(),
                invoice.getLineItems().stream().map(DTOProvider::buildLineItemDTO).collect(Collectors.toUnmodifiableSet()),
                invoice.getTotalPrice(),
                invoice.getCustomerNo()
        ).build();
    }

    static ArticleDTO buildArticleDTO(Article article) {
        return buildArticleDTOWithMediumTypes(article, MediumType.allMediumTypes());
    }

    static AlbumDTO buildArticleDTO(Album album) {
        return buildArticleDTOWithMediumTypes(album, MediumType.allMediumTypes());
    }

    static SongDTO buildArticleDTO(Song song) {
        return buildArticleDTOWithMediumTypes(song, MediumType.allMediumTypes());
    }

    static ArticleDTO buildArticleDTOWithMediumTypes(Article article, EnumSet<MediumType> includedMediumTypes) {
        if (article instanceof Album) {
            return buildArticleDTOWithMediumTypes((Album) article, includedMediumTypes);

        } else if (article instanceof Song) {
            return buildArticleDTOWithMediumTypes((Song) article, includedMediumTypes);

        } else {
            throw new UnsupportedOperationException();
        }
    }

    static AlbumDTO buildArticleDTOWithMediumTypes(Album album, EnumSet<MediumType> includedMediumTypes) {
        final AlbumDTO.Builder albumBuilder = getAlbumDTOBuilder(album, includedMediumTypes);

        albumBuilder.withSongs(
                album.getSongs().stream()
                        .map(song -> getSongDTOBuilder(song).withAlbums(Collections.singletonList(albumBuilder.build())).build())
                        .collect(Collectors.toUnmodifiableList()));

        return albumBuilder.build();
    }

    static SongDTO buildArticleDTOWithMediumTypes(Song song, EnumSet<MediumType> includedMediumTypes) {
        final SongDTO.Builder songBuilder = getSongDTOBuilder(song);

        songBuilder.withAlbums(
                song.getAlbums().stream()
                        .map(album -> getAlbumDTOBuilder(album, includedMediumTypes).withSongs(Collections.singletonList(songBuilder.build())).build())
                        .collect(Collectors.toUnmodifiableList()));

        return songBuilder.build();
    }

    private static SongDTO.Builder getSongDTOBuilder(Song song) {
        final SongDTO.Builder builder = SongDTO.builder()
                .withArticleSpecificData(
                        song.getId(),
                        song.getUuid(),
                        song.getTitle(),
                        song.getLabel(),
                        song.getReleaseDate(),
                        song.getGenre(),
                        song.getMusicbrainzId(),
                        song.getArtists().stream()
                                .map(DTOProvider::buildArtistDTO)
                                .collect(Collectors.toUnmodifiableList()));
        return builder.withSongSpecificData(song.getLength());
    }

    private static AlbumDTO.Builder getAlbumDTOBuilder(Album album, EnumSet<MediumType> includedMediumTypes) {
        final AlbumDTO.Builder builder = AlbumDTO.builder()
                .withArticleSpecificData(
                        album.getId(),
                        album.getUuid(),
                        album.getTitle(),
                        album.getLabel(),
                        album.getReleaseDate(),
                        album.getGenre(),
                        album.getMusicbrainzId(),
                        album.getArtists()
                                .stream()
                                .map(DTOProvider::buildArtistDTO)
                                .collect(Collectors.toUnmodifiableList()));
        return builder.withAlbumSpecificData(album.getMediums().stream()
                .filter(medium -> includedMediumTypes.contains(medium.getType()))
                .map(DTOProvider::buildMediumDTO)
                .collect(Collectors.toUnmodifiableList()));
    }
}
