package kr.co.won.user.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user/brew-completed")
public class UserBrewCompletedNotifyController {


    @PostMapping
    public ResponseEntity<Void> notifyBrewCompleted() {
        return ResponseEntity.accepted().build();
    }


//    public record UserBrewCompleteCommand(OrderId orderId) {
//    }
}
