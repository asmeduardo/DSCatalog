package com.asmeduardo.dscatalog.services;

import com.asmeduardo.dscatalog.dtos.RoleDTO;
import com.asmeduardo.dscatalog.dtos.UserRequestDTO;
import com.asmeduardo.dscatalog.dtos.UserResponseDTO;
import com.asmeduardo.dscatalog.entities.User;
import com.asmeduardo.dscatalog.repositories.RoleRepository;
import com.asmeduardo.dscatalog.repositories.UserRepository;
import com.asmeduardo.dscatalog.services.exceptions.DatabaseException;
import com.asmeduardo.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAllPaged(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        return new UserResponseDTO(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado")));
    }

    @Transactional
    public UserResponseDTO insert(UserRequestDTO dto) {
        User user = new User();
        copyDtoToEntity(dto, user);

        user =  userRepository.save(user);
        return new UserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        try {
            User user = userRepository.getReferenceById(id);
            copyDtoToEntity(dto, user);

            if (dto.password() != null && !dto.password().isBlank()) {
                user.setPassword(passwordEncoder.encode(dto.password()));
            }

            user = userRepository.save(user);
            return new UserResponseDTO(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro de integridade referêncial");
        }
    }

    private void copyDtoToEntity(UserRequestDTO dto, User user) {
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());

        user.getRoles().clear();
        for(RoleDTO roleDto : dto.roles()) {
            user.getRoles().add(roleRepository.getReferenceById(roleDto.id()));
        }
    }
}
