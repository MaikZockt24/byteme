package byteme

import grails.gorm.transactions.Transactional
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Transactional
class UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder()

    User register(String email, String rawPassword) {
        if (User.findByEmail(email)) return null
        String hashed = passwordEncoder.encode(rawPassword)
        def user = new User(email: email, passwordHash: hashed)
        user.save(flush: true)
        return user
    }

    User authenticate(String email, String rawPassword) {
        def user = User.findByEmail(email)
        if (!user) return null
        if (passwordEncoder.matches(rawPassword, user.passwordHash)) return user
        return null
    }

    boolean resetPassword(String email, String newPassword) {
        def user = User.findByEmail(email)
        if (!user) return false
        user.passwordHash = passwordEncoder.encode(newPassword)
        user.save(flush: true)
        return true
    }
    User setUsername(String email, String username) {
        def user = User.findByEmail(email)
        if (!user) return null
        user.username = username
        user.save(flush: true)
        return user
    }
    boolean deleteUser(String email) {
        def user = User.findByEmail(email)
        if (!user) return false
        user.delete(flush: true)
        return true
    }
}


