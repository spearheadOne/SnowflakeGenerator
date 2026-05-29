package org.abondar.experimental.generator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class GeneratorTest {

    @Test
    fun `Generate ID `() {
        val generator = SnowflakeGenerator(1)

        val id = generator.generateId()
        println(id)

        assertNotNull(id)
    }


    @Test
    fun `Generate unique ID`() {
        val generator = SnowflakeGenerator(1)

        val id = generator.generateId()
        val id1 = generator.generateId()

        assertNotEquals(id, id1)
    }

    @Test
    fun `Generate IDs in chronological order`() {
        val generator = SnowflakeGenerator(1)
        val ids = mutableListOf<Long>()

        repeat(10) {
            ids.add(generator.generateId())
            Thread.sleep(1)
        }

        assertEquals(ids.sorted(), ids)
    }

    @Test
    fun `Fail on invalid machine ID`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            SnowflakeGenerator(1024)
        }

        assertTrue(exception.message!!.contains("Machine ID must be between 0..MAX_MACHINE_ID"))
    }
}