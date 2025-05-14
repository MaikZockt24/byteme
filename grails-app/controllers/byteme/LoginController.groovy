package byteme

class LoginController {
    UserService userService

    def index() {
        render view:'login'
    }

    def authenticate() {
        User u = userService.authenticate(params.email, params.password)
        if(u) {
            session.user = u
            redirect controller:'room', action:'lobby'
        } else {
            flash.error = 'Ungültige Anmeldedaten'
            render view:'login'
        }
    }
}