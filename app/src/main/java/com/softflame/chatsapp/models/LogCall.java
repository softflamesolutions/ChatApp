package com.softflame.chatsapp.models;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class LogCall implements RealmModel {
    private long timeUpdated;
    private int timeDurationSeconds;
    private String status, myId, userId, userName, userImage, userStatus,roll;
    private boolean isVideo;

    public LogCall() {
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public LogCall(User user, long timeUpdated, int timeDurationSeconds, boolean isVideo,
                   String status, String myId, String roll) {
        this.timeUpdated = timeUpdated;
        this.timeDurationSeconds = timeDurationSeconds;
        this.isVideo = isVideo;
        this.status = status;
        this.myId = myId;
        this.userId = user.getId();
        this.userName = user.getNameToDisplay();
        this.userImage = user.getImage();
        this.userStatus = user.getStatus();
        this.roll = roll;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public long getTimeUpdated() {
        return timeUpdated;
    }

    public int getTimeDuration() {
        return timeDurationSeconds;
    }

    public int getTimeDurationSeconds() {
        return timeDurationSeconds;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMyId() {
        return myId;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public String getStatus() {
        return status;
    }
}
