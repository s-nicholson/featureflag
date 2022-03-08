package snicholson.featureflag.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFlag {
    private final User user;
    private final FeatureFlag flag;
}
