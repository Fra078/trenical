package it.trenical.customer.gui.data.grpc

import java.util.concurrent.Executor
import io.grpc.CallCredentials
import io.grpc.Metadata
import io.grpc.Status

class BearerToken(private val jwt: String) : CallCredentials() {
    override fun applyRequestMetadata(requestInfo: RequestInfo?, appExecutor: Executor, applier: MetadataApplier) {
        appExecutor.execute({
            try {
                val headers = Metadata()
                val authKey: Metadata.Key<String?>? = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)
                headers.put(authKey, "Bearer $jwt")
                applier.apply(headers)
            } catch (e: Throwable) {
                applier.fail(Status.UNAUTHENTICATED.withCause(e))
            }
        })
    }
}