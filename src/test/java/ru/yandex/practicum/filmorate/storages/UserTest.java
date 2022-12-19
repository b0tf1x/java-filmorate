package ru.yandex.practicum.filmorate.storages;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.AssertionsForClassTypes;
import ru.yandex.practicum.filmorate.exceptions.UserException;
import ru.yandex.practicum.filmorate.model.User;
import org.assertj.core.api.Assertions;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserTest {
    private final UserDbStorage userDbStorage;

    @Test
    void add() {
        User user = new User(1, "email", "login", "name", LocalDate.of(2010, 10, 10));

        userDbStorage.create(user);
        AssertionsForClassTypes.assertThat(user).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(user).extracting("name").isNotNull();
    }


    @Test
    void findById() {
        User user = new User(1, "email", "login", "name", LocalDate.of(2010, 10, 10));
        userDbStorage.create(user);
        AssertionsForClassTypes.assertThat(userDbStorage.getUserById(user.getId())).hasFieldOrPropertyWithValue("id", user.getId());
    }

    @Test
    void updateById() {
        User user = new User(1, "emailBefore", "loginBefore", "nameBefore", LocalDate.of(2010, 10, 10));
        userDbStorage.create(user);
        user.setName("nameAfter");
        user.setLogin("loginAfter");
        user.setEmail("emailAfter");
        userDbStorage.put(user);
        AssertionsForClassTypes.assertThat(userDbStorage.getUserById(user.getId()))
                .hasFieldOrPropertyWithValue("login", "loginAfter")
                .hasFieldOrPropertyWithValue("name", "nameAfter")
                .hasFieldOrPropertyWithValue("email", "emailAfter");
    }

    @Test
    public void UpdateNotFound() {
        User user = new User(9999, "email", "login", "name", LocalDate.of(2010, 10, 10));
        Assertions.assertThatThrownBy(() -> userDbStorage.put(user))
                .isInstanceOf(UserException.class);
    }

    @Test
    void addFriendshipTest() {
        User user = new User(1, "email", "login", "name", LocalDate.of(2010, 10, 10));
        User friend = new User(2, "email2", "login2", "name2", LocalDate.of(2015, 10, 10));
        userDbStorage.create(user);
        userDbStorage.create(friend);
        assertThat(userDbStorage.getFriends(friend.getId()).isEmpty());
        userDbStorage.addFriend(user.getId(), friend.getId());
        assertThat(userDbStorage.getFriends(user.getId())).isNotNull();
        Assertions.assertThat(userDbStorage.getFriends(friend.getId()).size() == 2);
    }

    @Test
    void removeFriendshipTest() {
        User user = new User(1, "email", "login", "name", LocalDate.of(2010, 10, 10));
        User friend = new User(2, "email2", "login2", "name2", LocalDate.of(2015, 10, 10));

        userDbStorage.create(user);
        userDbStorage.create(friend);
        assertThat(userDbStorage.getFriends(friend.getId()).isEmpty());
        userDbStorage.addFriend(user.getId(), friend.getId());
        assertThat(userDbStorage.getFriends(friend.getId())).isNotNull();
        Assertions.assertThat(userDbStorage.getFriends(user.getId()).size() == 2);
        userDbStorage.removeFriend(user.getId(), friend.getId());
        Assertions.assertThat(userDbStorage.getFriends(user.getId()).size() == 1);
    }

    @Test
    void getFriendshipTest() {
        User user = new User(1, "email", "login", "name", LocalDate.of(2010, 10, 10));
        User friend = new User(2, "email2", "login2", "name2", LocalDate.of(2015, 10, 10));
        userDbStorage.create(user);
        userDbStorage.create(friend);
        assertThat(userDbStorage.getFriends(user.getId()).isEmpty());
        userDbStorage.addFriend(user.getId(), friend.getId());
        Assertions.assertThat(userDbStorage.getFriends(user.getId()).size() == 2);
    }

    @Test
    void getCommonFriendshipTest() {
        User user = new User(1, "email", "login", "name", LocalDate.of(2010, 10, 10));
        User friend = new User(2, "email2", "login2", "name2", LocalDate.of(2015, 10, 10));
        User friend2 = new User(3, "email3", "login3", "name3", LocalDate.of(2005, 10, 10));

        userDbStorage.create(user);
        userDbStorage.create(friend);
        userDbStorage.create(friend2);
        userDbStorage.addFriend(user.getId(), friend2.getId());
        userDbStorage.addFriend(friend.getId(), friend2.getId());
        Assertions.assertThat(userDbStorage.getCommonFriends(user.getId(), friend.getId()).size() == 1);
    }

    @Test
    void getAllUsersTest() {
        User user = new User(1, "email", "login", "name", LocalDate.of(2010, 10, 10));
        userDbStorage.create(user);
        Collection<User> users = userDbStorage.findAll();
        Assertions.assertThat(users).isNotEmpty().isNotNull().doesNotHaveDuplicates();
        Assertions.assertThat(users).extracting("email").contains(user.getEmail());
        Assertions.assertThat(users).extracting("login").contains(user.getLogin());
    }

    @Test
    void removeUserByIdTest() {
        User user = new User(1, "email", "login", "name", LocalDate.of(2010, 10, 10));
        userDbStorage.create(user);
        userDbStorage.deleteUserById(user.getId());
        Assertions.assertThatThrownBy(() -> userDbStorage.getUserById(user.getId()))
                .isInstanceOf(UserException.class);
    }
}
