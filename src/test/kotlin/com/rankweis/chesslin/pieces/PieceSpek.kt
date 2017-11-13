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
            assertThat(horizontal).hasSize(7).contains(Tile(0,1), Tile(0,3))
            assertThat(horizontal.last()).isEqualTo(Tile(0,7))
        }
        it("Can find all the horizontal moves from the right") {
            val horizontal = legalHorizontalMoves(Piece(Type.ROOK,Color.WHITE,Tile(0,7)), Chessboard(8, emptyList()))
            assertThat(horizontal).hasSize(7).contains(Tile(0,1), Tile(0,3))
            assertThat(horizontal.last()).isEqualTo(Tile(0,0))
        }
        it("Can find all the horizontal moves from the middle") {
            val horizontal = legalHorizontalMoves(Piece(Type.ROOK,Color.WHITE,Tile(3,4)), Chessboard(8, emptyList()))
            assertThat(horizontal).hasSize(7).contains(Tile(3,1), Tile(3,3), Tile(3,7))
        }
        it( "Stops at an enemy piece") {
            val board = Chessboard(8,listOf(Piece(Type.ROOK, Color.BLACK, Tile(0,3))))
            val horizontal = legalHorizontalMoves(Piece(Type.ROOK,Color.WHITE,Tile(0,0)), board)
            assertThat(horizontal).hasSize(3).contains(Tile(0,1), Tile(0,2), Tile(0,3))
                    .doesNotContain(Tile(0,4))

        }

        it( "Stops before a friendly piece") {
            val board = Chessboard(8,listOf(Piece(Type.ROOK, Color.WHITE, Tile(0,3))))
            val horizontal = legalHorizontalMoves(Piece(Type.ROOK,Color.WHITE,Tile(0,0)), board)
            assertThat(horizontal).hasSize(2).contains(Tile(0,1), Tile(0,2))
                    .doesNotContain(Tile(0,4))

        }
    }

    describe("vertical movement") {
        it("Can find all the vertical moves from the bottom") {
            val horizontal = legalVerticalMoves(Piece(Type.ROOK,Color.WHITE,Tile(0,0)), Chessboard(8, emptyList()))
            assertThat(horizontal).hasSize(7).contains(Tile(3,0), Tile(5,0))
            assertThat(horizontal.last()).isEqualTo(Tile(7,0))
        }
        it("Can find all the horizontal moves from the right") {
            val horizontal = legalVerticalMoves(Piece(Type.ROOK,Color.WHITE,Tile(7,0)), Chessboard(8, emptyList()))
            assertThat(horizontal).hasSize(7).contains(Tile(1,0), Tile(3,0))
            assertThat(horizontal.last()).isEqualTo(Tile(0,0))
        }
        it("Can find all the horizontal moves from the middle") {
            val horizontal = legalVerticalMoves(Piece(Type.ROOK,Color.WHITE,Tile(3,4)), Chessboard(8, emptyList()))
            assertThat(horizontal).hasSize(7).contains(Tile(4,4), Tile(2,4), Tile(1,4))
        }
    }

    describe("diagonal movement") {
        it("Can find diagonal moves in one direction") {
            val diagonal = legalDiagonalMoves(Piece(Type.BISHOP, Color.WHITE, Tile(0,0)),
                    Chessboard(8, emptyList()))
            assertThat(diagonal).hasSize(7).containsAll(Tile(1,1)..Tile(7,7))
        }

        it("Can find moves in multiple diagonal directions") {
            val diagonal = legalDiagonalMoves(Piece(Type.BISHOP, Color.WHITE, Tile(1,1)),
                    Chessboard(8, emptyList()))
            assertThat(diagonal).hasSize(9).containsAll(Tile(2,2)..Tile(7,7))
                    .contains(Tile(0,0), Tile(0,2), Tile(2,0))
        }
    }

    describe("Knight movement") {
        it("finds all knight moves") {
            val knightMoves = legalKnightMoves(Piece(Type.KNIGHT, Color.WHITE, Tile(3,3)), Chessboard())
            assertThat(knightMoves).hasSize(8).contains(
                    Tile(1,2),
                    Tile(1,4),
                    Tile(2, 1),
                    Tile(2,5),
                    Tile(4,1),
                    Tile(4,5),
                    Tile(5,2),
                    Tile(5,4)
            )
        }

        it("finds only in bounds knight moves") {
            val knightMoves = legalKnightMoves(Piece(Type.KNIGHT, Color.WHITE, Tile(0,0)), Chessboard())
            assertThat(knightMoves).hasSize(2).contains(
                    Tile(2,1),
                    Tile(1,2)
            )
        }

        it("finds only legal knight moves") {
            val whitePiece = Piece(Type.ROOK, Color.WHITE, Tile(2,1))
            val blackPiece = Piece(Type.ROOK, Color.BLACK, Tile(1,2))
            val knightMoves = legalKnightMoves(Piece(Type.KNIGHT, Color.WHITE, Tile(0,0)),
                    Chessboard(pieces = listOf(blackPiece, whitePiece)))
            assertThat(knightMoves).hasSize(1).contains(
                    Tile(1,2)
            )
        }

    }
})