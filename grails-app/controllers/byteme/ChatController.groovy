package byteme

import grails.rest.*
import grails.converters.JSON
import grails.gorm.transactions.Transactional

@Transactional
class ChatController {
    static responseFormats = ['json']
    ChatService chatService

    // Chat-Nachricht (chat)
    def chat() {
        def json = request.JSON
        def user = User.get(session.userId)
        if (!user) {
            render status: 403, json: [error: "Nicht authentifiziert"]
            return
        }
        def chatMsg = chatService.postChatMessage(json.gameId as Long, user, json.text)
        if (!chatMsg) {
            render status: 400, json: [error: "Nachricht konnte nicht gesendet werden"]
        } else {
            render status: 201, json: [gameId: chatMsg.game.id, userId: chatMsg.user.id, text: chatMsg.text]
        }
    }
}
