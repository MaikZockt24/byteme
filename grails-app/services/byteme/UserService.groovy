package byteme

import grails.gorm.transactions.Transactional
import org.springframework.security.crypto.bcrypt.BCrypt

@Transactional
class UserService {
    User createUser(String email, String rawPassword) {
        String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt())
        new User(email: email, password: hash).save(flush:true)
    }

    User authenticate(String email, String rawPassword) {
        User u = User.findByEmail(email)
        if(u && BCrypt.checkpw(rawPassword, u.password)) return u
        null
    }
}