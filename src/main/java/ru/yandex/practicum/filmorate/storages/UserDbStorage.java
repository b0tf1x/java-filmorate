package ru.yandex.practicum.filmorate.storages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAll() {
        final String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, this::createUser);
    }

    private User createUser(ResultSet resultSet, int rowNum) throws SQLException {
        final int id = resultSet.getInt("id");
        final String email = resultSet.getString("email");
        final String login = resultSet.getString("login");
        final String name = resultSet.getString("name");
        final LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }

    @Override
    public User create(User user) {
        final String sqlQuery = "insert into users(email,login,name,birthday) values(?,?,?,?) ";
        KeyHolder generatedId = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, generatedId);
        user.setId(Objects.requireNonNull(generatedId.getKey().intValue()));
        log.info("Пользователь с почтой +" + user.getEmail());
        return user;
    }
    private void checkExists(int id){
        final String checkQuery = "select * from users where id = ?";
        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet(checkQuery, id);
        if (!userRowSet.next()) {
            throw new UserException("Пользователь не найден");
        }
    }

    @Override
    public User put(User user) {
       checkExists(user.getId());
        log.info("email = " + user.getEmail() + " login= " + user.getLogin() + " name = " + user.getName() + " birthday= " + user.getBirthday() + " id = " + user.getId());
        final String query = "UPDATE users SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? where id = ?";
        jdbcTemplate.update(query,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User getUserById(int id) {
        String checkExists = "select * from users where id=?";
        checkExists(id);
        return jdbcTemplate.queryForObject(checkExists, this::createUser, id);
    }

    @Override
    public User deleteUserById(int id) {
        User user = getUserById(id);
        final String query = "delete from users where id = ?";
        jdbcTemplate.update(query, id);
        return user;
    }

    @Override
    public Map<Integer, User> getUsers() {
        Map<Integer, User> userMap = new HashMap<>();
        final String query = "select * from users";
        Collection<User> users = jdbcTemplate.query(query, this::createUser);
        for (User user : users) {
            userMap.put(user.getId(), user);
        }
        return userMap;
    }

    @Override
    public List<Integer> addFriend(int firstId, int secondId) {
        final String query = "insert into friends(user_id,friend_id,friendship_status) values(?,?,?)";
        final String statusQuery = "update friends set friendship_status = ? where user_id=? and friend_id=?";
        final String check = "select * from friends where user_id=? and friend_id=?";
        SqlRowSet friendsRowSet = jdbcTemplate.queryForRowSet(check, firstId, secondId);
        if (!friendsRowSet.first()) {
            jdbcTemplate.update(query, firstId, secondId, FriendshipStatus.REQUIRED.toString());
        } else {
            jdbcTemplate.update(statusQuery, FriendshipStatus.CONFIRMED.toString(), firstId, secondId);
        }
        return List.of(firstId, secondId);
    }

    @Override
    public List<Integer> removeFriend(int firstId, int secondId) {
        final String query = "delete from friends where user_id=? and friend_id=?";
        jdbcTemplate.update(query, firstId, secondId);
        return List.of(firstId, secondId);
    }

    @Override
    public List<User> getFriends(int id) {
        final String checkExists = "select * from users where id = ?";
        SqlRowSet checkRow = jdbcTemplate.queryForRowSet(checkExists, id);
        if (!checkRow.next()) {
            throw new UserException("Пользователь не найден");
        }
        final String query = "select id,email,login,name,birthday from users " +
                "left join friends as f on id=f.friend_id where f.user_id=? and " +
                "friendship_status like 'REQUIRED'";
        return jdbcTemplate.query(query, this::createUser, id);
    }

    @Override
    public List<User> getCommonFriends(int firstId, int secondId) {
        final String query = "select id, email,login, name, birthday from friends as f " +
                "left join users on users.id=f.friend_id where f.user_id=? and f.friend_id " +
                "in (select friend_id from friends as f left join users on users.id=f.friend_id " +
                "where f.user_id=?)";
        return jdbcTemplate.query(query, this::createUser, firstId, secondId);
    }


}
