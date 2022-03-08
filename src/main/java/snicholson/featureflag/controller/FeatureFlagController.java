package snicholson.featureflag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snicholson.featureflag.entity.dto.FeatureFlag;
import snicholson.featureflag.entity.dto.UserFlag;
import snicholson.featureflag.service.FeatureFlagService;

import java.util.Collection;

@RestController
@RequestMapping("/flags")
public class FeatureFlagController {
    private final FeatureFlagService featureFlagService;

    @Autowired
    FeatureFlagController(final FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    @PostMapping
    public ResponseEntity<FeatureFlag> createFlag(
            @RequestBody
            FeatureFlag featureFlag
    ) {
        var result = featureFlagService.createFlag(featureFlag);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<UserFlag> enableFlag(long userId, String flagName) {
        var result = featureFlagService.enableFlag(userId, flagName);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<Collection<UserFlag>> getFlags(long userId) {
        var result = featureFlagService.getFlags(userId);
        return ResponseEntity.ok(result);
    }
}
