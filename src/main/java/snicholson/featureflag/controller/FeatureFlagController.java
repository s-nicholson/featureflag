package snicholson.featureflag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snicholson.featureflag.service.FeatureFlagService;

@RestController
@RequestMapping("/flags")
public class FeatureFlagController {
    private final FeatureFlagService featureFlagService;

    @Autowired
    FeatureFlagController(final FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }
}
