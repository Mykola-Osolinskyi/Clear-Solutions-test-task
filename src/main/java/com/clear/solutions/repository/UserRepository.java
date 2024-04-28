package com.clear.solutions.repository;

import com.clear.solutions.model.User;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Data
public class UserRepository {
    public List<User> users = new ArrayList<>();
}
