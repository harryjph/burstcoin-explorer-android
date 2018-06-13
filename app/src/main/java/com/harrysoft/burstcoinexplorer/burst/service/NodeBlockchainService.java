package com.harrysoft.burstcoinexplorer.burst.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.harrysoft.burstcoinexplorer.burst.entity.Account;
import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstAddress;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstValue;
import com.harrysoft.burstcoinexplorer.burst.entity.SearchRequestType;
import com.harrysoft.burstcoinexplorer.burst.entity.SearchResult;
import com.harrysoft.burstcoinexplorer.burst.entity.Transaction;
import com.harrysoft.burstcoinexplorer.burst.service.entity.NullResponseException;
import com.harrysoft.burstcoinexplorer.burst.util.BurstUtils;
import com.harrysoft.burstcoinexplorer.main.repository.PreferenceRepository;

import java.math.BigInteger;
import java.util.List;

import io.reactivex.Single;

public class NodeBlockchainService implements BurstBlockchainService {

    private final PreferenceRepository preferenceRepository;
    private final RequestQueue requestQueue;
    private final Gson gson = new Gson();

    public NodeBlockchainService(PreferenceRepository preferenceRepository, Context context) {
        this.preferenceRepository = preferenceRepository;
        requestQueue = Volley.newRequestQueue(context);
    }

    private String getNodeAddress() {
        return preferenceRepository.getNodeAddress();
    }

    private <T> Single<T> fetchEntity(String url, Class<T> responseType) {
        return Single.create(e -> {
            StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
                if (response != null) {
                    T entity;

                    try {
                        entity = gson.fromJson(response, responseType);
                    } catch (Exception ex) {
                        e.onError(ex);
                        return;
                    }

                    e.onSuccess(entity);
                } else {
                    e.onError(new NullResponseException());
                }
            }, e::onError);

            requestQueue.add(request);
        });
    }

    @Override
    public Single<List<Block>> fetchRecentBlocks() {
        return fetchEntity(getNodeAddress() + "?requestType=getBlocks&firstIndex=0&lastIndex=100", RecentBlocksResponse.class)
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

    @Override
    public Single<Block> fetchBlockByHeight(final BigInteger blockHeight) {
        return blockResponseToBlock(fetchEntity(getNodeAddress() + "?requestType=getBlock&height=" + blockHeight.toString(), BlockResponse.class), true);
    }

    @Override
    public Single<Block> fetchBlockByID(final BigInteger blockID) {
        return blockResponseToBlock(fetchEntity(getNodeAddress() + "?requestType=getBlock&block=" + blockID.toString(), BlockResponse.class), true);
    }

    private Single<Account> fetchAccountWithRewardRecipient(AccountResponse rewardRecipient, BigInteger accountID) {
        return fetchEntity(getNodeAddress() + "?requestType=getAccount&account=" + accountID, AccountResponse.class)
                .map(account -> new Account(new BurstAddress(account.account), account.publicKey, account.name, account.description, BurstValue.fromNQT(account.balanceNQT), BurstValue.fromNQT(account.forgedBalanceNQT), new BurstAddress(rewardRecipient.account), rewardRecipient.name));
    }

    @Override
    public Single<Account> fetchAccount(final BigInteger accountID) {
        return fetchAccountRewardRecipient(accountID)
                .flatMap(rewardRecipientID -> fetchEntity(getNodeAddress() + "?requestType=getAccount&account=" + rewardRecipientID, AccountResponse.class))
                .flatMap(rewardRecipient -> fetchAccountWithRewardRecipient(rewardRecipient, accountID));
    }

    @Override
    public Single<BigInteger> fetchAccountRewardRecipient(final BigInteger accountID) {
        return fetchEntity(getNodeAddress() + "?requestType=getRewardRecipient&account=" + accountID, RewardRecipientResponse.class)
                .map(response -> response.rewardRecipient);
    }

    @Override
    public Single<List<BigInteger>> fetchAccountTransactions(final BigInteger accountID) {
        return fetchEntity(getNodeAddress() + "?requestType=getAccountTransactionIds&account=" + accountID.toString(), AccountTransactionsResponse.class)
                .map(response -> response.transactionIds);
    }

    @Override
    public Single<Transaction> fetchTransaction(final BigInteger transactionID) {
        return fetchEntity(getNodeAddress() + "?requestType=getTransaction&transaction=" + transactionID.toString(), TransactionResponse.class)
                .map(TransactionResponse::toTransaction);
    }

    @SuppressLint("CheckResult")
    @Override
    public Single<SearchResult> determineSearchRequestType(final String rawSearchRequest) {
        return Single.fromCallable(() -> {
            try {
                BurstUtils.toNumericID(rawSearchRequest);
                return new SearchResult(rawSearchRequest, SearchRequestType.ACCOUNT_RS);
            } catch (BurstUtils.ReedSolomon.DecodeException ignored) {}

            BigInteger searchRequest;
            try {
                searchRequest = new BigInteger(rawSearchRequest);
            } catch (NumberFormatException e) {
                return new SearchResult(rawSearchRequest, SearchRequestType.INVALID);
            }

            try {
                fetchBlockByHeight(searchRequest).blockingGet();
                return new SearchResult(rawSearchRequest, SearchRequestType.BLOCK_NUMBER);
            } catch (Exception ignored) {}

            try {
                fetchBlockByID(searchRequest).blockingGet();
                return new SearchResult(rawSearchRequest, SearchRequestType.BLOCK_ID);
            } catch (Exception ignored) {}

            try {
                fetchAccount(searchRequest).blockingGet();
                return new SearchResult(rawSearchRequest, SearchRequestType.ACCOUNT_ID);
            } catch (Exception ignored) {}

            try {
                fetchTransaction(searchRequest).blockingGet();
                return new SearchResult(rawSearchRequest, SearchRequestType.TRANSACTION_ID);
            } catch (Exception ignored) {}

            return new SearchResult(rawSearchRequest, SearchRequestType.NO_CONNECTION);
        });
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
