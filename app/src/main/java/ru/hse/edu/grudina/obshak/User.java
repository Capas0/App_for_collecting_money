package ru.hse.edu.grudina.obshak;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import ru.hse.edu.grudina.obshak.interfaces.UserCallBack;

@IgnoreExtraProperties
public class User implements Serializable {
    private String photo;
    private String nickName;
    private String fio;
    private String birthday;
    private String city;
    private String language;
    private String work;
    private String email;
    private String phone;
    private String address;
    private String vk;
    private String instagram;
    private String facebook;
    private String twitter;
    private String telegram;
    private String skype;
    private String hobby;
    private String quote;
    private String film;
    private String book;
    private String musicalGroup;
    private String politicalPreferences;

    public User(){

    }

    public static void createUser(String nickName, String email){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        User user = new User();
        user.nickName = nickName;
        user.email = email;
        ref.setValue(user);
    }

    public static void loadUser(String uid, final UserCallBack callBack){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callBack.onCallback(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public static boolean pushUser(User user, String uid){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        Map map = new TreeMap();
        map.put(uid, user);
        try {
            ref.updateChildren(map);
        }catch (Throwable ex){
            return false;
        }
        return true;
    }

    public boolean updateInfo(Map<String, Object> map){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        try{
            ref.updateChildren(map);
        } catch (Throwable e){
            return false;
        }
        return true;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVk() {
        return vk;
    }

    public void setVk(String vk) {
        this.vk = vk;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getMusicalGroup() {
        return musicalGroup;
    }

    public void setMusicalGroup(String musicalGroup) {
        this.musicalGroup = musicalGroup;
    }

    public String getPoliticalPreferences() {
        return politicalPreferences;
    }

    public void setPoliticalPreferences(String politicalPreferences) {
        this.politicalPreferences = politicalPreferences;
    }
}
