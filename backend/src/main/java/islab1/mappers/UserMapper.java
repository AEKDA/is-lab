package islab1.mappers;

import org.springframework.stereotype.Component;

import islab1.exceptions.ConvertionException;
import islab1.models.DTO.UserDTO;
import islab1.models.auth.User;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserMapper {

    public User toEntity(UserDTO dto) throws ConvertionException {
        try {
            User user = new User();
            user.setUsername(dto.getUsername());
            user.setPassword(dto.getPassword());
            return user;
        } catch (ConvertionException e) {
            throw e;
        }
    }

    public UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword("");
        return dto;
    }

}
