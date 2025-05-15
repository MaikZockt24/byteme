package byteme

import grails.converters.JSON

class LoginController {
    UserService userService

    static allowedMethods = [authenticate: "POST"]

    // POST /login  { username, password }
    def authenticate() {
        def j = request.JSON
        User u = userService.authenticate(j.username, j.password)
        if (u) {
            session.user = u
            render([success: true, next: "/username.html"] as JSON)
        } else {
            render(status:401, [success: false, error: "Ungültige Anmeldedaten"] as JSON)
        }
    }
}
