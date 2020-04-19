package pl.termosteam.kinex.controller.administration.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.termosteam.kinex.dto.UserRequestDto;
import pl.termosteam.kinex.dto.UserResponseDto;
import pl.termosteam.kinex.service.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/admin/account")
public class AccountsController {
    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "user/{usernameOrEmail}")
    public UserResponseDto getUserInformation(@PathVariable String usernameOrEmail) {
        return userService.get(usernameOrEmail);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping(path = "user")
    public String putUserInformation(@RequestBody @Valid UserRequestDto userRequestDTO) {
        return userService.update(userRequestDTO);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping(path = "user/{usernameOrEmail}")
    public String putUserInformation(@RequestBody @Valid UserRequestDto userRequestDTO, @PathVariable String usernameOrEmail) {
        return userService.update(userRequestDTO, usernameOrEmail);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(path = "user")
    public String deleteUserInformation() {
        return userService.delete();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(path = "user/{usernameOrEmail}")
    public String deleteUserInformation(@PathVariable String usernameOrEmail) {
        return userService.delete(usernameOrEmail);
    }
}
