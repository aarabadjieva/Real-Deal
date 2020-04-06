package softuni.exam.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity{

    @Column(unique = true)
    @Length(min = 2, max = 20)
    @NotNull
    private String name;

    @Column
    @NotNull
    private LocalDateTime dateAndTime;

    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    @ManyToMany(mappedBy = "pictures")
    private List<Offer> offers;
}
