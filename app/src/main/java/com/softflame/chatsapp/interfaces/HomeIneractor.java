package com.softflame.chatsapp.interfaces;

import com.softflame.chatsapp.models.Contact;
import com.softflame.chatsapp.models.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by a_man on 01-01-2018.
 */

public interface HomeIneractor {
    HashMap<String, Contact> getLocalContacts();
}
