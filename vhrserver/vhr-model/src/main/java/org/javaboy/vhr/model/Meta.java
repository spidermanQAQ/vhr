package org.javaboy.vhr.model;

import java.io.Serializable;

public class Meta implements Serializable {
    private Boolean requireAuth;

    private Boolean keepAlive;

    public Boolean getRequireAuth() {
        return requireAuth;
    }

    public void setRequireAuth(Boolean requireAuth) {
        this.requireAuth = requireAuth;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
}
