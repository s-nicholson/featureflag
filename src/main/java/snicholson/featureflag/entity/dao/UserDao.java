package snicholson.featureflag.entity.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDao {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String name;

    @ManyToMany
    @JoinTable(name="USER_FLAGS")
    private Set<FeatureFlagDao> flags;
}
