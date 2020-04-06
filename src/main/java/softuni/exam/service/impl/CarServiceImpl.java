package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.common.Constants;
import softuni.exam.models.dto.json.CarImportDto;
import softuni.exam.models.entities.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ModelMapper mapper;
    private final FileUtil fileUtil;
    private final Gson gson;
    private final ValidationUtil validator;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, ModelMapper mapper, FileUtil fileUtil, Gson gson, ValidationUtil validator) {
        this.carRepository = carRepository;
        this.mapper = mapper;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.validator = validator;
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count()>0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return this.fileUtil.readFile(Constants.PATH_TO_JSON_FILES + "cars.json");
    }

    @Override
    public String importCars() throws IOException {
        CarImportDto[] carImportDtos = this.gson.fromJson(readCarsFileContent(), CarImportDto[].class);
        List<String> messages = new ArrayList<>();
        for (CarImportDto carDto : carImportDtos) {
            Car existingCar = this.carRepository.findByMakeAndModelAndKilometers(carDto.getMake(),
                    carDto.getModel(), carDto.getKilometers()).orElse(null);
            Car car = mapper.map(carDto, Car.class);
            car.setRegisteredOn(LocalDate.parse(carDto.getRegisteredOn(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            if (existingCar!=null||!this.validator.isValid(car)){
                messages.add(String.format(Constants.INVALID_DATA_MESSAGE,
                        car.getClass().getSimpleName().toLowerCase()));
                continue;
            }
            this.carRepository.saveAndFlush(car);
            messages.add(String.format(Constants.SUCCESSFULLY_IMPORTED_MESSAGE,
                    car.getClass().getSimpleName().toLowerCase(),
                    car.getMake(), car.getModel()));
        }
        return String.join("\n", messages);
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        List<Car> carsByPictures = this.carRepository.findByCarsCountOrderByCarsCountAndMakeAsc();
        List<String> result = new ArrayList<>();
        for (Car car : carsByPictures) {
            result.add(String.format("Car make - %s, model - %s\n" +
                    "\tKilometers - %d\n" +
                    "\tRegistered on - %s\n" +
                    "\tNumber of pictures - %d\n",
                    car.getMake(), car.getModel(), car.getKilometers(),
                    car.getRegisteredOn(), car.getPictures().size()));
        }
        return String.join("", result);
    }
}
