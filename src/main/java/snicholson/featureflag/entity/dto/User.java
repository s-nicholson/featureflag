package snicholson.featureflag.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private final Long userId;
    private final String name;
}
