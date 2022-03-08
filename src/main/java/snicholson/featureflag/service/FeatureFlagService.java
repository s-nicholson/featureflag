package snicholson.featureflag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import snicholson.featureflag.entity.dto.FeatureFlag;
import snicholson.featureflag.entity.dto.UserFlag;
import snicholson.featureflag.repository.FeatureFlagRepository;
import snicholson.featureflag.repository.UserRepository;

import java.util.Collection;

@Service
public class FeatureFlagService {
    private final UserRepository userRepository;
    private final FeatureFlagRepository featureFlagRepository;

    @Autowired
    public FeatureFlagService(
            final FeatureFlagRepository featureFlagRepository,
            final UserRepository userRepository
    ) {
        this.featureFlagRepository = featureFlagRepository;
        this.userRepository = userRepository;
    }

    public FeatureFlag createFlag(FeatureFlag featureFlag) {
        return null;
    }

    public UserFlag enableFlag(long userId, String flagName) {
        return null;
    }

    public Collection<UserFlag> getFlags(long userId) {
        return null;
    }
}
