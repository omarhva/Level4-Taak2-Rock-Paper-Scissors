package com.example.rockpaperscissors.Database

import android.content.Context
import com.example.rockpaperscissors.ResultActivitiy.Result

/**
 * @author Omar Mulla Ibrahim
 * Student Nr 500766035
 */

/**
 *  Repository for the Result
 */
class ResultRepository(context: Context) {

    private val resultDao: ResultDao

    init {
        val database = ResultRoomDatabase.getDatabase(context)
        resultDao = database!!.resultDao()
    }

    // here we get list of all the Results
    suspend fun getAllResults(): List<Result> = resultDao.getAllResults()

    // here we insert an result using the Dao interface
    suspend fun insertResult(result: Result) = resultDao.insertResult(result)

    // here we delete all results using the Dao interface
    suspend fun deleteAllResults() = resultDao.deleteAllResults()

    // here we get the number of the wins using the Dao interface
    suspend fun getNumberOfWins(string: String) = resultDao.getCountOf(string)
}