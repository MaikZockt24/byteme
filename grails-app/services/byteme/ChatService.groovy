package byteme

import grails.gorm.transactions.Transactional

@Transactional
class ChatService {
    Chat postMessage(Room room, User author, String text) {
        new Chat(room:room, author:author, message:text).save(flush:true)
    }

    List<Chat> listForRoom(Room room) {
        Chat.findAllByRoom(room, [sort:'dateCreated',order:'asc'])
    }
}

// MoveService.groovy
package byteme

import grails.gorm.transactions.Transactional

@Transactional
class MoveService {
    Move makeMove(Room room, User player, int r, int c, String sym) {
        // check empty cell, turn logic, win-check omitted for brevity
        new Move(room:room, player:player, row:r, col:c, symbol:sym).save(flush:true)
    }

    void resetMoves(Room room) {
        Move.where { room==room }.deleteAll()
    }
}