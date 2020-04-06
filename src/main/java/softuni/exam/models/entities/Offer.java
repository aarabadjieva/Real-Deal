package softuni.exam.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {

    @Column
    @DecimalMin("0")
    @NotNull
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    @NotNull
    @Length(min = 5)
    private String description;

    @Column
    private boolean hasGoldStatus;

    @Column
    private LocalDate addedOn;

    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Seller seller;

    @ManyToMany
    @JoinTable(name = "offers_pictures",
    joinColumns = {@JoinColumn(name = "offer_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "picture_id", referencedColumnName = "id")})
    private List<Picture> pictures;
}
