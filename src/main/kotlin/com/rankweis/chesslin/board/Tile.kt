package com.rankweis.chesslin.board

import java.lang.Math.abs

data class Tile(val row: Int, val column: Int) {
    operator fun rangeTo(to: Tile): List<Tile> {
        return when {
            this.column == to.column -> {
                //Updown
                val len = Math.abs(to.row - this.row)
                val dir = if (to.row > this.row) 1 else -1
                (0..len).fold(emptyList<Tile>()) { acc, i -> acc + to.copy(row = this.row + (dir * i)) }
            }
            this.row == to.row -> {
                // Rightleft
                val len = Math.abs(to.column - this.column)
                val dir = if (to.column > this.column) 1 else -1
                (0..len).fold(emptyList()) { acc, i -> acc + to.copy(column = this.column + (dir * i)) }
            }
            abs(this.row - to.row) == abs(this.column - to.column) -> {
                val len = abs(this.row - to.row)
                val heightDirection = len / (to.row - this.row)
                val widthDirection = len / (to.column - this.column)
                // Diagonal
                (0..len).fold(emptyList()) { acc, i -> acc + this.copy(row = heightDirection * i + this.row, column = widthDirection * i + this.column) }
            }
            else -> {
                emptyList()
            }
        }.filter { it != this }
    }
}