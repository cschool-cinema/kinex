package pl.termosteam.kinex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtActivationRequestDto {
    private String username;
    @JsonProperty("activation_token")
    private String activationToken;
}
