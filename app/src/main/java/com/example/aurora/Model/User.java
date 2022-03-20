package com.example.aurora.Model;

public class User {

    String UserEmail, UserID,UserName,UserPass,UserPhone,UserCoverPic,UserProfilePic;

    public User(String userEmail, String userID, String userName, String userPass, String userPhone, String userCoverPic, String userProfilePic) {
        UserEmail = userEmail;
        UserID = userID;
        UserName = userName;
        UserPass = userPass;
        UserPhone = userPhone;
        UserCoverPic = userCoverPic;
        UserProfilePic = userProfilePic;
    }

    public User() {
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPass() {
        return UserPass;
    }

    public void setUserPass(String userPass) {
        UserPass = userPass;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getUserCoverPic() {
        return UserCoverPic;
    }

    public void setUserCoverPic(String userCoverPic) {
        UserCoverPic = userCoverPic;
    }

    public String getUserProfilePic() {
        return UserProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        UserProfilePic = userProfilePic;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserEmail='" + UserEmail + '\'' +
                ", UserID='" + UserID + '\'' +
                ", UserName='" + UserName + '\'' +
                ", UserPass='" + UserPass + '\'' +
                ", UserPhone='" + UserPhone + '\'' +
                ", UserCoverPic='" + UserCoverPic + '\'' +
                ", UserProfilePic='" + UserProfilePic + '\'' +
                '}';
    }
}
