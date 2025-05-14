package byteme

class RegisterController {
    UserService userService

    def index() {
        render view:'register'
    }

    def save() {
        User u = userService.createUser(params.email, params.password)
        session.user = u
        redirect controller:'room', action:'lobby'
    }
}