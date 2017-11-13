package com.rankweis.chesslin.pieces

import com.rankweis.chesslin.board.Chessboard
import com.rankweis.chesslin.board.Tile
import com.rankweis.chesslin.board.maxIndex

enum class Type {
    ROOK, KNIGHT, BISHOP, KING, QUEEN, PAWN
}

enum class Color {
    BLACK, WHITE
}

data class Piece(val type: Type, val color: Color, val tile: Tile) {

}

fun pieceOnTile(tile: Tile, board: Chessboard): Piece? {
    return board.pieces.find { it.tile == tile }
}

inline fun <T> Iterable<T>.takeWhileIncluding(predicate: (T) -> Boolean): List<T> {
    var list = emptyList<T>()
    for (item in this) {
        list = list.plus(item)
        if (!predicate(item)) break
    }
    return list
}

inline fun Piece.getMoves(board: Chessboard): List<Tile> {
    return when (this.type) {
        Type.ROOK -> legalHorizontalMoves(this, board)
                .plus(legalVerticalMoves(this, board))
                .distinct()
        Type.BISHOP -> legalDiagonalMoves(this, board)
        Type.QUEEN -> legalHorizontalMoves(this, board)
                .plus(legalVerticalMoves(this, board))
                .plus(legalDiagonalMoves(this, board))
                .distinct()
        Type.KNIGHT -> legalKnightMoves(this, board)
        else -> emptyList()
    }
}

// Only include the final tile if the piece on there is of opposite color
fun removeFinalIfColorSame(piece: Piece, board: Chessboard, moves: List<Tile>): List<Tile> {
    return if (moves.isNotEmpty() && pieceOnTile(moves.last(), board)?.color == piece.color) moves.dropLast(1) else moves
}

fun legalMoves(piece: Piece, board: Chessboard, vararg directions: List<Tile>): List<Tile> {
    val emptyTile: List<Tile> = emptyList()
    return directions.fold(
            emptyTile,
            { acc, list ->
                acc.plus(
                        removeFinalIfColorSame(piece, board, list.takeWhileIncluding { pieceOnTile(it, board) == null })
                )
            }
    ).distinct()
}

fun legalHorizontalMoves(piece: Piece, board: Chessboard): List<Tile> {
    val right = piece.tile..Tile(piece.tile.row, board.maxIndex())
    val left = piece.tile..Tile(piece.tile.row, 0)
    return legalMoves(piece, board, right, left)
}

fun legalVerticalMoves(piece: Piece, board: Chessboard): List<Tile> {
    val up = piece.tile..Tile(board.maxIndex(), piece.tile.column)
    val down = piece.tile..Tile(0, piece.tile.column)
    return legalMoves(piece, board, up, down)
}

fun legalDiagonalMoves(piece: Piece, board: Chessboard): List<Tile> {
    val distanceToRight = board.maxIndex() - piece.tile.column
    val distanceToLeft = piece.tile.column
    val distanceToTop = board.maxIndex() - piece.tile.row
    val distanceToBottom = piece.tile.row
    val uprightDistance = minOf(distanceToRight, distanceToTop)
    val upleftDistance = minOf(distanceToLeft, distanceToTop)
    val downRightDistance = minOf(distanceToRight, distanceToBottom)
    val downLeftDistance = minOf(distanceToLeft, distanceToBottom)

    val upright = piece.tile..Tile(piece.tile.row + uprightDistance, piece.tile.column + uprightDistance)
    val upleft = piece.tile..Tile(piece.tile.row + upleftDistance, piece.tile.column - upleftDistance)
    val downright = piece.tile..Tile(piece.tile.row - downRightDistance, piece.tile.column + downRightDistance)
    val downLeft = piece.tile..Tile(piece.tile.row - downLeftDistance, piece.tile.column - downLeftDistance)

    return legalMoves(piece, board, upright, upleft, downright, downLeft)
}

fun legalKnightMoves(piece: Piece, board: Chessboard): List<Tile> {
    fun Tile.plus(row: Int, column: Int): Tile {
        return Tile(this.row + row, this.column + column)
    }

    val tile = piece.tile
    return listOf(
            tile.plus(1, 2),
            tile.plus(1, -2),
            tile.plus(-1, 2),
            tile.plus(-1, -2),
            tile.plus(2, 1),
            tile.plus(2, -1),
            tile.plus(-2, 1),
            tile.plus(-2, -1))
            .filter { it.column in 0..board.maxIndex() && it.row in 0..board.maxIndex() }
            .filter { pieceOnTile(it, board)?.color != piece.color }
}
