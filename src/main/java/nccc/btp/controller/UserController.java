package nccc.btp.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nccc.btp.dto.NcccUserDto;

@RestController
@RequestMapping("/user")
public class UserController {

  @GetMapping("/getCurrentUser")
  public NcccUserDto getCurrentUser(@AuthenticationPrincipal NcccUserDto user) {
    return user;
  }
}
