package com.med.aftas.serverside.service;

import com.med.aftas.serverside.dto.UserDto;
import com.med.aftas.serverside.dto.AuthenticationRequest;
import com.med.aftas.serverside.dto.respDto.UserRespDto;
import com.med.aftas.serverside.dto.respDto.AuthenticationResponse;

import java.util.List;

public interface UserService extends BaseService<UserDto, Integer, UserRespDto> {
    List<UserRespDto> findByNameOrFamilyName(String query);
    String updateStatus(Integer userNum, String role);
    AuthenticationResponse register(UserDto userDto);
    AuthenticationResponse login(AuthenticationRequest userDto);
}
