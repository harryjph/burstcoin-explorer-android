package com.harrysoft.burstcoinexplorer.burst.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.harrysoft.burstcoinexplorer.burst.entity.Account;
import com.harrysoft.burstcoinexplorer.burst.entity.AccountTransactions;
import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstAddress;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstValue;
import com.harrysoft.burstcoinexplorer.burst.entity.SearchRequestType;
import com.harrysoft.burstcoinexplorer.burst.entity.SearchResult;
import com.harrysoft.burstcoinexplorer.burst.entity.Transaction;
import com.harrysoft.burstcoinexplorer.burst.service.entity.NullResponseException;
import com.harrysoft.burstcoinexplorer.burst.util.BurstUtils;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class PoCCBlockchainService implements BurstBlockchainService {
    private final String nodeAddress = "https://wallet.burst.cryptoguru.org:8125/burst"; // todo allow user to set

    private final RequestQueue requestQueue;
    private final Gson gson;

    public PoCCBlockchainService(Context context) {
        requestQueue = Volley.newRequestQueue(context);

        this.gson = new GsonBuilder()
                .registerTypeAdapter(AccountTransactions.class, new AccountTransactionsDeserializer())
                .create();
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
    public Single<Block[]> fetchRecentBlocks() {
        return Single.fromCallable(() -> {
            List<Block> blocks = new ArrayList<>();

            for (BlockResponse blockResponse : fetchEntity(nodeAddress + "?requestType=getBlocks&firstIndex=0&lastIndex=100", RecentBlocksResponse.class).blockingGet().blocks) {
                blocks.add(blockResponseToBlock(blockResponse, false).blockingGet());
            }

            return blocks.toArray(new Block[blocks.size()]);
        });
    }

    private Single<Block> blockResponseToBlock(BlockResponse blockResponse, boolean fetchGenerator) {
        return Single.fromCallable(() -> new Block(blockResponse.numberOfTransactions, blockResponse.timestamp, blockResponse.block, BurstValue.fromNQT(blockResponse.totalAmountNQT), blockResponse.payloadLength, BurstValue.fromNQT(blockResponse.totalFeeNQT), BurstValue.fromBurst(blockResponse.blockReward), blockResponse.transactions, blockResponse.height, blockResponse.generator, fetchGenerator ? fetchAccount(blockResponse.generator).blockingGet() : null));
    }

    @Override
    public Single<Block> fetchBlockByHeight(final BigInteger blockHeight) {
        return Single.fromCallable(() -> blockResponseToBlock(fetchEntity(nodeAddress + "?requestType=getBlock&height=" + blockHeight.toString(), BlockResponse.class).blockingGet(), true).blockingGet());
    }

    @Override
    public Single<Block> fetchBlockByID(final BigInteger blockID) {
        return Single.fromCallable(() -> blockResponseToBlock(fetchEntity(nodeAddress + "?requestType=getBlock&block=" + blockID.toString(), BlockResponse.class).blockingGet(), true).blockingGet());
    }

    @Override
    public Single<Account> fetchAccount(final BigInteger accountID) {
        return Single.fromCallable(() -> {
            AccountResponse account = fetchEntity(nodeAddress + "?requestType=getAccount&account=" + accountID, AccountResponse.class).blockingGet();

            BigInteger rewardRecipientID = fetchAccountRewardRecipient(accountID).blockingGet();
            String rewardRecipientName = fetchEntity(nodeAddress + "?requestType=getAccount&account=" + accountID, AccountResponse.class).blockingGet().name;
            return new Account(new BurstAddress(account.account), account.publicKey, account.name, account.description, BurstValue.fromNQT(account.balanceNQT), BurstValue.fromNQT(account.forgedBalanceNQT), new BurstAddress(rewardRecipientID), rewardRecipientName);
        });
    }

    @Override
    public Single<BigInteger> fetchAccountRewardRecipient(final BigInteger accountID) {
        return Single.fromCallable(() -> fetchEntity(nodeAddress + "?requestType=getRewardRecipient&account=" + accountID, RewardRecipientResponse.class).blockingGet().rewardRecipient);
    }

    @Override
    public Single<AccountTransactions> fetchAccountTransactions(final BigInteger accountID) {
        return Single.fromCallable(() -> fetchEntity(nodeAddress + "?requestType=getAccountTransactionIds&account=" + accountID.toString(), AccountTransactions.class).blockingGet());
    }

    @Override
    public Single<Transaction> fetchTransaction(final BigInteger transactionID) {
        return Single.fromCallable(() -> fetchEntity(nodeAddress + "?requestType=getTransaction&transaction=" + transactionID.toString(), TransactionResponse.class).blockingGet().toTransaction());
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

    private class AccountTransactionsDeserializer implements JsonDeserializer<AccountTransactions> {

        @Override
        @SuppressWarnings("ConstantConditions")
        public AccountTransactions deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObj = json.getAsJsonObject();

            JsonArray transactionsObj = jsonObj.getAsJsonArray("transactionIds");
            ArrayList<BigInteger> transactionIDs = new ArrayList<>();

            if (transactionsObj != null && !transactionsObj.isJsonNull()) {
                for (JsonElement transaction : transactionsObj) {
                    transactionIDs.add(transaction == null || transaction.isJsonNull() ? BigInteger.ZERO : transaction.getAsBigInteger());
                }
            }

            return new AccountTransactions(transactionIDs);
        }
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
        BlockResponse[] blocks;
    }
}
