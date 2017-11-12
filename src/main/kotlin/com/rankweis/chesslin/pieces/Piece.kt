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

fun pieceOnTile(tile: Tile, board: Chessboard) : Piece? {
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

// Only include the final tile if the piece on there is of opposite color
fun removeFinalIfColorSame(piece: Piece, board: Chessboard, moves: List<Tile>) : List<Tile> {
    return if (moves.isNotEmpty() && pieceOnTile(moves.last(), board)?.color == piece.color) moves.dropLast(1) else moves
}

fun legalMoves(piece: Piece, board: Chessboard, direction1: List<Tile>, direction2: List<Tile>) : List<Tile> {
    val dir1UntilPiece = removeFinalIfColorSame(piece, board, direction1.takeWhileIncluding { pieceOnTile(it, board) == null })
    val dir2UntilPiece = removeFinalIfColorSame(piece, board, direction2.takeWhileIncluding { pieceOnTile(it, board) == null })
    return dir1UntilPiece.plus(dir2UntilPiece)
}

fun legalHorizontalMoves(piece: Piece, board: Chessboard) : List<Tile> {
    val right = piece.tile..Tile(piece.tile.row, board.maxIndex())
    val left = piece.tile..Tile(piece.tile.row, 0)
    return legalMoves(piece, board, right, left)
}

fun legalVerticalMoves(piece: Piece, board: Chessboard) : List<Tile> {
    val up = piece.tile..Tile(board.maxIndex(), piece.tile.column)
    val down = piece.tile..Tile(0, piece.tile.column)
    return legalMoves(piece, board, up, down)
}

