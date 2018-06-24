package com.harry1453.burst.explorer.service;

import com.harry1453.burst.BurstUtils;
import com.harry1453.burst.explorer.entity.Account;
import com.harry1453.burst.explorer.entity.Block;
import com.harry1453.burst.explorer.entity.BurstAddress;
import com.harry1453.burst.explorer.entity.BurstValue;
import com.harry1453.burst.explorer.entity.SearchRequestType;
import com.harry1453.burst.explorer.entity.SearchResult;
import com.harry1453.burst.explorer.entity.Transaction;
import com.harry1453.burst.explorer.repository.ConfigRepository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.List;

import io.reactivex.Single;

public final class NodeBlockchainService implements BurstBlockchainService {

    private final ConfigRepository configRepository;
    private final ObjectService objectService;

    NodeBlockchainService(ConfigRepository configRepository, ObjectService objectService) {
        this.configRepository = configRepository;
        this.objectService = objectService;
    }

    private String getNodeAddress() {
        return configRepository.getNodeAddress();
    }

    @NotNull
    @Override
    public Single<List<Block>> fetchRecentBlocks() {
        return objectService.fetchObject(getNodeAddress() + "?requestType=getBlocks&firstIndex=0&lastIndex=100", RecentBlocksResponse.class)
                .flattenAsObservable(list -> list.blocks)
                .flatMap(blockResponse -> blockResponseToBlock(Single.just(blockResponse), false).toObservable())
                .toList();
    }

    private Single<Block> blockResponseToBlock(Single<BlockResponse> blockResponseSingle, boolean fetchGenerator) {
        if (fetchGenerator) {
            return blockResponseSingle
                    .flatMap(blockResponse -> fetchAccount(blockResponse.generator)
                    .map(generator -> new Block(blockResponse.numberOfTransactions, blockResponse.timestamp, blockResponse.block, BurstValue.fromNQT(blockResponse.totalAmountNQT), blockResponse.payloadLength, BurstValue.fromNQT(blockResponse.totalFeeNQT), BurstValue.fromBurst(blockResponse.blockReward), blockResponse.transactions, blockResponse.height, blockResponse.generator, generator)));
        } else {
            return blockResponseSingle
                    .map(blockResponse -> new Block(blockResponse.numberOfTransactions, blockResponse.timestamp, blockResponse.block, BurstValue.fromNQT(blockResponse.totalAmountNQT), blockResponse.payloadLength, BurstValue.fromNQT(blockResponse.totalFeeNQT), BurstValue.fromBurst(blockResponse.blockReward), blockResponse.transactions, blockResponse.height, blockResponse.generator, null));
        }
    }

    @NotNull
    @Override
    public Single<Block> fetchBlockByHeight(final BigInteger blockHeight) {
        return blockResponseToBlock(objectService.fetchObject(getNodeAddress() + "?requestType=getBlock&height=" + blockHeight.toString(), BlockResponse.class), true);
    }

    @NotNull
    @Override
    public Single<Block> fetchBlockByID(final BigInteger blockID) {
        return blockResponseToBlock(objectService.fetchObject(getNodeAddress() + "?requestType=getBlock&block=" + blockID.toString(), BlockResponse.class), true);
    }

    private Single<Account> fetchAccountWithRewardRecipient(AccountResponse rewardRecipient, BigInteger accountID) {
        return objectService.fetchObject(getNodeAddress() + "?requestType=getAccount&account=" + accountID, AccountResponse.class)
                .map(account -> new Account(new BurstAddress(account.account), account.publicKey, account.name != null ? account.name : "", account.description != null? account.description : "", BurstValue.fromNQT(account.balanceNQT), BurstValue.fromNQT(account.forgedBalanceNQT), new BurstAddress(rewardRecipient.account), rewardRecipient.name == null ? "" : rewardRecipient.name));
    }

    @NotNull
    @Override
    public Single<Account> fetchAccount(@NotNull final BigInteger accountID) {
        return fetchAccountRewardRecipient(accountID)
                .flatMap(rewardRecipientID -> objectService.fetchObject(getNodeAddress() + "?requestType=getAccount&account=" + rewardRecipientID, AccountResponse.class))
                .flatMap(rewardRecipient -> fetchAccountWithRewardRecipient(rewardRecipient, accountID));
    }

