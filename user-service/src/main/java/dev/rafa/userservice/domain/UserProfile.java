package dev.rafa.userservice.domain;

import jakarta.persistence.*;
import lombok.*;

@With
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NamedEntityGraph(
        name = "UserProfile.fullDetails",
        attributeNodes = {@NamedAttributeNode("user"), @NamedAttributeNode("profile")}
)
public class UserProfile {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Profile profile;

}
