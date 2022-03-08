package snicholson.featureflag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snicholson.featureflag.repository.FeatureFlagRepository;
import snicholson.featureflag.repository.UserRepository;

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
}
