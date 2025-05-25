package byteme

import grails.gorm.transactions.Transactional

@Transactional
class ChatService {
    GameService gameService
    ChatMessage postChatMessage(Long gameId, User user, String text) {
        def game = gameService.findById(gameId)
        if (!game) return null
        def chatMsg = new ChatMessage(game: game, user: user, text: text)
        chatMsg.save(flush: true)
        return chatMsg
    }
}