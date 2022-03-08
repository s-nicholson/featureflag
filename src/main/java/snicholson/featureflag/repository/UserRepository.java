package snicholson.featureflag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import snicholson.featureflag.entity.dao.UserDao;

@Repository
public interface UserRepository extends JpaRepository<UserDao, Long> {
}
