package com.rankweis.chesslin.board

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class TileSpek : Spek({
    describe("a tile") {
        it("can get a horizontal range") {
            val start = Tile(0,0)
            val end = Tile(0,8)
            val range = start..end
            assertThat(range.size).isEqualTo(8)
            (1..8).map { Tile(0, it) }
                    .forEach { assertThat(range.contains(it)).isTrue() }
            assertThat(range.last()).isEqualTo(end)
        }

        it("can get a horizontal range backwards") {
            val start = Tile(0,8)
            val end = Tile(0,0)
            val range = start..end
            assertThat(range.size).isEqualTo(8)
            (0..7).map { Tile(0, it) }
                    .forEach { assertThat(range.contains(it)).isTrue() }
            assertThat(range.last()).isEqualTo(end)
        }

        it("can get a horizontal range backwards starting from a random number") {
            val start = Tile(6,4)
            val end = Tile(6,2)
            val range = start..end
            assertThat(range.size).isEqualTo(2)
            assertThat(range.contains(Tile(0,2)))
            assertThat(range.contains(Tile(0,3)))
            assertThat(range.contains(Tile(0,4)))
            assertThat(range.last()).isEqualTo(end)
        }

        it("can get a vertical range") {
            val start = Tile(0,0)
            val end = Tile(8,0)
            val range = start..end
            assertThat(range.size).isEqualTo(8)
            (1..8).map { Tile(it,0) }
                    .forEach { assertThat(range.contains(it)).isTrue() }
            assertThat(range.last()).isEqualTo(end)
        }

        it("can get a vertical range backwards") {
            val start = Tile(8,0)
            val end = Tile(0,0)
            val range = start..end
            assertThat(range.size).isEqualTo(8)
            (0..7).map { Tile(it,0) }
                    .forEach { assertThat(range.contains(it)).isTrue() }
            assertThat(range.last()).isEqualTo(end)
        }

        it("can get a diagonal range") {
            val start = Tile(0,0)
            val end = Tile(8,8)
            val range = start..end
            assertThat(range.size).isEqualTo(8)
            (1..8).map { Tile(it, it) }
                    .forEach { assertThat(range.contains(it)).isTrue() }
            assertThat(range.last()).isEqualTo(end)
        }

        it("can get a diagonal range backwards") {
            val start = Tile(8,8)
            val end = Tile(0,0)
            val range = start..end
            assertThat(range.size).isEqualTo(8)
            (0..7).map { Tile(it, it) }
                    .forEach { assertThat(range.contains(it)).isTrue() }
            assertThat(range.last()).isEqualTo(end)
        }

        it("can get a diagonal range left to right") {
            val start = Tile(0,8)
            val end = Tile(8,0)
            val range = start..end
            assertThat(range.contains(start)).isFalse()
            assertThat(range.size).isEqualTo(8)
            assertThat(range.contains(Tile(1,7)))
            assertThat(range.contains(Tile(2,6)))
            assertThat(range.contains(Tile(3,5)))
            assertThat(range.contains(Tile(4,4)))
            assertThat(range.last()).isEqualTo(end)
        }

        it("can get a diagonal range right to left") {
            val start = Tile(8,0)
            val end = Tile(0,8)
            val range = start..end
            assertThat(range.contains(start)).isFalse()
            assertThat(range.size).isEqualTo(8)
            assertThat(range.contains(Tile(1,7)))
            assertThat(range.contains(Tile(2,6)))
            assertThat(range.contains(Tile(3,5)))
            assertThat(range.contains(Tile(4,4)))
            assertThat(range.last()).isEqualTo(end)
        }



    }
})