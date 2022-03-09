package snicholson.featureflag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import snicholson.featureflag.entity.FeatureFlagDaoTransformer;
import snicholson.featureflag.entity.FeatureFlagTransformer;
import snicholson.featureflag.entity.Transformer;
import snicholson.featureflag.entity.UserDaoTransformer;
import snicholson.featureflag.entity.dao.FeatureFlagDao;
import snicholson.featureflag.entity.dao.UserDao;
import snicholson.featureflag.entity.dto.FeatureFlag;
import snicholson.featureflag.entity.dto.User;
import snicholson.featureflag.entity.dto.UserFlag;
import snicholson.featureflag.entity.dto.UserFlagSet;
import snicholson.featureflag.repository.FeatureFlagRepository;
import snicholson.featureflag.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.*;

@Service
public class FeatureFlagService {
    private static final Transformer<FeatureFlag, FeatureFlagDao> FEATURE_FLAG_TRANSFORMER = new FeatureFlagTransformer();
    private static final Transformer<FeatureFlagDao, FeatureFlag> FEATURE_FLAG_DAO_TRANSFORMER = new FeatureFlagDaoTransformer();
    private static final Transformer<UserDao, User> USER_DAO_TRANSFORMER = new UserDaoTransformer();

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

    public FeatureFlag createFlag(final FeatureFlag featureFlag) {
        var existingFlag = featureFlagRepository.findByName(featureFlag.getName());
        if (existingFlag.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format("Flag with name [%s] already exists.", featureFlag.getName()));
        }

        var newFlagDao = FEATURE_FLAG_TRANSFORMER.transform(featureFlag);
        var saved = featureFlagRepository.save(newFlagDao);
        return FEATURE_FLAG_DAO_TRANSFORMER.transform(saved);
    }

    public UserFlag enableFlag(final long userId, final String flagName) {
        var existingUser = userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No user with ID [%s] exists.", userId));
        }

        var existingFlag = featureFlagRepository.findByName(flagName);
        if (existingFlag.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No flag with name [%s] exists.", flagName));
        }

        var userDao = existingUser.get();
        var toAdd = Set.of(existingFlag.get());
        if (userDao.getFlags() == null) {
            userDao.setFlags(toAdd);
        } else {
            userDao.getFlags().addAll(toAdd);
        }
        userRepository.save(userDao);

        return UserFlag.builder()
                .user(USER_DAO_TRANSFORMER.transform(userDao))
                .flagName(flagName)
                .build();
    }

    public UserFlagSet getFlags(final long userId) {
        var existingUser = userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No user with ID [%s] exists.", userId));
        }

        var activeFlags = featureFlagRepository.findEnabledFlags()
                .stream().map(FeatureFlagDao::getName)
                .collect(toSet());

        var userDao = existingUser.get();
        if (userDao.getFlags() != null) {
            activeFlags.addAll(
                    userDao.getFlags().stream()
                            .map(FeatureFlagDao::getName)
                            .collect(toSet())
            );
        }

        return UserFlagSet.builder()
                .user(USER_DAO_TRANSFORMER.transform(userDao))
                .activeFlags(activeFlags)
                .build();
    }
}
