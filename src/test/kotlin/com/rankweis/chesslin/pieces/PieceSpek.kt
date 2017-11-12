package com.rankweis.chesslin.pieces

import com.rankweis.chesslin.board.Chessboard
import com.rankweis.chesslin.board.Tile
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class PieceSpek : Spek({
    describe("Piece helper functions") {
        it("Can find a piece on a tile") {
            val piece = Piece(Type.ROOK, Color.WHITE, Tile(0,0))
            assertThat(pieceOnTile(Tile(0,0),
                    Chessboard(8, listOf(piece)))).isEqualTo(piece)
        }

        it("can take while including last") {
            val taken = listOf(1,3,5,9,10,11,13,15).takeWhileIncluding { it % 2 == 1 }
            assertThat(taken).hasSize(5).contains(10).doesNotContain(11)
        }

        it("removes the last tile if the color is the same") {
            val whitePiece = Piece(Type.ROOK, Color.WHITE, Tile(0,0))
            val blackPiece = Piece(Type.ROOK, Color.BLACK, Tile(8,0))
            val board = Chessboard(8, listOf(whitePiece))
            val tiles = listOf(Tile(1,1), Tile(2,2), Tile(3,3))
            val noPieceOnTile = removeFinalIfColorSame(whitePiece, board, tiles)
            val whitePieceOnTile = removeFinalIfColorSame(whitePiece, board, tiles.plus(whitePiece.tile))
            val blackPieceOnTile = removeFinalIfColorSame(blackPiece, board, tiles.plus(whitePiece.tile))
            assertThat(noPieceOnTile).hasSize(tiles.size)
            assertThat(blackPieceOnTile).hasSize(tiles.size + 1)
            assertThat(whitePieceOnTile).hasSize(tiles.size)
        }
    }

    describe("horizontal movement") {
        it("Can find all the horizontal moves from the left") {
            val horizontal = legalHorizontalMoves(Piece(Type.ROOK,Color.WHITE,Tile(0,0)), Chessboard(8, emptyList()))
            assertThat(horizontal.size).isEqualTo(7)
            assertThat(horizontal).hasSize(7).contains(Tile(0,1), Tile(0,3))
            assertThat(horizontal.last()).isEqualTo(Tile(0,7))
        }
        it("Can find all the horizontal moves from the right") {
            val horizontal = legalHorizontalMoves(Piece(Type.ROOK,Color.WHITE,Tile(0,7)), Chessboard(8, emptyList()))
            assertThat(horizontal.size).isEqualTo(7)
            assertThat(horizontal).hasSize(7).contains(Tile(0,1), Tile(0,3))
            assertThat(horizontal.last()).isEqualTo(Tile(0,0))
        }
        it("Can find all the horizontal moves from the middle") {
            val horizontal = legalHorizontalMoves(Piece(Type.ROOK,Color.WHITE,Tile(0,4)), Chessboard(8, emptyList()))
            assertThat(horizontal.size).isEqualTo(7)
            assertThat(horizontal).hasSize(7).contains(Tile(0,1), Tile(0,3))
        }
    }
})