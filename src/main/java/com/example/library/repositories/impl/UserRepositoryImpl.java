package com.example.library.repositories.impl;

import com.example.library.entities.Role;
import com.example.library.entities.User;
import com.example.library.repositories.RoleRepository;
import com.example.library.repositories.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RoleRepository roleRepository;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate, RoleRepository roleRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.roleRepository = roleRepository;
    }

    private class UserRowMapper implements RowMapper<User> {
        private final JdbcTemplate jdbcTemplate;

        public UserRowMapper(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            Long userId = rs.getLong("id");

            String roleSql = "SELECT role_id FROM user_roles WHERE user_id = ?";
            List<Long> roleIds = jdbcTemplate.queryForList(roleSql, Long.class, userId);
            Set<Role> roles = new HashSet<>();
            for (Long roleId : roleIds) {
                roles.add(roleRepository.findById(roleId).orElseThrow());
            }
            user.setRoles(roles);

            return user;
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new UserRowMapper(jdbcTemplate), username));
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword());

        Long userId = findByUsername(user.getUsername()).orElseThrow().getId();
        for (Role role : user.getRoles()) {
            String roleSql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
            jdbcTemplate.update(roleSql, userId, role.getId());
        }
    }
}