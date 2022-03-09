package snicholson.featureflag.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import snicholson.featureflag.entity.FeatureFlagDaoTransformer;
import snicholson.featureflag.entity.FeatureFlagTransformer;
import snicholson.featureflag.entity.UserDaoTransformer;
import snicholson.featureflag.entity.dao.FeatureFlagDao;
import snicholson.featureflag.entity.dao.UserDao;
import snicholson.featureflag.entity.dto.FeatureFlag;
import snicholson.featureflag.entity.dto.User;
import snicholson.featureflag.entity.dto.UserFlag;
import snicholson.featureflag.entity.dto.UserFlagSet;
import snicholson.featureflag.repository.FeatureFlagRepository;
import snicholson.featureflag.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FeatureFlagServiceTest {

    private static final FeatureFlagTransformer FEATURE_FLAG_TRANSFORMER = new FeatureFlagTransformer();
    private static final FeatureFlagDaoTransformer FEATURE_FLAG_DAO_TRANSFORMER = new FeatureFlagDaoTransformer();
    private static final UserDaoTransformer USER_DAO_TRANSFORMER = new UserDaoTransformer();

    private FeatureFlagRepository featureFlagRepository;
    private UserRepository userRepository;
    private FeatureFlagService featureFlagService;

    @BeforeEach
    void setup() {
        featureFlagRepository = mock(FeatureFlagRepository.class);
        userRepository = mock(UserRepository.class);
        featureFlagService = new FeatureFlagService(featureFlagRepository, userRepository);
    }

    @Nested
    @DisplayName("Flag creation")
    class FlagCreation {
        @Test
        @DisplayName("Can create a new flag")
        void createNewFlag() {
            var newFlag = FeatureFlag.builder()
                    .name("Name")
                    .description("Desc")
                    .enabled(true)
                    .build();
            var newFlagDao = FEATURE_FLAG_TRANSFORMER.transform(newFlag);

            when(featureFlagRepository.findByName("Name"))
                    .thenReturn(Optional.empty());
            when(featureFlagRepository.save(newFlagDao))
                    .thenReturn(newFlagDao);

            var actual = featureFlagService.createFlag(newFlag);

            verify(featureFlagRepository).save(newFlagDao);

            assertThat(actual, is(newFlag));
        }

        @Test
        @DisplayName("Can't create a duplicate flag")
        void duplicateThrowsConflict() {
            var newFlag = FeatureFlag.builder()
                    .name("Name")
                    .description("Desc")
                    .enabled(true)
                    .build();
            var newFlagDao = FEATURE_FLAG_TRANSFORMER.transform(newFlag);

            when(featureFlagRepository.findByName("Name"))
                    .thenReturn(Optional.ofNullable(newFlagDao));

            var thrown = assertThrows(ResponseStatusException.class,
                    () -> featureFlagService.createFlag(newFlag));

            assertThat(thrown.getStatus(), is(HttpStatus.CONFLICT));
            verify(featureFlagRepository, times(0)).save(newFlagDao);
        }
    }

    @Nested
    @DisplayName("Enabling flags")
    class EnableFlags {
        private final long userId = 1L;
        private final UserDao userDao = UserDao.builder()
                .id(userId)
                .name("user")
                .build();
        private final User user = USER_DAO_TRANSFORMER.transform(userDao);
        private final String flagName = "flagName";
        private final FeatureFlagDao.FeatureFlagDaoBuilder builder = FeatureFlagDao.builder()
                .id(2L)
                .name(flagName)
                .description("description")
                .enabled(false);
        private final FeatureFlagDao flagDao = builder.build();

        @Test
        @DisplayName("Enabling a flag for a user ")
        void enableFlagForUser() {
            when(userRepository.findById(userId))
                    .thenReturn(Optional.ofNullable(userDao));
            when(featureFlagRepository.findByName(flagName))
                    .thenReturn(Optional.ofNullable(flagDao));

            var expected = UserFlag.builder()
                    .user(user)
                    .flagName(flagName)
                    .build();

            var actual = featureFlagService.enableFlag(userId, flagName);

            assertThat(actual, is(expected));
        }

        @Test
        @DisplayName("Attempting to enable a non-existent flag throws an error")
        void missingFlagThrowsNotFound() {
            when(userRepository.findById(userId))
                    .thenReturn(Optional.ofNullable(userDao));
            when(featureFlagRepository.findByName(flagName))
                    .thenReturn(Optional.empty());

            var thrown = assertThrows(ResponseStatusException.class,
                    () -> featureFlagService.enableFlag(userId, flagName));

            assertThat(thrown.getStatus(), is(HttpStatus.NOT_FOUND));
            assertThat(thrown.getMessage(), containsString(flagName));
        }

        @Test
        @DisplayName("Attempting to set a flag for a non-existent user throws an error")
        void missingUserThrowsNotFound() {
            when(userRepository.findById(userId))
                    .thenReturn(Optional.empty());

            var thrown = assertThrows(ResponseStatusException.class,
                    () -> featureFlagService.enableFlag(userId, flagName));

            assertThat(thrown.getStatus(), is(HttpStatus.NOT_FOUND));
            assertThat(thrown.getMessage(), containsString(Long.toString(userId)));
        }
    }

    @Nested
    @DisplayName("Retrieving flags")
    class FlagRetrieval {
        private final FeatureFlagDao.FeatureFlagDaoBuilder builder = FeatureFlagDao.builder();

        private final long userId = 1L;
        private final UserDao userDao = UserDao.builder()
                .id(userId)
                .name("user")
                .flags(Set.of(
                        builder.id(1L).name("flag1").build(),
                        builder.id(2L).name("flag2").build(),
                        builder.id(3L).name("flag3").build()
                ))
                .build();
        private final FeatureFlagDao globalFlag = FeatureFlagDao.builder()
                .id(4L)
                .name("globalFlag")
                .build();

        @Test
        @DisplayName("User can retrieve flags")
        void flagRetrieval() {
            when(userRepository.findById(userId))
                    .thenReturn(Optional.ofNullable(userDao));
            when(featureFlagRepository.findEnabledFlags())
                    .thenReturn(Set.of(globalFlag));

            var expected = UserFlagSet.builder()
                    .user(USER_DAO_TRANSFORMER.transform(userDao))
                    .activeFlags(Set.of(
                            "flag1", "flag2", "flag3", "globalFlag"
                    )).build();

            var actual = featureFlagService.getFlags(userId);

            assertThat(actual, is(expected));
        }

        @Test
        @DisplayName("Attempting to get flags for a non-existent user throws an error")
        void missingUserThrowsNotFound() {
            when(userRepository.findById(userId))
                    .thenReturn(Optional.empty());

            var thrown = assertThrows(ResponseStatusException.class,
                    () -> featureFlagService.getFlags(userId));

            assertThat(thrown.getStatus(), is(HttpStatus.NOT_FOUND));
            assertThat(thrown.getMessage(), containsString(Long.toString(userId)));
        }
    }
}