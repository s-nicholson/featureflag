package snicholson.featureflag.entity.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "feature_flag")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureFlagDao {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private boolean enabled;
}
