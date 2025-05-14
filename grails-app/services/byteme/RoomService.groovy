package byteme

import grails.gorm.transactions.Transactional
import org.springframework.security.crypto.bcrypt.BCrypt

@Transactional
class RoomService {
    Room createRoom(String rawPassword, String name) {
        String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt())
        new Room(roomName:name, roomPassword:hash).save(flush:true)
    }

    Room joinRoom(Long id, String rawPassword, User user) {
        Room r = Room.get(id)
        if(!r) throw new IllegalArgumentException('404')
        if(!BCrypt.checkpw(rawPassword, r.roomPassword)) throw new SecurityException('Invalid password')
        r.addToPlayers(user).save(flush:true)
    }
}