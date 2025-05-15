package byteme

import grails.converters.JSON

class RoomController {
    RoomService roomService
    MoveService moveService

    static allowedMethods = [create:"POST", join:"POST", reset:"POST"]

    // POST /room/create  { name, password }
    def create() {
        def j = request.JSON
        try {
            Room r = roomService.createRoom(j.password, j.name)
            render([success: true, roomId: r.id] as JSON)
        } catch(Exception e) {
            render(status:400, [success: false, error: e.message] as JSON)
        }
    }

    // POST /room/join   { id, password }
    def join() {
        def j = request.JSON
        try {
            Room r = roomService.joinRoom(j.id as Long, j.password, session.user)
            render([success: true, roomId: r.id] as JSON)
        } catch(IllegalArgumentException e) {
            render(status:404, [success: false, error: "Raum nicht gefunden"] as JSON)
        } catch(SecurityException e) {
            render(status:403, [success: false, error: "Falsches Passwort"] as JSON)
        }
    }

    // POST /room/reset  { id }
    def reset() {
        def j = request.JSON
        try {
            Room r = Room.get(j.id as Long)
            moveService.resetMoves(r)
            render([success: true] as JSON)
        } catch(Exception e) {
            render(status:500, [success: false, error: "Reset fehlgeschlagen"] as JSON)
        }
    }
}
