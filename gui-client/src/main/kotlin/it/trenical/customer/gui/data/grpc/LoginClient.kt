package it.trenical.customer.gui.data.grpc

import io.grpc.ManagedChannel
import it.trenical.server.gateway.proto.LoginServiceGrpcKt
import it.trenical.user.proto.signinRequest
import it.trenical.user.proto.signupRequest

class LoginClient() {
    private val loginService = LoginServiceGrpcKt.LoginServiceCoroutineStub(
        GrpcChannelProvider.get()
    )

    suspend fun login(username: String, password: String) =
        loginService.login(signinRequest{
            this.username = username
            this.password = password
        })

    suspend fun register(username: String, password: String, firstName: String, lastName: String) =
        loginService.register(signupRequest {
            this.username = username
            this.password = password
            this.firstName = firstName
            this.lastName = lastName
        })

}