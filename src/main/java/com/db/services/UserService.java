package com.db.services;
import com.db.entities.Users;

import java.util.List;

public interface UserService {
    Users saveUsers(Users user);
    public List<Users> getAllUsers();
}
