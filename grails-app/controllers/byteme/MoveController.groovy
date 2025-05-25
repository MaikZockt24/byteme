package byteme

import grails.rest.*
import grails.converters.JSON
import grails.gorm.transactions.Transactional

@Transactional
class MoveController {
    static responseFormats = ['json']
    GameService gameService

    // Spielzug (move)
    def move() {
       try {
            def json = request.JSON
            def user = User.get(session.userId)
            if (!user) {
                render status: 403, json: [error: "Nicht authentifiziert"]
                return
            }

            log.debug "Move-Request: gameId=${json.gameId}, row=${json.row}, column=${json.column}, user=${user.id}"

            def move = gameService.makeMove(
                 json.gameId as Long,
                 user,
                 json.row    as Integer,
                 json.column as Integer
            )
            log.debug "makeMove returned: $move"

            if (!move) {
                render status: 400, json: [error: "Ungültiger Zug"]
            } else {
                render status: 201, json: [
                    gameId:  move.game.id,
                    userId:  move.user.id,
                    row:     move.row,
                    column:  move.column
                ]
            }
        } catch (Exception e) {
            log.error("Fehler beim Zug anlegen", e)
            render status: 500, json: [error: e.message]
        }
    }
}
