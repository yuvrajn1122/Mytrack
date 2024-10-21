package com.Wallet.Application.module.Refferel.refferalDto;



import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateReferralCodeRequest {

    @NotNull(message = "User ID is required")
    private Long userId;
    

}
