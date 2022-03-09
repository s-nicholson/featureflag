package snicholson.featureflag.entity;

import snicholson.featureflag.entity.dao.FeatureFlagDao;
import snicholson.featureflag.entity.dto.FeatureFlag;

public class FeatureFlagTransformer implements Transformer<FeatureFlag, FeatureFlagDao> {

    @Override
    public FeatureFlagDao transform(FeatureFlag in) {
        if (in == null) {
            return null;
        }
        return FeatureFlagDao.builder()
                .name(in.getName())
                .description(in.getDescription())
                .enabled(in.isEnabled())
                .build();
    }
}
