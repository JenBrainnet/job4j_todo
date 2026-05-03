package ru.job4j.todo.repository.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HibernateUserRepository implements UserRepository {

    private static final String UNIQUE_VIOLATION_CODE = "23505";

    private final SessionFactory sessionFactory;

    @Override
    public Optional<User> save(User user) {
        try (Session session = sessionFactory.openSession()) {
            var tx = session.beginTransaction();
            try {
                session.save(user);
                tx.commit();
                return Optional.of(user);
            } catch (Exception e) {
                tx.rollback();
                if (isUniqueViolation(e)) {
                    log.warn("Registration failed. User with login {} already exists", user.getLogin());
                    return Optional.empty();
                }
                log.error("Failed to save user with login: {}", user.getLogin(), e);
                throw e;
            }
        }
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "FROM User WHERE login = :login AND password = :password", User.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .uniqueResultOptional();
        }
    }

    private boolean isUniqueViolation(Exception e) {
        return e instanceof ConstraintViolationException constraintViolationException
                && UNIQUE_VIOLATION_CODE.equals(
                constraintViolationException.getSQLException().getSQLState());
    }

}
