package snicholson.featureflag.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import snicholson.featureflag.entity.dto.FeatureFlag;
import snicholson.featureflag.entity.dto.User;
import snicholson.featureflag.entity.dto.UserFlag;
import snicholson.featureflag.entity.dto.UserFlagSet;
import snicholson.featureflag.service.FeatureFlagService;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FeatureFlagControllerTest {

    private FeatureFlagService featureFlagService;
    private FeatureFlagController featureFlagController;

    @BeforeEach
    void setup() {
        featureFlagService = mock(FeatureFlagService.class);
        featureFlagController = new FeatureFlagController(featureFlagService);
    }

    @Nested
    @DisplayName("Use cases for admin user")
    class AdminUseCases {
        @Test
        @DisplayName("Can add a new flag")
        void createFlag() {
            var featureFlag = FeatureFlag.builder()
                    .name("Name")
                    .description("Desc")
                    .build();
            when(featureFlagService.createFlag(featureFlag))
                    .thenReturn(featureFlag);
            var flagResponse = featureFlagController.createFlag(featureFlag);

            assertThat(flagResponse.getStatusCode(), is(HttpStatus.OK));
            assertThat(flagResponse.getBody(), is(featureFlag));
        }

        @Test
        @DisplayName("Can enable a flag for a user")
        void enableFlag() {
            var userId = 1L;
            var flagName = "Flag";
            var userFlag = UserFlag.builder()
                    .user(User.builder().userId(userId).build())
                    .flagName(flagName)
                    .build();
            when(featureFlagService.enableFlag(userId, flagName))
                    .thenReturn(userFlag);
            var flagResponse = featureFlagController.enableFlag(userId, flagName);

            assertThat(flagResponse.getStatusCode(), is(HttpStatus.OK));
            assertThat(flagResponse.getBody(), is(userFlag));
        }
    }

    @Nested
    @DisplayName("Use cases for normal user")
    class UserUseCases {
        @Test
        @DisplayName("Can retrieve all flags for user")
        void fetchFlags() {
            var userId = 1L;
            var userFlagSet = UserFlagSet.builder()
                    .user(User.builder().userId(userId).build())
                    .activeFlags(Set.of("flagName"))
                    .build();
            when(featureFlagService.getFlags(userId))
                    .thenReturn(userFlagSet);
            var flagResponse = featureFlagController.getFlags(userId);

            assertThat(flagResponse.getStatusCode(), is(HttpStatus.OK));
            assertThat(flagResponse.getBody(), is(userFlagSet));
        }
    }
}