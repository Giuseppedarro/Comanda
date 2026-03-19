package dev.giuseppedarro.comanda.features.tables.data.repository

import dev.giuseppedarro.comanda.features.tables.data.remote.TableApi
import dev.giuseppedarro.comanda.features.tables.domain.model.TableException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TablesRepositoryImplTest {

    private lateinit var repository: TablesRepositoryImpl
    private val tableApi: TableApi = mockk()

    @Before
    fun setUp() {
        repository = TablesRepositoryImpl(tableApi)
    }

    @Test
    fun `when addTable returns 201, then return success`() = runBlocking {
        val response = mockk<HttpResponse>()
        every { response.status } returns HttpStatusCode.Created
        coEvery { tableApi.addTable(any()) } returns response

        val result = repository.addTable(5)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `when addTable returns 409, then return TableAlreadyExists failure`() = runBlocking {
        val response = mockk<HttpResponse>()
        every { response.status } returns HttpStatusCode.Conflict
        coEvery { tableApi.addTable(any()) } returns response

        val result = repository.addTable(5)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TableException.TableAlreadyExists)
    }

    @Test
    fun `when addTable returns 400, then return InvalidTableNumber failure`() = runBlocking {
        val response = mockk<HttpResponse>()
        every { response.status } returns HttpStatusCode.BadRequest
        coEvery { tableApi.addTable(any()) } returns response

        val result = repository.addTable(-1)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TableException.InvalidTableNumber)
    }
}
