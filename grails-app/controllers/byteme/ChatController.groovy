package byteme

import grails.converters.JSON

class ChatController {
    ChatService chatService

    static allowedMethods = [list:"POST", post:"POST"]

    // POST /chat/list   { roomId }
    def list() {
        def j = request.JSON
        Room r = Room.get(j.roomId as Long)
        render(chatService.listForRoom(r)) as JSON
    }

    // POST /chat/post   { roomId, text }
    def post() {
        def j = request.JSON
        Room r = Room.get(j.roomId as Long)
        Chat c = chatService.postMessage(r, session.user, j.text)
        render(c) as JSON
    }
}
