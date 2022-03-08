package snicholson.featureflag.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class User {
    private final Long userId;
    private final String name;
    private final Set<FeatureFlag> flags;
}
