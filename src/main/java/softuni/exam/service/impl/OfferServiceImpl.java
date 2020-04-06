package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.common.Constants;
import softuni.exam.models.dto.xml.OfferImportDto;
import softuni.exam.models.dto.xml.OfferImportRootDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Offer;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final SellerRepository sellerRepository;
    private final CarRepository carRepository;
    private final ModelMapper mapper;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;
    private final ValidationUtil validator;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, SellerRepository sellerRepository, CarRepository carRepository, ModelMapper mapper, FileUtil fileUtil, XmlParser xmlParser, ValidationUtil validator) {
        this.offerRepository = offerRepository;
        this.sellerRepository = sellerRepository;
        this.carRepository = carRepository;
        this.mapper = mapper;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
        this.validator = validator;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count()>0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return this.fileUtil.readFile(Constants.PATH_TO_XML_FILES + "offers.xml");
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        OfferImportRootDto offerImportRootDto = this.xmlParser.parseXml(
                OfferImportRootDto.class, Constants.PATH_TO_XML_FILES + "offers.xml");
        List<String> messages = new ArrayList<>();
        for (OfferImportDto offerDto : offerImportRootDto.getOffers()) {
            Car car = this.carRepository.findById(offerDto.getCarXmlDto().getId()).orElse(null);
            Seller seller = this.sellerRepository.findById(offerDto.getSellerXmlDto().getId()).orElse(null);
            LocalDate addedOn = LocalDate.parse(offerDto.getAddedOn(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Offer existingOffer = this.offerRepository.findByDescriptionAndAddedOn(offerDto.getDescription(), addedOn).orElse(null);
            Offer offer = this.mapper.map(offerDto, Offer.class);
            offer.setCar(car);
            offer.setSeller(seller);
            offer.setAddedOn(addedOn);
            if (car==null||seller==null||!this.validator.isValid(offer)){
                messages.add(String.format(Constants.INVALID_DATA_MESSAGE,
                        offer.getClass().getSimpleName().toLowerCase()));
                continue;
            }
            this.offerRepository.saveAndFlush(offer);
            messages.add(String.format(Constants.SUCCESSFULLY_IMPORTED_MESSAGE,
                    offer.getClass().getSimpleName().toLowerCase(),
                    offer.getAddedOn(), offer.isHasGoldStatus()));
        }
        return String.join("\n", messages);
    }
}
