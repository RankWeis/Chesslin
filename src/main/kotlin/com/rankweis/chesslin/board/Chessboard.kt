package com.rankweis.chesslin.board

import com.rankweis.chesslin.actions.Move
import com.rankweis.chesslin.pieces.Piece

data class Chessboard(val dimension: Int = 8, val pieces: List<Piece> = emptyList(), val history: List<Move> = emptyList())

fun Chessboard.maxIndex() : Int {
    return this.dimension - 1
}

// Will move a piece whether or not a piece can be moved
fun movePiece(move: Move, board: Chessboard): Chessboard {
    return if (board.pieces.contains(move.piece) && moveWithinBounds(move, board)) {
        board.copy(
                pieces = board.pieces - move.piece
                         + move.piece.copy(tile = move.toTile,
                                           history = move.piece.history + move),
                history = board.history + move
        )
    } else {
        board
    }
}



fun moveWithinBounds(move: Move, board: Chessboard) : Boolean {
    return move.toTile.column in 0..board.maxIndex() && move.toTile.row in 0..board.maxIndex()
}
