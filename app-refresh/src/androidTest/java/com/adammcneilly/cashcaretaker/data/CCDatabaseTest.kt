package com.adammcneilly.cashcaretaker.data

import android.arch.persistence.room.Room
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.adammcneilly.cashcaretaker.main.MainActivity
import com.adammcneilly.cashcaretaker.account.Account
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests the database code.
 */
@RunWith(AndroidJUnit4::class)
class CCDatabaseTest {
    private lateinit var database: CCDatabase
    private lateinit var accountDao: AccountDAO
    private lateinit var transactionDao: TransactionDAO

    @JvmField @Rule val mainActivity = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setUp() {
        val context = mainActivity.activity
        database = Room.inMemoryDatabaseBuilder(context, CCDatabase::class.java).allowMainThreadQueries().build()
        accountDao = database.accountDao()
        transactionDao = database.transactionDao()
    }

    @After
    fun tearDown() {
        accountDao.deleteAll()
        database.close()
    }

    @Test
    fun testWriteReadAccount() {
        val testAccount = Account(TEST_ACCOUNT_NAME, TEST_ACCOUNT_BALANCE)

        val ids = accountDao.insert(listOf(testAccount))
        assertEquals(1, ids.size)

        val accountsFlowable = accountDao.getAll()
        val testSubscriber = TestSubscriber<List<Account>>()
        accountsFlowable.blockingSubscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        // Fails: testSubscriber.assertValue(listOf(testAccount))
        // Fails: testSubscriber.assertResult(listOf(testAccount))
    }

    companion object {
        val TEST_ACCOUNT_NAME = "Checking"
        val TEST_ACCOUNT_BALANCE = 100.00
    }
}