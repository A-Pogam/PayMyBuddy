package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import model.DBUser;

public interface DBUserRepository extends JpaRepository<DBUser, Integer> {
    public DBUser findByEmail(String email);
}
