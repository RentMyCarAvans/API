package avd.inf.jdm.rentmycar.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor


public class ImageDto {

    private Long id;

    @Valid
    @NotNull(message = "ENTER VALID DATA")
    private String name;

    private String bodyContentInHtml;

    private String documentLogo;

    public ImageDto(Long id,String name,String documentLogo,String bodyContentInHtml){
        this.id=id;
        this.name=name;
        this.documentLogo=documentLogo;
        this.bodyContentInHtml=bodyContentInHtml;
    }
    @Override
    public String toString(){
        return this.name;
    }
}
