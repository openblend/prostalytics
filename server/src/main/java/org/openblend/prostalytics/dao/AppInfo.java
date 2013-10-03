package org.openblend.prostalytics.dao;

import java.io.Serializable;

public class AppInfo implements Serializable {
    private boolean isSetUp;

    public boolean isSetUp() {
        return isSetUp;
    }

    public void setSetUp(boolean setUp) {
        isSetUp = setUp;
    }
}