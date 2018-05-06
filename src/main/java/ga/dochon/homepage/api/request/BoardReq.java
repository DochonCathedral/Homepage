package ga.dochon.homepage.api.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class BoardReq {
    // 일단 안 씀.
    /*
    @NotNull
    @Length(min = 1, max = 45)
    private String name;

    @NotNull
    private int idOrganization;

    @NotNull
    @Min(0)
    @Max(10)
    private short type;

    @Length(max = 45)
    private String description;
    */
}
