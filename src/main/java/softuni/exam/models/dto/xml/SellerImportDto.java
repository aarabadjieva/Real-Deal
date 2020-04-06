package softuni.exam.models.dto.xml;

import lombok.Getter;
import lombok.Setter;
import softuni.exam.models.entities.enums.Rating;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement(name = "seller")
@XmlAccessorType(XmlAccessType.FIELD)
public class SellerImportDto {

    @XmlElement(name = "first-name")
    private String firstName;

    @XmlElement(name = "last-name")
    private String lastName;

    @XmlElement
    private String email;

    @XmlElement
    private Rating rating;

    @XmlElement
    private String town;
}
