package com.rankweis.chesslin.actions

import com.rankweis.chesslin.board.Tile
import com.rankweis.chesslin.pieces.Piece

data class Move(val piece: Piece, val toTile: Tile)

