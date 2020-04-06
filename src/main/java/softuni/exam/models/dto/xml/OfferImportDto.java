package softuni.exam.models.dto.xml;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Getter
@Setter
@XmlRootElement(name = "offer")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferImportDto {

    @XmlElement
    private String description;

    @XmlElement
    private BigDecimal price;

    @XmlElement(name = "added-on")
    private String addedOn;

    @XmlElement(name = "has-gold-status")
    private boolean hasGoldStatus;

    @XmlElement(name = "car")
    private CarXmlDto carXmlDto;

    @XmlElement(name = "seller")
    private SellerXmlDto sellerXmlDto;
}
