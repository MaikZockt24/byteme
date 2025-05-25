package byteme

import grails.gorm.transactions.Transactional

@Transactional
class GameService {
    Game findById(Long id) {
        Game.get(id)
    }

    Game createGame(User creator, String rawPassword) {
        def game = new Game()
        if (rawPassword) {
            game.password = rawPassword
        }
        game.addToPlayers(creator)
        game.save(flush: true)
        return game
    }

    Game joinGame(User user, Long gameId, String rawPassword) {
        def game = Game.get(gameId)
        if (!game) return null
        if (game.password) {
            if ((rawPassword ?: "") != game.password) return null
        }
        if (game.players.contains(user) || game.players.size() >= 2) return null
        game.addToPlayers(user)
        game.status = (game.players.size() == 2 ? "ongoing" : game.status)
        game.save(flush: true)
        return game
    }
    boolean leaveGame(Long gameId, User user) {
        Game game = Game.get(gameId)
        if (!game || !game.players.contains(user)) return false

        game.removeFromPlayers(user)
        game.status = (game.players.size() == 2 ? 'ongoing' : 'waiting')
        game.save(flush:true)
        return true
    }
    void resetGame(Game game) {
        Move.where { game == game }.deleteAll()
        game.status = "waiting"
        game.save(flush: true)
    }

    List<Game> openGames() {
        return Game.findAllByStatus("waiting")
    }

    Move makeMove(Long gameId, User user, Integer row, Integer column) {
        def game = Game.get(gameId)
        if (!game || game.status != "ongoing") return null
        if (Move.findByGameAndRowAndColumn(game, row, column)) return null
        def move = new Move(game: game, user: user, row: row, column: column)
        move.save(flush: true)
        return move
    }
}
