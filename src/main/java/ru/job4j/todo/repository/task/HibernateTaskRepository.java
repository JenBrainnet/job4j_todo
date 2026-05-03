package ru.job4j.todo.repository.task;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HibernateTaskRepository implements TaskRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Task save(Task task) {
        try (Session session = sessionFactory.openSession()) {
            var tx = session.beginTransaction();
            try {
                session.save(task);
                tx.commit();
                return task;
            } catch (Exception e) {
                tx.rollback();
                log.error("Failed to save task: {}", task.getTitle(), e);
                throw e;
            }
        }
    }

    @Override
    public List<Task> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "FROM Task ORDER BY created DESC", Task.class)
                    .list();
        }
    }

    @Override
    public List<Task> findByDone(boolean done) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "FROM Task WHERE done = :done ORDER BY created DESC", Task.class)
                    .setParameter("done", done)
                    .list();
        }
    }

    @Override
    public Optional<Task> findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Task WHERE id = :id", Task.class)
                    .setParameter("id", id)
                    .uniqueResultOptional();
        }
    }

    @Override
    public boolean update(Task task) {
        try (Session session = sessionFactory.openSession()) {
            var tx = session.beginTransaction();
            try {
                int result = session.createQuery(
                        "UPDATE Task SET title = :title, description = :description WHERE id = :id")
                        .setParameter("title", task.getTitle())
                        .setParameter("description", task.getDescription())
                        .setParameter("id", task.getId())
                        .executeUpdate();
                tx.commit();
                return result > 0;
            } catch (Exception e) {
                tx.rollback();
                log.error("Failed to update task with id: {}", task.getId(), e);
                throw e;
            }
        }
    }

    @Override
    public boolean complete(int id) {
        try (Session session = sessionFactory.openSession()) {
            var tx = session.beginTransaction();
            try {
                int result = session.createQuery(
                        "UPDATE Task SET done = true WHERE id = :id")
                        .setParameter("id", id)
                        .executeUpdate();
                tx.commit();
                return result > 0;
            } catch (Exception e) {
                tx.rollback();
                log.error("Failed to complete task with id: {}", id, e);
                throw e;
            }
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Session session = sessionFactory.openSession()) {
            var tx = session.beginTransaction();
            try {
                int result = session.createQuery(
                        "DELETE FROM Task WHERE id = :id")
                        .setParameter("id", id)
                        .executeUpdate();
                tx.commit();
                return result > 0;
            } catch (Exception e) {
                tx.rollback();
                log.error("Failed to delete task with id: {}", id, e);
                throw e;
            }
        }
    }

}
