package byteme

import grails.converters.JSON
import org.springframework.security.crypto.bcrypt.BCrypt

class ForgotPasswordController {
    def update() {
        User u = User.findByEmail(params.email)
        if(!u) {
            render([success:false,error:'E-Mail nicht registriert'] as JSON)
            return
        }
        u.password = BCrypt.hashpw(params.newPassword, BCrypt.gensalt())
        u.save(flush:true)
        render([success:true] as JSON)
    }
}