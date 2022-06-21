package at.fhv.teamd.application.services;

public class ServiceFactory {
    private static PlaylistService playlistService;
    private static MediaService mediaService;

    private ServiceFactory() {}

    public static PlaylistService getPlaylistServiceInstance() {
        if (playlistService == null) {
            playlistService = new PlaylistService();
        }
        return playlistService;
    }

    public static MediaService getMediaServiceInstance() {
        if (mediaService == null) {
            mediaService = new MediaService();
        }
        return mediaService;
    }
}
