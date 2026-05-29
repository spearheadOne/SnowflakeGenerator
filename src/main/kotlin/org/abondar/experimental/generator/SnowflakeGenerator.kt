package org.abondar.experimental.generator

import java.time.Instant
import java.util.concurrent.atomic.AtomicLong

class SnowflakeGenerator(
    private val machineId: Long,
) {

    companion object {

        //SNOFLAKE ID is 64 bit long
        private const val SIGN_BITS = 1
        private const val TIMESTAMP_BITS = 41
        private const val MACHINE_ID_BITS = 10
        private const val SEQUENCE_BITS = 12


        private const val MAX_MACHINE_ID = (1L shl MACHINE_ID_BITS) - 1
        private const val MAX_SEQUENCE = (1L shl SEQUENCE_BITS) - 1

        private const val TIMESTAMP_SHIFT =   MACHINE_ID_BITS + SEQUENCE_BITS
        private const val MACHINE_ID_SHIFT = SEQUENCE_BITS
        private const val EPOCH_START = "2024-01-01T00:00:00Z"
    }

    private val epoch: Long
    private val sequence: AtomicLong = AtomicLong(0)
    @Volatile private var lastTimestamp = -1L

    init {
        require(machineId in 0..MAX_MACHINE_ID) {
            "Machine ID must be between 0..MAX_MACHINE_ID"
        }

        epoch =  Instant.parse(EPOCH_START).toEpochMilli()
    }


    @Synchronized
    fun generateId(): Long {
        var currentTimeStamp = System.currentTimeMillis()

        if (currentTimeStamp < lastTimestamp) {
            throw IllegalStateException("Clock moved backwards. Generation is impossible")
        }

        val seq: Long
        if (currentTimeStamp == lastTimestamp) {
            seq = (sequence.incrementAndGet() and MAX_SEQUENCE)
            if (seq == 0L) {
                currentTimeStamp = waitNextMillis(currentTimeStamp) //wait for a new TS
                sequence.set(0)
            }
        } else {
            sequence.set(0)
            seq = 0L
        }

        lastTimestamp = currentTimeStamp


        return ((currentTimeStamp - epoch) shl TIMESTAMP_SHIFT) or
                (machineId shl MACHINE_ID_SHIFT) or seq
    }

    private fun waitNextMillis(currentTimeStamp: Long): Long {
        var timeStamp = currentTimeStamp
        while (timeStamp <= lastTimestamp){
            timeStamp = System.currentTimeMillis()
        }

        return timeStamp
    }


}