package snicholson.featureflag.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeatureFlag {
    private final String name;
    private final String description;
    private final boolean enabled;
}
