package byteme
import grails.converters.JSON
import grails.gorm.transactions.Transactional

@Transactional
class UserController {
    static responseFormats = ['json']

    UserService userService

    // Registrierung
    def register() {
        def json = request.JSON
        if (!json.email || !json.password || !json.passwordConfirmation) {
        render status: 400, json: [error: "E-Mail, Passwort und Passwortbestätigung erforderlich"]
        return
        }
        if (json.password != json.passwordConfirmation) {
        render status: 400, json: [error: "Passwörter stimmen nicht überein"]
        return
        }
        def user = userService.register(json.email, json.password)
        if (!user) {
            render status: 400, json: [error: "E-Mail existiert bereits"]
        } else {
            render status: 201, json: [id: user.id, email: user.email, username: user.username, registeredAt: user.registeredAt]
        }
    }

    // Login
    def login() {
        def json = request.JSON
        if (!json.email || !json.password) {
            render status: 400, json: [error: "E-Mail und/oder Passwort erforderlich"]
            return
        }
        def user = userService.authenticate(json.email, json.password)
        if (!user) {
            render status: 403, json: [error: "Ungültige E-Mail oder Passwort"]
        } else {
            // Kein Token/JWT für Einfachheit. Bei echtem System hier JWT generieren!
            render status: 200, json: [id: user.id, email: user.email, username: user.username, registeredAt: user.registeredAt]
        }
    }

    // Passwort zurücksetzen
    def forgot() {
        def json = request.JSON
        if (!json.email || !json.newPassword) {
            render status: 400, json: [error: "E-Mail und/oder neues Passwort erforderlich"]
            return
        }
        boolean success = userService.resetPassword(json.email, json.newPassword)
        if (!success) {
            render status: 404, json: [error: "E-Mail nicht gefunden"]
        } else {
            render status: 200, json: [message: "Passwort erfolgreich geändert"]
        }
    }

    // Username setzen/ändern
    def setUsername() {
        def json = request.JSON
        if (!json.email || !json.username) {
            render status: 400, json: [error: "E-Mail und/oder Username erforderlich"]
            return
        }
        def user = User.findByEmail(json.email)
        if (!user) {
            render status: 404, json: [error: "User nicht gefunden"]
        } else {
            user.username = json.username
            user.save(flush: true)
            render status: 200, json: [id: user.id, email: user.email, username: user.username, registeredAt: user.registeredAt]
        }
    }

    // Account löschen
    def delete() {
        def json = request.JSON
        if (!json.email) {
            render status: 400, json: [error: "E-Mail erforderlich"]
            return
        }
        def user = User.findByEmail(json.email)
        if (!user) {
            render status: 404, json: [error: "User nicht gefunden"]
        } else {
            user.delete(flush: true)
            render status: 200, json: [message: "Account erfolgreich gelöscht"]
        }
    }
}