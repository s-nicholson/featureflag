package snicholson.featureflag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import snicholson.featureflag.entity.dao.FeatureFlagDao;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlagDao, Long> {
    @Query(
            "SELECT f FROM FeatureFlagDao f WHERE f.name = ?1"
    )
    Optional<FeatureFlagDao> findByName(String name);

    @Query(
            "SELECT f FROM FeatureFlagDao f WHERE f.enabled = true"
    )
    Set<FeatureFlagDao> findEnabledFlags();
}
