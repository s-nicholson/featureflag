package snicholson.featureflag.entity;

import snicholson.featureflag.entity.dao.UserDao;
import snicholson.featureflag.entity.dto.User;

public class UserDaoTransformer implements Transformer<UserDao, User> {

    @Override
    public User transform(UserDao in) {
        if (in == null) {
            return null;
        }
        return User.builder()
                .userId(in.getId())
                .name(in.getName())
                .build();
    }
}
