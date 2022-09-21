package com.realityexpander.pixabayforvsco.data.csv

import java.io.InputStream

interface CSVParser<T> {
    suspend fun parse(csvStream: InputStream): List<T>
}