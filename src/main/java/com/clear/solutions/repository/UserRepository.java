package com.clear.solutions.repository;

import java.util.List;
import com.clear.solutions.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Repository;

@Repository
@Data
@AllArgsConstructor
public class UserRepository {
    private List<User> users;
}
