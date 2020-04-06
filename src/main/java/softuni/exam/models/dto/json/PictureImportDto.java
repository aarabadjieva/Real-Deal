package softuni.exam.models.dto.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PictureImportDto {

    @Expose
    private String name;

    @Expose
    private String dateAndTime;

    @Expose
    @SerializedName("car")
    private int car;
}
