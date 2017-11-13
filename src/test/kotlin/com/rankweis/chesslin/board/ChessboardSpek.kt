package com.rankweis.chesslin.board

import com.rankweis.chesslin.actions.Move
import com.rankweis.chesslin.pieces.Color
import com.rankweis.chesslin.pieces.Piece
import com.rankweis.chesslin.pieces.Type
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ChessboardSpek : Spek({
    describe("a chessboard") {
        it("contains a board of given dimensions") {
            val board = Chessboard(dimension = 8, pieces = emptyList())
            assertThat(board.dimension).isEqualTo(8)
        }

        it("can move a piece") {
            val oldPiece = Piece(Type.PAWN, Color.WHITE, Tile(5,2))
            val board = Chessboard(dimension = 8, pieces = listOf(oldPiece))
            val piece = Piece(Type.PAWN, Color.WHITE, Tile(5, 2))
            val newTile = Tile(5, 4)
            val move = Move(piece, newTile)
            val board2 = movePiece(move, board)
            assertThat(board2.pieces.size).isEqualTo(1)
            assertThat(board2.pieces.find { it.tile == newTile }).isEqualTo(piece.copy(tile = newTile, history = listOf(move)))
        }

        it("handles absent piece movements gracefully") {
            val oldPiece = Piece(Type.PAWN, Color.WHITE, Tile(5,2))
            val board = Chessboard(dimension = 8, pieces = listOf(oldPiece))
            val piece = Piece(Type.PAWN, Color.WHITE, Tile(5, 2))
            val newTile = Tile(5, 4)
            val board2 = movePiece(Move(piece.copy(tile = newTile), newTile), board)
            assertThat(board2.pieces.size).isEqualTo(1)
            assertThat(board2.pieces.find { it.tile == newTile }).isEqualTo(null)
        }

        it("handles column out of bounds errors") {
            val oldPiece = Piece(Type.PAWN, Color.WHITE, Tile(5,2))
            val board = Chessboard(dimension = 8, pieces = listOf(oldPiece))
            val piece = Piece(Type.PAWN, Color.WHITE, Tile(5, 2))
            val newTile = Tile(5, 8)
            val board2 = movePiece(Move(piece, newTile), board)
            assertThat(board2.pieces.size).isEqualTo(1)
            assertThat(board2.pieces.find { it.tile == newTile }).isEqualTo(null)
            assertThat(board2.pieces.find { it.tile == oldPiece.tile }).isEqualTo(oldPiece)

        }

        it("handles row out of bounds errors") {
            val oldPiece = Piece(Type.PAWN, Color.WHITE, Tile(5,2))
            val board = Chessboard(dimension = 8, pieces = listOf(oldPiece))
            val piece = Piece(Type.PAWN, Color.WHITE, Tile(5, 2))
            val newTile = Tile(8, 4)
            val board2 = movePiece(Move(piece, newTile), board)
            assertThat(board2.pieces.size).isEqualTo(1)
            assertThat(board2.pieces.find { it.tile == newTile }).isEqualTo(null)
            assertThat(board2.pieces.find { it.tile == oldPiece.tile }).isEqualTo(oldPiece)
        }
    }
})