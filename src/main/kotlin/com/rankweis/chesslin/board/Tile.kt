package com.rankweis.chesslin.board

import java.lang.Math.abs

data class Tile(val row: Int, val column: Int) {
    operator fun rangeTo(to: Tile) : List<Tile> {
        var range : List<Tile> = emptyList()
        if (this.column == to.column) {
            //Updown
            val len = Math.abs(to.row - this.row)
            val dir = if (to.row > this.row) 1 else -1
            for(i in 0..len) {
                range = range.plus(to.copy(row = this.row + (dir * i)))
            }
        } else if (this.row == to.row) {
            // Rightleft
            val len = Math.abs(to.column - this.column)
            val dir = if (to.column > this.column) 1 else -1
            for(i in 0..len) {
                range = range.plus(to.copy(column = this.column + (dir * i)))
            }
        } else if (abs(this.row - to.row) == abs(this.column - to.column)) {
            val len = abs(this.row - to.row)
            val heightDirection = len / (to.row - this.row)
            val widthDirection = len / (to.column - this.column)
            // Diagonal
            for(i in 0..len)
                range = range.plus(this.copy(row = heightDirection * i + this.row, column = widthDirection * i + this.column))
        }
        return range.filter { it != this }
    }
}