package org.api.rest_api_grupo2.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ValidationErrorDto {
    @JsonProperty("error")
    private String error;

    @JsonProperty("validation_messages")
    private List<String> validationMessages;

    @JsonProperty("status")
    private Integer status;
}