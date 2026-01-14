package dev.giuseppedarro.comanda.features.printers.data.repository

import dev.giuseppedarro.comanda.features.printers.data.Printers
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class PrintersRepositoryImpl : PrintersRepository {

    override suspend fun getAllPrinters(): List<Printer> = transaction {
        Printers
            .selectAll()
            .map { row ->
                Printer(
                    id = row[Printers.id],
                    name = row[Printers.name],
                    address = row[Printers.address],
                    port = row[Printers.port]
                )
            }
    }

    override suspend fun getPrinterById(id: Int): Printer? = transaction {
        Printers
            .select { Printers.id eq id }
            .singleOrNull()
            ?.let { row ->
                Printer(
                    id = row[Printers.id],
                    name = row[Printers.name],
                    address = row[Printers.address],
                    port = row[Printers.port]
                )
            }
    }

    override suspend fun createPrinter(name: String, address: String, port: Int): Printer = transaction {
        val id = Printers.insert {
            it[Printers.name] = name
            it[Printers.address] = address
            it[Printers.port] = port
        } get Printers.id

        Printer(
            id = id!!,
            name = name,
            address = address,
            port = port
        )
    }

    override suspend fun updatePrinter(id: Int, name: String, address: String, port: Int): Printer? = transaction {
        val exists = Printers.select { Printers.id eq id }.singleOrNull()
        if (exists == null) null
        else {
            Printers.update({ Printers.id eq id }) {
                it[Printers.name] = name
                it[Printers.address] = address
                it[Printers.port] = port
            }

            Printer(
                id = id,
                name = name,
                address = address,
                port = port
            )
        }
    }

    override suspend fun deletePrinter(id: Int): Boolean = transaction {
        Printers.deleteWhere { Printers.id eq id } > 0
    }
}