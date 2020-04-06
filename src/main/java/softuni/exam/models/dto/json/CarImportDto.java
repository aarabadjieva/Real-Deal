package softuni.exam.models.dto.json;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarImportDto {

    @Expose
    private String make;

    @Expose
    private String model;

    @Expose
    private int kilometers;

    @Expose
    private String registeredOn;
}
