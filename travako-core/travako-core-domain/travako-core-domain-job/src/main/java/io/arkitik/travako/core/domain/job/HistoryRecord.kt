package io.arkitik.travako.core.domain.job

import kotlin.reflect.full.memberProperties

/**
 * Created By [**Ibrahim Al-Tamimi **](https://www.linkedin.com/in/iloom/)
 * Created At **Thursday **12**, May 2022**
 */
abstract class HistoryRecordDto() : HistoryRecord {
    final override val originalValues: Map<String, Any>

    init {
        originalValues = hashMapOf<String, Any>()
            .also { map ->
                this::class.memberProperties
                    .filterNot {
                        it.name == HistoryRecord::originalValues.name
                    }.filterNot {
                        it.name == HistoryRecord::updatedColumns.name
                    }.map {
                        this.readInstanceProperty<HistoryRecord, Any>(it.name)
                            ?.let { propValue -> map.put(it.name, propValue) }
                    }
            }
    }
}

interface HistoryRecord {
    val originalValues: Map<String, Any>

    val updatedColumns: Map<String, Any>
        get() = hashMapOf<String, Any>()
            .also { map ->
                this::class.memberProperties
                    .filterNot {
                        it.name == HistoryRecord::originalValues.name
                    }.filterNot {
                        it.name == HistoryRecord::updatedColumns.name
                    }.map {
                        this.readInstanceProperty<HistoryRecord, Any>(it.name)
                            ?.let { propValue -> map.put(it.name, propValue) }
                    }
            }
}

class Sample(
    var name: String,
) : HistoryRecordDto()

fun main(args: Array<String>) {
    val sample = Sample("Ibrahim")
    sample.name = "Sad"
    println(
        sample.originalValues
    )
    println(
        sample.updatedColumns
    )
}