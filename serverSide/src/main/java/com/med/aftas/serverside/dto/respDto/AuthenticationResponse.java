package com.med.aftas.serverside.dto.respDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;

    private UserRespDto userRespDto;

}
