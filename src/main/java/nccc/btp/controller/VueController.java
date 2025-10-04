package nccc.btp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VueController {

  @GetMapping({"/"})
  public String index() {
    return "forward:/index.html";
  }
}