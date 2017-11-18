package com.rankweis.chesslin.pieces

import com.rankweis.chesslin.actions.Move
import com.rankweis.chesslin.board.Chessboard
import com.rankweis.chesslin.board.Tile
import com.rankweis.chesslin.board.maxIndex

enum class Type {
    ROOK, KNIGHT, BISHOP, KING, QUEEN, PAWN
}

enum class Color {
    BLACK, WHITE;

    fun opposite(): Color {
        return if (this == WHITE) BLACK else WHITE
    }
}

data class Piece(val type: Type, val color: Color, val tile: Tile, val history: List<Move> = emptyList()) {

}

fun pieceOnTile(tile: Tile, board: Chessboard): Piece? {
    return board.pieces.find { it.tile == tile }
}

inline fun <T> Iterable<T>.takeWhileIncluding(predicate: (T) -> Boolean): List<T> {
    var list = emptyList<T>()
    for (item in this) {
        list += item
        if (!predicate(item)) break
    }
    return list
}

inline fun Piece.getMoves(board: Chessboard): List<Tile> {
    return when (this.type) {
        Type.ROOK -> legalHorizontalMoves(this, board) +
                legalVerticalMoves(this, board)
        Type.BISHOP -> legalDiagonalMoves(this, board)
        Type.QUEEN -> legalHorizontalMoves(this, board) +
                legalVerticalMoves(this, board) +
                legalDiagonalMoves(this, board)
        Type.KNIGHT -> legalKnightMoves(this, board)
        else -> emptyList()
    }.distinct()
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
                acc + removeFinalIfColorSame(piece, board, list.takeWhileIncluding { pieceOnTile(it, board) == null })
            }
    ).distinct()
}

fun Tile.plus(row: Int, column: Int): Tile {
    return Tile(this.row + row, this.column + column)
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

fun inBounds(tile: Tile, board: Chessboard): Boolean {
    return tile.column in 0..board.maxIndex() && tile.row in 0..board.maxIndex()
}

fun legalKnightMoves(piece: Piece, board: Chessboard): List<Tile> {

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
            .filter { inBounds(it, board) }
            .filter { pieceOnTile(it, board)?.color != piece.color }
}

fun Piece.pawnInStartingRow(board: Chessboard): Boolean {
    return this.history.isEmpty() &&
            when (this.color) {
                Color.WHITE -> this.tile.row == 1
                Color.BLACK -> this.tile.row == board.maxIndex() - 1
            }
}

fun legalPawnMoves(piece: Piece, board: Chessboard): List<Tile> {
    val direction = if (piece.color == Color.WHITE) 1 else -1
    val forwardMovement = if (piece.pawnInStartingRow(board)) {
        piece.tile..piece.tile.copy(row = piece.tile.row + 2 * direction)
    } else {
        piece.tile..piece.tile.copy(row = piece.tile.row + direction)
    }.filter { pieceOnTile(it, board) == null }

    val potentialCaptures = listOf(piece.tile.plus(direction, 1), piece.tile.plus(direction, -1))
            .filter { inBounds(it, board) }
            .filter { pieceOnTile(it, board)?.color?.opposite() == piece.color }
    return forwardMovement + potentialCaptures
}
