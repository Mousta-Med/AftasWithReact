package com.med.aftas.serverside.service.impl;

import com.med.aftas.serverside.dto.UserDto;
import com.med.aftas.serverside.dto.AuthenticationRequest;
import com.med.aftas.serverside.dto.respDto.UserRespDto;
import com.med.aftas.serverside.enums.Role;
import com.med.aftas.serverside.enums.Status;
import com.med.aftas.serverside.exception.ResourceNotFoundException;
import com.med.aftas.serverside.jwt.JWTUtil;
import com.med.aftas.serverside.dto.respDto.AuthenticationResponse;
import com.med.aftas.serverside.model.User;
import com.med.aftas.serverside.repository.UserRepository;
import com.med.aftas.serverside.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(UserDto userDto){
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setStatus(Status.PENDING);
        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user);
         return new AuthenticationResponse(token, modelMapper.map(user, UserRespDto.class));
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest userDto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getUsername(),
                        userDto.getPassword()
                )
        );
        User user = userRepository.findByUsername(userDto.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User Not found with this: "));
        String token = jwtUtil.generateToken(user);
        return new AuthenticationResponse(token, modelMapper.map(user, UserRespDto.class));
    }



    @Override
    public UserRespDto save(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setStatus(Status.PENDING);
        return modelMapper.map(userRepository.save(user), UserRespDto.class);
    }

    @Override
    public void delete(Integer id) {
        userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not found with this: " + id));
        userRepository.deleteById(id);
    }

    @Override
    public UserRespDto update(Integer id, UserDto userDto) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not found with this: " + id));
        BeanUtils.copyProperties(userDto, existingUser);
        existingUser.setNum(id);
        return modelMapper.map(userRepository.save(existingUser), UserRespDto.class);
    }

    @Override
    public UserRespDto findOne(Integer id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserRespDto.class)).orElseThrow(() -> new ResourceNotFoundException("User Not found with this: " + id));
    }

    @Override
    public List<UserRespDto> findAll() {
        return userRepository.findByRoleIsNot(Role.ROLE_MANAGER).stream().map(user -> modelMapper.map(user, UserRespDto.class)).collect(Collectors.toList());
    }

    @Override
    public Page<UserRespDto> findWithPagination(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(user -> modelMapper.map(user, UserRespDto.class));
    }

    @Override
    public List<UserRespDto> findByNameOrFamilyName(String query) {
        return userRepository.findUserByNameContainingIgnoreCaseOrFamilyNameContainingIgnoreCase(query, query)
                .stream().map(user -> modelMapper.map(user, UserRespDto.class)).collect(Collectors.toList());
    }

    @Override
    public String updateStatus(Integer userNum, String role) {
         User exictingUser = userRepository.findById(userNum).orElseThrow(() -> new ResourceNotFoundException("User Not found with this: " + userNum));
         exictingUser.setStatus(Status.valueOf(role));
         userRepository.save(exictingUser);
         return "Status Updated TO " + role + " Successfully";
    }
}
