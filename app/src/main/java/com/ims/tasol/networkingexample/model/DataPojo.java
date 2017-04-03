
package com.ims.tasol.networkingexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataPojo {
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_age")
    @Expose
    private String userAge;
    @SerializedName("profile_photo")
    @Expose
    private String profilePhoto;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public DataPojo() {
    }

    public DataPojo(int userId, String userName, String userAge, String profilePhoto) {
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.profilePhoto = profilePhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

//    @SerializedName("user_id")
//    @Expose
//    private int userId;
//    @SerializedName("user_name")
//    @Expose
//    private String userName;
//    @SerializedName("user_age")
//    @Expose
//    private String userAge;
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getUserAge() {
//        return userAge;
//    }
//
//    public void setUserAge(String userAge) {
//        this.userAge = userAge;
//    }

}
