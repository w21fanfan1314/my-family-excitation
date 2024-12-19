package family.excitation.service

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo

class GameWebSocket {

    @MessageMapping('/match')
    @SendTo('/topic/match')
    def match(data) {
        println "match: $data"
    }
}
