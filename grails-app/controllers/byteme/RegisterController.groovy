package byteme

import grails.converters.JSON

class RegisterController {
    UserService userService

    static allowedMethods = [save: "POST"]

    // POST /register  { email, password }
    def save() {
        def j = request.JSON
        try {
            User u = userService.createUser(j.email, j.password)
            session.user = u
            render([success: true, next: "/username.html"] as JSON)
        } catch(Exception e) {
            render(status:400, [success: false, error: e.message] as JSON)
        }
    }
}
