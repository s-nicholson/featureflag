package snicholson.featureflag.entity;

import java.util.Collection;
import java.util.stream.Collectors;

public interface Transformer<I, O> {
    O transform(I in);

    default Collection<O> transform(Collection<I> inCollection) {
        return inCollection.stream()
                .map(this::transform)
                .collect(Collectors.toList());
    }
}