    @NotNull
    @Override
    public Single<BigInteger> fetchAccountRewardRecipient(@NotNull final BigInteger accountID) {
        return objectService.fetchObject(getNodeAddress() + "?requestType=getRewardRecipient&account=" + accountID, RewardRecipientResponse.class)
                .map(response -> response.rewardRecipient);
    }

    @NotNull
    @Override
    public Single<List<BigInteger>> fetchAccountTransactions(final BigInteger accountID) {
        return objectService.fetchObject(getNodeAddress() + "?requestType=getAccountTransactionIds&account=" + accountID.toString(), AccountTransactionsResponse.class)
                .map(response -> response.transactionIds);
    }

    @NotNull
    @Override
    public Single<Transaction> fetchTransaction(final BigInteger transactionID) {
        return objectService.fetchObject(getNodeAddress() + "?requestType=getTransaction&transaction=" + transactionID.toString(), TransactionResponse.class)
                .map(TransactionResponse::toTransaction);
    }

    @NotNull
    @Override
    public Single<SearchResult> determineSearchRequestType(@NotNull final String rawSearchRequest) {
        try {
            BurstUtils.toNumericID(rawSearchRequest);
            return Single.just(new SearchResult(rawSearchRequest, SearchRequestType.ACCOUNT_RS));
        } catch (BurstUtils.ReedSolomon.DecodeException ignored) {}

        BigInteger searchRequest;
        try {
            searchRequest = new BigInteger(rawSearchRequest);
        } catch (NumberFormatException e) {
            return Single.just(new SearchResult(rawSearchRequest, SearchRequestType.INVALID));
        }

        return fetchBlockByHeight(searchRequest)
                        .map(result -> new SearchResult(rawSearchRequest, SearchRequestType.BLOCK_NUMBER))
                .onErrorResumeNext(fetchBlockByID(searchRequest)
                        .map(result -> new SearchResult(rawSearchRequest, SearchRequestType.BLOCK_ID)))
                .onErrorResumeNext(fetchAccount(searchRequest)
                        .map(result -> new SearchResult(rawSearchRequest, SearchRequestType.ACCOUNT_ID)))
                .onErrorResumeNext(fetchTransaction(searchRequest)
                        .map(result -> new SearchResult(rawSearchRequest, SearchRequestType.TRANSACTION_ID)))
                .onErrorReturn(t -> new SearchResult(rawSearchRequest, SearchRequestType.NO_CONNECTION));
    }

    private class AccountResponse {
        BigInteger account;
        @Nullable
        String name;
        @Nullable
        String description;
        String publicKey;
        String balanceNQT;
        String forgedBalanceNQT;
    }

    private class BlockResponse {
        BigInteger payloadLength;
        String totalAmountNQT;
        BigInteger generator;
        BigInteger numberOfTransactions;
        String totalFeeNQT;
        String blockReward;
        List<BigInteger> transactions;
        BigInteger block;
        BigInteger timestamp;
        BigInteger height;
    }

    private class TransactionResponse {
        String signature;
        String feeNQT;
        BigInteger type;
        BigInteger subtype;
        BigInteger confirmations;
        String fullHash;
        String signatureHash;
        BigInteger sender;
        BigInteger recipient;
        String amountNQT;
        BigInteger block;
        BigInteger transaction;
        BigInteger timestamp;

        Transaction toTransaction() {
            return new Transaction(BurstValue.fromNQT(amountNQT), block, fullHash, confirmations, BurstValue.fromNQT(feeNQT), type, signatureHash, signature, new BurstAddress(sender), new BurstAddress(recipient), timestamp, transaction, subtype);
        }
    }

    private class RewardRecipientResponse {
        BigInteger rewardRecipient;
    }

    private class RecentBlocksResponse {
        List<BlockResponse> blocks;
    }

    private class AccountTransactionsResponse {
        List<BigInteger> transactionIds;
    }
}
