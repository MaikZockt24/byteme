package byteme

import grails.rest.*
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import byteme.JWTUtil

@Transactional
class ChatController {
    static responseFormats = ['json']
    ChatService chatService

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

    // Chat-Nachricht
    def chat() {
        def json = request.JSON
        def userId = getUserIdFromRequest()
        def user = User.get(userId)
        if (!user) {
            json.status = 403
            render ([error: "Nicht authentifiziert"] as JSON)
            return
        }
        def chatMsg = chatService.postChatMessage(json.gameId as Long, user, json.text)
        if (!chatMsg) {
            json.status = 400
            render ([error: "Nachricht konnte nicht gesendet werden"] as JSON)
        } else {
            json.status = 201
            render ([gameId: chatMsg.game.id, userId: chatMsg.user.id, text: chatMsg.text] as JSON)
        }
    }
    def history() {
    def json = request.JSON
        Long gid = (json.gameId as Long)
        if (!gid) {
            response.status = 400
            render ([error: 'gameId erforderlich'] as JSON)
            return
        }
        def game = Game.get(gid)
        if (!game) {
            response.status = 404
            render ([error: 'Spiel nicht gefunden'] as JSON)
            return
        }
        def msgs = ChatMessage.findAllByGame(game, [sort:'createdAt', order:'asc'])
        render msgs.collect { 
            [id: it.id, user: it.user.username, text: it.text, time: it.createdAt.time] 
        } as JSON
    }
}
