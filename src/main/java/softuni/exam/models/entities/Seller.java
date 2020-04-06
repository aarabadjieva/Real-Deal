package softuni.exam.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import softuni.exam.models.entities.enums.Rating;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sellers")
public class Seller extends BaseEntity{

    @Column(unique = true)
    @NotNull
    @Length(min = 2, max = 20)
    private String firstName;

    @Column(unique = true)
    @NotNull
    @Length(min = 2, max = 20)
    private String lastName;

    @Column(unique = true)
    @Pattern(regexp = ".*@.*\\..*")
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Rating rating;

    @Column
    @NotNull
    private String town;

    @OneToMany(mappedBy = "seller")
    private List<Offer> offers;
}
