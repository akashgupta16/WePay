package com.paypal.user_service.client;

import com.paypal.user_service.dto.CreateWalletRequest;
import com.paypal.user_service.dto.WalletResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//Feign client says spring that it is a http client, with the help of this
// we can communicate microservices by http request i.e. Wallet Client here
@FeignClient(name = "wallet-service", url="http://localhost:8088/api/v1/wallets")
public interface WalletClient {

    @PostMapping
    WalletResponse createWallet(@RequestBody CreateWalletRequest request);

}
