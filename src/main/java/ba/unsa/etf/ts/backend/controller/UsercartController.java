package ba.unsa.etf.ts.backend.controller;

import ba.unsa.etf.ts.backend.services.UsercartService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsercartController {

    private final UsercartService usercartService;

    public UsercartController(UsercartService usercartService) {
        this.usercartService = usercartService;
    }


}
