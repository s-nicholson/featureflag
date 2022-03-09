package snicholson.featureflag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snicholson.featureflag.entity.dto.FeatureFlag;
import snicholson.featureflag.entity.dto.UserFlag;
import snicholson.featureflag.entity.dto.UserFlagSet;
import snicholson.featureflag.service.FeatureFlagService;

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

    @PatchMapping("/{userId}/{flagName}")
    public ResponseEntity<UserFlag> enableFlag(
            @PathVariable
            final long userId,
            @PathVariable
            final String flagName) {
        var result = featureFlagService.enableFlag(userId, flagName);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserFlagSet> getFlags(
            @PathVariable
            final long userId
    ) {
        var result = featureFlagService.getFlags(userId);
        return ResponseEntity.ok(result);
    }
}
