package spring_test.pp_3_1_4_rest.service;





import spring_test.pp_3_1_4_rest.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    void saveUser(User user, String rawPassword);
    Optional<User> findById(Long id);
    void updateUser(User user,String rawPassword);
    void deleteUser(Long id);
    User findByEmail(String email);
    void updateUserWithoutPassword(User user);
}