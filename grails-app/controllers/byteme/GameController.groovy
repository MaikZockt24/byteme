package byteme

import grails.rest.*
import grails.converters.JSON
import grails.gorm.transactions.Transactional

@Transactional
class GameController {
    static responseFormats = ['json']
    GameService gameService

    // Neues Spiel erstellen
    def create() {
        def json = request.JSON
        def user = User.get(session.userId)
        if (!user) {
            render status: 403, json: [error: "Nicht authentifiziert"]
        return
        }

        def game = gameService.createGame(user, json.password)
        if (!game) {
            render status: 400, json: [error: "Spiel konnte nicht erstellt werden"]
        } else {
            render status: 201, json: [gameId: game.id, status: game.status, players: game.players.collect { [id: it.id, username: it.username] }, createdAt: game.createdAt]
        }
    }
    // Spiel beitreten
    def join() {
        def user = User.get(session.userId)
        if (!user) {
            render status:403, json:[error:'Nicht authentifiziert']
            return
        }
        def json = request.JSON
        Game game = Game.get(json.gameId as Long)
        if (!game) {
            render status:404, json:[error:'Raum nicht gefunden']
            return
        }
        // 1) Prüfen ob Raum voll
        if (game.players.size() >= 2) {
            render status:400, json:[error:'Raum ist bereits voll']
            return
        }
        // 2) Passwort prüfen
        if (game.password && (json.password ?: '') != game.password) {
            render status:403, json:[error:'Falsches Passwort']
            return
        }
        // 3) eigentlicher Join
        game.addToPlayers(user)
        game.status = (game.players.size()==2 ? 'ongoing' : game.status)
        game.save(flush:true)
        render status:200, json:[
        gameId:  game.id,
        status:  game.status,
        players: game.players.collect{ [id:it.id, username:it.username] }
        ]
    }
    def leave() {
        def user = User.get(session.userId)
        if (!user) {
            render status:403, json:[error:'Nicht authentifiziert']
            return
        }
        def json = request.JSON
        boolean ok = gameService.leaveGame(json.gameId as Long, user)
        if (!ok) {
            render status:400, json:[error:'Leave fehlgeschlagen']
        } else {
            render status:200, json:[message:'Verlassen erfolgreich']
        }
    }
  
    // Spiel zurücksetzen
    def reset() {
        def json = request.JSON
        def game = Game.get(json.gameId as Long)
        if (!game) {
            render status: 404, json: [error: "Spiel nicht gefunden"]
            return
        }
        gameService.resetGame(game)
        render status: 200, json: [message: "Spiel wurde zurückgesetzt"]
    }

    // Übersicht offene Spiele (Lobby)
    def lobby() {
        def openGames = gameService.openGames()
        render status: 200, json: openGames.collect { game ->
            [gameId: game.id, status: game.status, playerCount: game.players.size()]
        }
    }
}
