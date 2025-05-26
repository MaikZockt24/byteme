package byteme

import grails.rest.*
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import byteme.JWTUtil 

@Transactional
class MoveController {
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

    // Spielzug
    def move() {
        try {
            def json = request.JSON
            def userId = getUserIdFromRequest()
            def user = User.get(userId)
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
                json.status = 400
                render ([error: "Ungültiger Zug"] as JSON)
            } else {
                json.status = 201
                render ([gameId:  move.game.id, userId:  move.user.id, row:     move.row, column:  move.column] as JSON)
            }
        } catch (Exception e) {
            log.error("Fehler beim Zug anlegen", e)
            response.status = 500
            render ([error: e.message] as JSON)
        }
    }
}
