package pl.termosteam.kinex.controller.administration.accounts;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.termosteam.kinex.dto.UserResponseDto;

@AllArgsConstructor
@RestController
@RequestMapping("api/admin/account")
public class AccountsController {
    /*
     * CRUD:
     *   create (Insert) -> POST REST API    -> account registration
     *   read   (Select) -> GET REST API     -> get information about account
     *   update (Update) -> PUT REST API     -> update account information's
     *   delete (Delete) -> DELETE REST API  -> delete account
     */

    /*
     *  GET REST API:
     *  1. USER can get only his account
     *  2. MANAGER can get only his account
     *  3. ADMINISTRATOR can get only his account or MANAGERS or USERS
     *  4. OWNER can do anything with account's information's
     **/

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public UserResponseDto getUserInformation() {
        return null;
    }

    /*
     *  PUT REST API (update):
     *  1. USER can update only his account
     *  2. MANAGER can update only his account
     *  3. ADMINISTRATOR can update only his account and promote account's GRANTS till manages
     *  4. OWNER can do anything with account's information's
     *  5. SPECIAL CASE: reset passwords:
     *      5.1. USERS and MANAGERS can reset only personal passwords
     *      5.2. ADMINISTRATOR and OWNER can reset personal and other lower grant's roles password's
     *      5.3. OWNER can reset every password
     **/

    /*
     *  DELETE REST API:
     *  1. USER can delete only his account
     *  2. MANAGER can delete only his account
     *  3. ADMINISTRATOR can delete only his account or MANAGERS or USERS
     *  4. OWNER can do anything with account's information's
     **/
}
