package at.fhv.teamd.musicshop.backend.application;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ApplicationClientSession {

    @Getter
    private final String userId;
    private final Map<ApplicationClientSession.attributes, Object> sessionObjects = new HashMap<>();

    public ApplicationClientSession(String userId) {
        setSessionObject(attributes.USERID, userId);
        this.userId = userId;
    }

    public boolean containsSessionObject(ApplicationClientSession.attributes sessionObjKey) {
        return sessionObjects.containsKey(sessionObjKey);
    }

    public Object getSessionObject(ApplicationClientSession.attributes sessionObjKey) {
        return sessionObjects.get(sessionObjKey);
    }

    public <T> T getSessionObject(ApplicationClientSession.attributes sessionObjKey, Class<T> type) {
        return type.cast(sessionObjects.get(sessionObjKey));
    }

    public void setSessionObject(ApplicationClientSession.attributes sessionObjKey, Object sessionObj) {
        sessionObjects.put(sessionObjKey, sessionObj);
    }

    private void getSessionObjectOrCallInitializer(ApplicationClientSession.attributes sessionObjKey, Callable<?> callable) {
        if (!sessionObjects.containsKey(sessionObjKey)) {
            try {
                sessionObjects.put(sessionObjKey, callable.call());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public <T> T getSessionObjectOrCallInitializer(ApplicationClientSession.attributes sessionObjKey, Callable<T> callable, Class<T> type) {
        getSessionObjectOrCallInitializer(sessionObjKey, callable);

        return type.cast(sessionObjects.get(sessionObjKey));
    }

    public enum attributes {
        USERID,
        USER_ROLES
    }
}