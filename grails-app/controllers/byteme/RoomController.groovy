package byteme

class RoomController {
    RoomService roomService

    def lobby() {
        [rooms: Room.list()]
    }

    def create() {
        Room r = roomService.createRoom(params.password, params.name)
        redirect action:'play', params:[id:r.id]
    }

    def join() {
        roomService.joinRoom(params.id as Long, params.password, session.user)
        redirect action:'play', params:[id:params.id]
    }

    def play() {
        Room r = Room.get(params.id as Long)
        [room:r]
    }

    def reset() {
        Room r = Room.get(params.id as Long)
        moveService.resetMoves(r)
        render status:200
    }
}