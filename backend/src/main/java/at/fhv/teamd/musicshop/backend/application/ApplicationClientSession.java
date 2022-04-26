package at.fhv.teamd.musicshop.backend.application;

import lombok.Getter;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ApplicationClientSession {

    @Getter private final String userId;
    private final Map<String, Object> sessionObjects = new HashMap<>();

    public ApplicationClientSession(String userId) {
        this.userId = userId;
    }

    public Object getSessionObject(String sessionObjKey) {
        return sessionObjects.get(sessionObjKey);
    }

    public <T> T getSessionObject(String sessionObjKey, Class<T> type) {
        return type.cast(sessionObjects.get(sessionObjKey));
    }

    public void setSessionObject(String sessionObjKey, Object sessionObj) {
        sessionObjects.put(sessionObjKey, sessionObj);
    }

    public void getSessionObjectOrCallInitializer(String sessionObjKey, Callable<?> callable) {
        if (!sessionObjects.containsKey(sessionObjKey)) {
            try {
                sessionObjects.put(sessionObjKey, callable.call());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public <T> T getSessionObjectOrCallInitializer(String sessionObjKey, Callable<T> callable, Class<T> type) {
        getSessionObjectOrCallInitializer(sessionObjKey, callable);

        return type.cast(sessionObjects.get(sessionObjKey));
    }

    public void purge() {
        sessionObjects.values().stream()
                .filter(obj -> obj instanceof Closeable)
                .forEach(obj -> {
                    try {
                        ((Closeable) obj).close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
