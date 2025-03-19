package org.example.fosdesktop.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.prefs.Preferences;


@Service
public class StorageService {

    public String getItem(String key) {
        return preferences.get(key, null);
    }

    public void setItem(String key, String value) {
        preferences.put(key, value);
    }

    public void setJwtToken(String value) {
        setItem("jwtToken", value);
    }

    public void setTotalPrice(int value) {
        preferences.putInt("totalPrice", value);
    }

    public int getTotalPrice() {
        return preferences.getInt("totalPrice", 0);
    }

    public void removeItem(String key) {
        preferences.remove(key);
    }

    public String getJwtToken() {
        return getItem("jwtToken");
    }

    public void removeJwtToken() {
        removeItem("jwtToken");
    }

    @PostConstruct
    public void init() {
        this.preferences = Preferences.userNodeForPackage(StorageService.class);
    }

    private Preferences preferences;
}
