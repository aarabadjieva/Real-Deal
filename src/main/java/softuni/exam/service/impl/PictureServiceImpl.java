package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.common.Constants;
import softuni.exam.models.dto.json.PictureImportDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Picture;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    ;private final CarRepository carRepository;
    private final ModelMapper mapper;
    private final FileUtil fileUtil;
    private final Gson gson;
    private final ValidationUtil validator;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, CarRepository carRepository, ModelMapper mapper, FileUtil fileUtil, Gson gson, ValidationUtil validator) {
        this.pictureRepository = pictureRepository;
        this.carRepository = carRepository;
        this.mapper = mapper;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.validator = validator;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count()>0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return this.fileUtil.readFile(Constants.PATH_TO_JSON_FILES + "pictures.json");
    }

    @Override
    public String importPictures() throws IOException {
        PictureImportDto[] pictureImportDtos = this.gson.fromJson(readPicturesFromFile(), PictureImportDto[].class);
        List<String> messages = new ArrayList<>();
        for (PictureImportDto pictureDto : pictureImportDtos) {
            Car car = this.carRepository.findById(pictureDto.getCar()).orElse(null);
            Picture existingPic = this.pictureRepository.findByName(pictureDto.getName()).orElse(null);
            Picture picture = this.mapper.map(pictureDto, Picture.class);
            picture.setCar(car);
            picture.setDateAndTime(LocalDateTime.parse(pictureDto.getDateAndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            if (car==null||existingPic!=null||!this.validator.isValid(picture)){
                messages.add(String.format(Constants.INVALID_DATA_MESSAGE,
                        picture.getClass().getSimpleName().toLowerCase()));
                continue;
            }
            this.pictureRepository.saveAndFlush(picture);
            messages.add(String.format(Constants.SUCCESSFULLY_IMPORTED_MESSAGE,
                    picture.getClass().getSimpleName().toLowerCase(),
                    picture.getName(), ""));
        }
        return String.join("\n", messages);
    }
}
