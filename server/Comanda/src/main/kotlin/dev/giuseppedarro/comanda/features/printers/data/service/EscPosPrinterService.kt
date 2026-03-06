package dev.giuseppedarro.comanda.features.printers.data.service

import dev.giuseppedarro.comanda.features.printers.domain.service.PrinterService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

class EscPosPrinterService : PrinterService {

    companion object {
        private const val TIMEOUT_MS = 2000
        private val ESC_INIT = byteArrayOf(0x1B, 0x40) // ESC @
        private val GS_CUT = byteArrayOf(0x1D, 0x56, 0x42, 0x00) // GS V 66 0 (Cut paper)
    }

    override suspend fun printTicket(ip: String, port: Int, content: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            var socket: Socket? = null
            var outputStream: OutputStream? = null

            try {
                socket = Socket()
                socket.connect(InetSocketAddress(ip, port), TIMEOUT_MS)
                outputStream = socket.getOutputStream()

                // 1. Initialize Printer
                outputStream.write(ESC_INIT)

                // 2. Send Content (encoded as ISO-8859-1 for compatibility)
                // Replace newlines with CR+LF for some printers, though LF usually works
                val formattedContent = content.replace("\n", "\r\n")
                outputStream.write(formattedContent.toByteArray(Charsets.ISO_8859_1))

                // 3. Feed lines to clear the cutter
                outputStream.write("\r\n\r\n\r\n".toByteArray(Charsets.ISO_8859_1))

                // 4. Cut Paper
                outputStream.write(GS_CUT)

                outputStream.flush()
                Result.success(Unit)
            } catch (e: Exception) {
                // Log the error but don't crash
                // The caller (UseCase/Repository) will handle the fallback
                Result.failure(e)
            } finally {
                try {
                    outputStream?.close()
                    socket?.close()
                } catch (e: Exception) {
                    // Ignore close errors
                }
            }
        }
    }
}
