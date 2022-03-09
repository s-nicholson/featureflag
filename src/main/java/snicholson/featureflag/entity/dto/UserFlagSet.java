package snicholson.featureflag.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserFlagSet {
    private final User user;
    private final Set<String> activeFlags;
}
