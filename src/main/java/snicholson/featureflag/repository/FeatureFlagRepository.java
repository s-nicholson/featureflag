package snicholson.featureflag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import snicholson.featureflag.entity.dao.FeatureFlagDao;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlagDao, Long> {
}
