package at.fhv.teamd.application.services;

import at.fhv.teamd.musicshop.library.dto.AlbumDTO;
import at.fhv.teamd.musicshop.library.dto.SongDTO;
import at.fhv.teamd.musicshop.library.exceptions.UnauthorizedMediaException;
import lombok.NoArgsConstructor;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@NoArgsConstructor
public class MediaService {

    public ByteArrayOutputStream getPlaylistAlbumBinaryStream(List<AlbumDTO> playlist, int albumId) throws IOException, UnauthorizedMediaException {
        AlbumDTO albumDTO = findAlbumFromPlaylist(playlist, albumId);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(baos);

        for (SongDTO songDTO : albumDTO.songs()) {
            InputStream fis = getClass().getClassLoader().getResourceAsStream("/WEB-INF/songs/" + songDTO.uuid() + ".mp3");
            if (fis == null)
                throw new FileNotFoundException();

            zip.putNextEntry(new ZipEntry(songDTO.title() + ".mp3"));

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zip.write(buffer, 0, length);
            }

            fis.close();
        }
        zip.close();

        return baos;
    }

    public ByteArrayOutputStream getPlaylistSongBinaryStream(List<AlbumDTO> playlist, int songId) throws IOException, UnauthorizedMediaException {

        SongDTO songDTO = findSongFromPlaylist(playlist, songId);
        InputStream in = getClass().getClassLoader().getResourceAsStream("/WEB-INF/songs/" + songDTO.uuid() + ".mp3");
        if (in == null)
            throw new FileNotFoundException();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        in.transferTo(baos);
        in.close();

        return baos;
    }

    public String getAlbumDownloadFilename(List<AlbumDTO> playlist, int albumId) throws UnauthorizedMediaException {
        return findAlbumFromPlaylist(playlist, albumId).title() + ".zip";
    }

    public String getSongDownloadFilename(List<AlbumDTO> playlist, int songId) throws UnauthorizedMediaException {
        return findSongFromPlaylist(playlist, songId).title() + ".mp3";
    }

    private AlbumDTO findAlbumFromPlaylist(List<AlbumDTO> playlist, int albumId) throws UnauthorizedMediaException {
        return playlist.stream()
                .filter(album -> album.id() == albumId)
                .findFirst()
                .orElseThrow(UnauthorizedMediaException::new);
    }

    private SongDTO findSongFromPlaylist(List<AlbumDTO> playlist, int songId) throws UnauthorizedMediaException {
        return playlist.stream()
                .flatMap(album -> album.songs().stream())
                .filter(song -> song.id() == songId)
                .findFirst()
                .orElseThrow(UnauthorizedMediaException::new);
    }
}
