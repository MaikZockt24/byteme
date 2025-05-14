package byteme

import grails.converters.JSON

class ChatController {
    ChatService chatService

    def list(Long roomId) {
        Room r = Room.get(roomId)
        render chatService.listForRoom(r) as JSON
    }

    def post(Long roomId) {
        Room r = Room.get(roomId)
        Chat c = chatService.postMessage(r, session.user, params.text)
        render c as JSON
    }
}