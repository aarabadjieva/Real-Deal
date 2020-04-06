package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.common.Constants;
import softuni.exam.models.dto.xml.SellerImportDto;
import softuni.exam.models.dto.xml.SellerImportRootDto;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final ModelMapper mapper;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;
    private final ValidationUtil validator;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository, ModelMapper mapper, FileUtil fileUtil, XmlParser xmlParser, ValidationUtil validator) {
        this.sellerRepository = sellerRepository;
        this.mapper = mapper;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
        this.validator = validator;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count()>0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return this.fileUtil.readFile(Constants.PATH_TO_XML_FILES + "sellers.xml");
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        SellerImportRootDto sellerImportRootDto = this.xmlParser.parseXml(SellerImportRootDto.class,
                Constants.PATH_TO_XML_FILES + "sellers.xml");
        List<String> messages = new ArrayList<>();
        for (SellerImportDto sellerDto : sellerImportRootDto.getSellers()) {
            Seller existingSeller = this.sellerRepository.findByEmail(sellerDto.getEmail()).orElse(null);
            Seller seller = this.mapper.map(sellerDto, Seller.class);
            if (existingSeller!=null||!this.validator.isValid(seller)){
                messages.add(String.format(Constants.INVALID_DATA_MESSAGE,
                        seller.getClass().getSimpleName().toLowerCase()));
                continue;
            }
            this.sellerRepository.saveAndFlush(seller);
            messages.add(String.format(Constants.SUCCESSFULLY_IMPORTED_MESSAGE,
                    seller.getClass().getSimpleName().toLowerCase(),
                    seller.getLastName(), seller.getEmail()));
        }
        return String.join("\n", messages);
    }
}
