package com.harrysoft.burstcoinexplorer.burst;

import com.harrysoft.burstcoinexplorer.burst.entity.AccountWithRewardRecipient;
import com.harrysoft.burstcoinexplorer.burst.entity.BlockWithGenerator;

import burst.kit.entity.response.AccountResponse;
import burst.kit.entity.response.BlockResponse;
import burst.kit.service.BurstNodeService;
import io.reactivex.Single;

public class BurstServiceExtensions {
    public static Single<AccountWithRewardRecipient> fetchAccountWithRewardRecipient(BurstNodeService nodeService, Single<AccountResponse> accountResponseSingle) {
        return accountResponseSingle
                .flatMap(accountResponse -> nodeService.getRewardRecipient(accountResponse.getAccount())
                        .flatMap(rewardRecipientAddress -> nodeService.getAccount(rewardRecipientAddress.getRewardRecipient())
                                .map(rewardRecipient -> new AccountWithRewardRecipient(accountResponse, rewardRecipient.getAccount(), rewardRecipient.getName()))));
    }

    public static Single<BlockWithGenerator> fetchBlockWithGenerator(BurstNodeService nodeService, Single<BlockResponse> blockResponseSingle) {
        return blockResponseSingle
                .flatMap(blockResponse -> fetchAccountWithRewardRecipient(nodeService, nodeService.getAccount(blockResponse.getGenerator()))
                        .map(generator -> new BlockWithGenerator(blockResponse, generator)));
    }
}
