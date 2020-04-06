package softuni.exam.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cars")
public class Car extends BaseEntity{

    @Column
    @NotNull
    @Length(min = 2, max = 20)
    private String make;

    @Column
    @NotNull
    @Length(min = 2, max = 20)
    private String model;

    @Column
    @NotNull
    @Positive
    private int kilometers;

    @NotNull
    private LocalDate registeredOn;

    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER)
    private List<Picture> pictures;

    @OneToMany(mappedBy = "car")
    private List<Offer> offers;
}
