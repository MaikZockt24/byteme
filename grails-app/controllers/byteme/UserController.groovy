package byteme
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import byteme.JWTUtil

@Transactional
class UserController {
    static responseFormats = ['json']

    UserService userService

    // Helper-Methode für JWT
    private Long getUserIdFromRequest() { // ADD
        def authHeader = request.getHeader("Authorization")
        if (authHeader?.startsWith("Bearer ")) {
            def token = authHeader.substring(7)
            return JWTUtil.getUserIdFromToken(token)
        }
        return null
    }

    // Registrierung
    def register() {
        def json = request.JSON
        try {
            if (!json.email || !json.password) {
                json.status = 400
                render ([error: "E-Mail und/oder Passwort erforderlich"] as JSON)
                return
            }
        } catch (Exception e) {
            json.status = 400
            render ([error: "Ungültige Anfrage: ${e.message}"] as JSON)
            return
        }
        def user = userService.register(json.email, json.password)
        if (!user) {
            json.status = 400
            render ([error: "E-Mail existiert bereits"] as JSON)
        } else {
            println("User gefunden: ${user.id}")
            def token = JWTUtil.generateToken(user.id)
            println("User gefunden: ${user.id}")
            json.status = 201
            render ([token: token] as JSON)
        }
    }

    // Login
    def login() {
        def json = request.JSON
        try {
            if (!json.email || !json.password) {
                json.status = 400
                render ([error: "E-Mail und/oder Passwort erforderlich"] as JSON)
                return
            }
        } catch (Exception e) {
            json.status = 400
            render ([error: "Ungültige Anfrage: ${e.message}"] as JSON)
            return
        }
        def user = userService.authenticate(json.email, json.password)
        if (!user) {
            json.status = 403 
            render ([error: "Ungültige E-Mail oder Passwort"] as JSON)
        } else {
            println("User gefunden: ${user.id}")
            def token = JWTUtil.generateToken(user.id)
            println("User gefunden: ${user.id}")
            json.status = 200 
            render ([token: token] as JSON)
        }
    }

    // Passwort zurücksetzen
    def forgot() {
        def json = request.JSON
        try {
            if (!json.email || !json.newPassword) {
                json.status = 400
                render ([error: "E-Mail und/oder neues Passwort erforderlich"] as JSON)
                return
            }
        } catch (Exception e) {
            json.status = 400
            render ([error: "Ungültige Anfrage: ${e.message}"] as JSON)
            return
        }
        boolean success = userService.resetPassword(json.email, json.newPassword)
        if (!success) {
            json.status = 404
            render ([error: "E-Mail nicht gefunden"] as JSON)
        } else {
            json.status = 200
            render([message: "Passwort erfolgreich geändert"] as JSON)
        }
    }

    // Username setzen/ändern
    def setUsername() {
        def json = request.JSON
        try {
            if (!json.email || !json.username) {
                json.status = 400
                render ([error: "E-Mail und/oder Username erforderlich"] as JSON)
                return
            }
        } catch (Exception e) {
            json.status = 400
            render ([error: "Ungültige Anfrage: ${e.message}"] as JSON)
            return
        }
        def userId = getUserIdFromRequest()
        def user = User.get(userId)
        if (!user) {
            json.status = 404
            render ([error: "User nicht gefunden"] as JSON)
        } else {
            user.username = json.username
            user.save(flush: true)
            json.status = 200
            render ([message: "Dein Username lautet: ${user.username}", username: user.username] as JSON)
        }
    }

    // Logout
    def logout() {
        json.status = 200
        render ([message: "Logout erfolgreich"] as JSON)
    }

    // Account löschen
    def delete() {
        def json = request.JSON
        try {
            if (!json.email) {
                json.status = 400
                render ([error: "E-Mail erforderlich"] as JSON)
                return
            }
        } catch (Exception e) {
            json.status = 400
            render ([error: "Ungültige Anfrage: ${e.message}"] as JSON)
            return
        }
        def user = User.findByEmail(json.email)
        if (!user) {
            json.status = 404
            render ([error: "User nicht gefunden"] as JSON)
        } else {
            user.delete(flush: true)
            json.status = 200
            render ([message: "Account erfolgreich gelöscht"] as JSON)
        }
    }
}
