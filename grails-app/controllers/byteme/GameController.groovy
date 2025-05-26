package byteme

import grails.rest.*
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import byteme.JWTUtil

@Transactional
class GameController {
    static responseFormats = ['json']
    GameService gameService

    // Helper-Methode für JWT
    private Long getUserIdFromRequest() {
        def authHeader = request.getHeader("Authorization")
        if (authHeader?.startsWith("Bearer ")) {
            def token = authHeader.substring(7)
            def userId = JWTUtil.getUserIdFromToken(token)
            return userId
        }
        return null
    }

    // Neues Spiel erstellen
    def create() {
        def json = request.JSON
        def userId = getUserIdFromRequest()
        def user = User.get(userId)
        if (!user) {
            json.status = 403
            render ([error: "Nicht authentifiziert"] as JSON)
            return
        }
        def game = gameService.createGame(user, json.code)
        if (!game) {
            json.status = 400
            render ([error: "Spiel konnte nicht erstellt werden"] as JSON)
        } else {
            json.status = 201
            render ([gameId: game.id, status: game.status, players: game.players.collect { [id: it.id, username: it.username] }, createdAt: game.createdAt] as JSON)
        }
    }
    // Spiel beitreten
    def join() {
        def userId = getUserIdFromRequest()
        def user = User.get(userId)
        if (!user) {
            json.status = 403
            render ([error:'Nicht authentifiziert'] as JSON)
            return
        }
        def json = request.JSON
        Game game = Game.get(json.gameId as Long)
        if (!game) {
            json.status = 404
            render ([error:'Raum nicht gefunden'] as JSON)
            return
        }
        // 1) Prüfen ob Raum voll
        if (game.players.size() >= 2) {
            json.status = 400
            render ([error:'Raum ist bereits voll'] as JSON)
            return
        }
        // 2) Passwort prüfen
        if (game.password && (json.code ?: '') != game.password) {
            json.status = 403
            render ([error:'Falsches Passwort'] as JSON)
            return
        }
        // 3) eigentlicher Join
        game.addToPlayers(user)
        game.status = (game.players.size()==2 ? 'ongoing' : game.status)
        game.save(flush:true)
        json.status = 200
        render ([gameId:  game.id, status:  game.status, players: game.players.collect{ [id:it.id, username:it.username] }] as JSON)
    }
    def leave() {
        def userId = getUserIdFromRequest()
        def user = User.get(userId)
        if (!user) {
            json.status = 403
            render ([error:'Nicht authentifiziert'] as JSON)
            return
        }
        def json = request.JSON
        boolean ok = gameService.leaveGame(json.gameId as Long, user)
        if (!ok) {
            json.status = 400
            render ([error:'Leave fehlgeschlagen'] as JSON)
        } else {
            json.status = 200
            render ([message:'Verlassen erfolgreich'] as JSON)
        }
    }

    // Spiel zurücksetzen
    def reset() {
        def json = request.JSON
        def game = Game.get(json.gameId as Long)
        if (!game) {
            json.status = 404
            render ([error: "Spiel nicht gefunden"] as JSON)
            return
        }
        gameService.resetGame(game)
        json.status = 200
        render ([message: "Spiel wurde zurückgesetzt"] as JSON)
    }

    // Übersicht offene Spiele (Lobby)
    def lobby() {
        def openGames = gameService.openGames()
        response.status = 200
        render openGames.collect { game ->
            [
                gameId:      game.id,
                name:        game.name ?: "Raum ${game.id}",
                playerCount: game.players.size(),
                status:      game.status
            ] as JSON
        }
    }
}
