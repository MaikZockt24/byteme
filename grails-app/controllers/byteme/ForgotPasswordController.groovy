package byteme

import grails.converters.JSON
import org.springframework.security.crypto.bcrypt.BCrypt

class ForgotPasswordController {
    static allowedMethods = [update: "POST"]

    // POST /forgot  { email, newPassword }
    def update() {
        def j = request.JSON
        User u = User.findByEmail(j.email)
        if (!u) {
            render(status:404, [success: false, error: "E-Mail nicht registriert"] as JSON)
            return
        }
        u.password = BCrypt.hashpw(j.newPassword, BCrypt.gensalt())
        u.save(flush: true)
        render([success: true] as JSON)
    }
}
