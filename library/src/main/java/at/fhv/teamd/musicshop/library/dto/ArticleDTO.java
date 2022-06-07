package at.fhv.teamd.musicshop.library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AlbumDTO.class, name = "album"),
        @JsonSubTypes.Type(value = SongDTO.class, name = "song")})
public interface ArticleDTO {
    @JsonProperty(required = true)
    Long id();

    @JsonProperty(required = true)
    String title();

    @JsonProperty(required = true)
    String label();

    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    LocalDate releaseDate();

    @JsonProperty(required = true)
    String genre();

    @JsonProperty(required = true)
    String musicbrainzId();

    @JsonProperty(required = true)
    List<ArtistDTO> artists();
}
