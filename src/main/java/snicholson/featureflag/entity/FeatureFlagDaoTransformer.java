package snicholson.featureflag.entity;

import snicholson.featureflag.entity.dao.FeatureFlagDao;
import snicholson.featureflag.entity.dto.FeatureFlag;

public class FeatureFlagDaoTransformer implements Transformer<FeatureFlagDao, FeatureFlag> {
    @Override
    public FeatureFlag transform(FeatureFlagDao in) {
        if (in == null) {
            return null;
        }
        return FeatureFlag.builder()
                .name(in.getName())
                .description(in.getDescription())
                .enabled(in.isEnabled())
                .build();
    }
}
