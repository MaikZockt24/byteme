package byteme

import grails.converters.JSON

class UserController {
    static allowedMethods = [setUsername: "POST"]

    // POST /username  { username, avatar }
    def setUsername() {
        def j = request.JSON
        User u = session.user
        u.username = j.username
        u.avatar   = j.avatar
        u.save(flush: true)
        render([success: true, next: "/lobby.html"] as JSON)
    }
}
