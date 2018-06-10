package com.harrysoft.burstcoinexplorer.burst.service;

import android.annotation.SuppressLint;
import android.content.Context;

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
import com.harrysoft.burstcoinexplorer.burst.entity.BlockExtra;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstAddress;
import com.harrysoft.burstcoinexplorer.burst.entity.BurstValue;
import com.harrysoft.burstcoinexplorer.burst.service.entity.EntityDoesNotExistException;
import com.harrysoft.burstcoinexplorer.burst.entity.SearchRequestType;
import com.harrysoft.burstcoinexplorer.burst.entity.SearchResult;
import com.harrysoft.burstcoinexplorer.burst.entity.Transaction;
import com.harrysoft.burstcoinexplorer.burst.service.entity.NullResponseException;
import com.harrysoft.burstcoinexplorer.burst.util.BurstUtils;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;

import io.reactivex.Single;

public class PoCCBlockchainService implements BurstBlockchainService {

    private final String API_URL = "https://explore.burst.cryptoguru.org/api/v1/";
    private final String RECENT_BLOCKS_URL = API_URL + "last_blocks/";
    private final String BLOCK_DETAILS_URL = API_URL + "block/";
    private final String ACCOUNT_DETAILS_URL = API_URL + "account/";
    private final String TRANSACTION_DETAILS_URL = API_URL + "transaction/";

    private final String nodeAddress = "https://wallet.burst.cryptoguru.org:8125/burst"; // todo allow user to set

    private final RequestQueue requestQueue;
    private final Gson gson;

    public PoCCBlockchainService(Context context) {
        requestQueue = Volley.newRequestQueue(context);

        this.gson = new GsonBuilder()
                .registerTypeAdapter(Block.class, new BlockDeserializer())
                .registerTypeAdapter(BlockExtra.class, new BlockExtraDeserializer())
                .registerTypeAdapter(Account.class, new AccountDeserializer())
                .registerTypeAdapter(AccountTransactions.class, new AccountTransactionsDeserializer())
                .registerTypeAdapter(Transaction.class, new TransactionDeserializer())
                .create();
    }

    @Override
    public Single<Block[]> fetchRecentBlocks() {
        return Single.create(e -> {
            StringRequest request = new StringRequest(Request.Method.GET, RECENT_BLOCKS_URL, response -> {
                if (response != null) {
                    Block[] blocks;

                    try {
                        blocks = gson.fromJson(response, RecentBlocksApiResponse.class).data.blocks;
                    } catch (Exception ex) {
                        e.onError(ex);
                        return;
                    }

                    if (blocks != null) {
                        e.onSuccess(blocks);
                    } else {
                        e.onError(new EntityDoesNotExistException());
                    }
                } else {
                    e.onError(new NullResponseException());
                }
            }, e::onError);

            requestQueue.add(request);
        });
    }

    @Override
    public Single<Block> fetchBlockByHeight(BigInteger blockHeight) {
        return Single.create(e -> {
            String url = BLOCK_DETAILS_URL + blockHeight.toString();
            StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
                if (response != null) {
                    Block block;

                    try {
                        block = gson.fromJson(response, BlockApiResponse.class).data;
                    } catch (Exception ex) {
                        e.onError(ex);
                        return;
                    }

                    if (block != null) {
                        e.onSuccess(block);
                    } else {
                        e.onError(new EntityDoesNotExistException());
                    }
                } else {
                    e.onError(new NullResponseException());
                }
            }, e::onError);

            requestQueue.add(request);
        });
    }

    @Override
    public Single<Block> fetchBlockByID(BigInteger blockID) {
        // For the PoCC API we can be lazy because the address format is the same,
        // but we are going to keep 2 separate functions in case we change API
        return fetchBlockByHeight(blockID);
    }

    @Override
    public Single<Account> fetchAccount(BigInteger accountID) {
        return Single.create(e -> {
            String url = ACCOUNT_DETAILS_URL + accountID.toString();
            StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
                if (response != null) {
                    Account account;

                    try {
                        account = gson.fromJson(response, AccountApiResponse.class).data;
                    } catch (Exception ex) {
                        e.onError(ex);
                        return;
                    }

                    if (account != null) {
                        e.onSuccess(account);
                    } else {
                        e.onError(new EntityDoesNotExistException());
                    }
                } else {
                    e.onError(new NullResponseException());
                }
            }, e::onError);

            requestQueue.add(request);
        });
    }

    @Override
    public Single<AccountTransactions> fetchAccountTransactions(BigInteger accountID) {
        return Single.create(e -> {
            String url = nodeAddress + "?requestType=getAccountTransactionIds&account=" + accountID.toString();
            StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
                if (response != null) {
                    AccountTransactions accountTransactions;

                    try {
                         accountTransactions = gson.fromJson(response, AccountTransactions.class).setAddress(new BurstAddress(accountID));
                    } catch (Exception ex) {
                        e.onError(ex);
                        return;
                    }

                    if (accountTransactions != null) {
                        e.onSuccess(accountTransactions);
                    } else {
                        e.onError(new EntityDoesNotExistException());
                    }
                } else {
                    e.onError(new NullResponseException());
                }
            }, e::onError);

            requestQueue.add(request);
        });
    }

    @Override
    public Single<Transaction> fetchTransaction(BigInteger transactionID) {
        return Single.create(e -> {
            String url = TRANSACTION_DETAILS_URL + transactionID.toString();
            StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
                if (response != null) {
                    Transaction transaction;

                    try {
                        transaction = gson.fromJson(response, TransactionApiResponse.class).data;
                    } catch (Exception ex) {
                        e.onError(ex);
                        return;
                    }

                    if (transaction != null) {
                        e.onSuccess(transaction);
                    } else {
                        e.onError(new EntityDoesNotExistException());
                    }
                } else {
                    e.onError(new NullResponseException());
                }
            }, e::onError);

            requestQueue.add(request);
        });
    }

    @Override
    public Single<BlockExtra> fetchBlockExtra(BigInteger blockID) {
        return Single.create(e -> {
            String url = nodeAddress + "?requestType=getBlock&block=" + blockID.toString();
            StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
                if (response != null) {
                    BlockExtra blockExtra;

                    try {
                        blockExtra = gson.fromJson(response, BlockExtra.class);
                    } catch (Exception ex) {
                        e.onError(ex);
                        return;
                    }

                    if (blockExtra != null) {
                        e.onSuccess(blockExtra);
                    } else {
                        e.onError(new EntityDoesNotExistException());
                    }
                } else {
                    e.onError(new NullResponseException());
                }
            }, e::onError);

            requestQueue.add(request);
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public Single<SearchResult> determineSearchRequestType(String rawSearchRequest) {
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


    private class BlockDeserializer implements JsonDeserializer<Block> {

        @Override
        @SuppressWarnings("ConstantConditions")
        public Block deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObj = json.getAsJsonObject();

            JsonElement element = jsonObj.get("transactions");
            BigInteger transactionCount = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            element = jsonObj.get("created");
            String timestamp = element == null || element.isJsonNull() ? "" : element.getAsString();

            element = jsonObj.get("block_id");
            BigInteger blockID = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            element = jsonObj.get("total");
            BurstValue total = BurstValue.fromNQT(element == null || element.isJsonNull() ? "" : element.getAsString());

            element = jsonObj.get("reward_recipient_id");
            BurstAddress rewardRecipient = new BurstAddress(element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger());

            element = jsonObj.get("size");
            BigInteger size = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            element = jsonObj.get("generator_name");
            String generatorName = element == null || element.isJsonNull() ? "" : element.getAsString();

            element = jsonObj.get("fee");
            BurstValue fee = BurstValue.fromNQT(element == null || element.isJsonNull() ? "" : element.getAsString());

            element = jsonObj.get("height");
            BigInteger height = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            element = jsonObj.get("generator_id");
            BurstAddress generator = new BurstAddress(element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger());

            element = jsonObj.get("reward_recipient_name");
            String rewardRecipientName = element == null || element.isJsonNull() ? "" : element.getAsString();

            return new Block(transactionCount,
                    timestamp,
                    blockID,
                    total,
                    rewardRecipient,
                    size,
                    generatorName,
                    fee,
                    height,
                    generator,
                    rewardRecipientName);
        }
    }

    private class AccountDeserializer implements JsonDeserializer<Account> {

        @Override
        @SuppressWarnings("ConstantConditions")
        public Account deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObj = json.getAsJsonObject();

            JsonElement element = jsonObj.get("public_key");
            String publicKey = element == null || element.isJsonNull() ? "" : element.getAsString();

            element = jsonObj.get("total_fees");
            BurstValue totalFees = BurstValue.fromNQT(element == null || element.isJsonNull() ? "" : element.getAsString());

            element = jsonObj.get("total_received");
            BurstValue totalReceived = BurstValue.fromNQT(element == null || element.isJsonNull() ? "" : element.getAsString());

            element = jsonObj.get("total_sent_n");
            BigInteger totalSentN = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            element = jsonObj.get("total_received_n");
            BigInteger totalReceivedN = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            element = jsonObj.get("balance");
            BurstValue balance = BurstValue.fromNQT(element == null || element.isJsonNull() ? "" : element.getAsString());

            element = jsonObj.get("name");
            String name = element == null || element.isJsonNull() ? "" : element.getAsString();

            element = jsonObj.get("pool_mined_blocks");
            BigInteger poolMinedBlocks = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            element = jsonObj.get("solo_mined_blocks");
            BigInteger soloMinedBlocks = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            element = jsonObj.get("pool_mined_balance");
            BurstValue poolMinedBalance = BurstValue.fromNQT(element == null || element.isJsonNull() ? "" : element.getAsString());

            element = jsonObj.get("solo_mined_balance");
            BurstValue soloMinedBalance = BurstValue.fromNQT(element == null || element.isJsonNull() ? "" : element.getAsString());

            element = jsonObj.get("total_sent");
            BurstValue totalSent = BurstValue.fromNQT(element == null || element.isJsonNull() ? "" : element.getAsString());

            element = jsonObj.get("id");
            BurstAddress address = new BurstAddress(element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger());

            element = jsonObj.get("reward_recip_id");
            BurstAddress rewardRecipient = new BurstAddress(element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger());

            element = jsonObj.get("reward_recip_name");
            String rewardRecipientName = element == null || element.isJsonNull() ? "" : element.getAsString();

            return new Account(address,
                    publicKey,
                    totalFees,
                    totalReceived,
                    totalSentN,
                    totalReceivedN,
                    balance,
                    name,
                    poolMinedBlocks,
                    soloMinedBlocks,
                    poolMinedBalance,
                    soloMinedBalance,
                    totalSent,
                    rewardRecipient,
                    rewardRecipientName);
        }
    }

    private class TransactionDeserializer implements JsonDeserializer<Transaction> {

        @Override
        @SuppressWarnings("ConstantConditions")
        public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObj = json.getAsJsonObject();

            JsonElement element = jsonObj.get("amount");
            BurstValue amount = BurstValue.fromNQT(element == null || element.isJsonNull() ? "" : element.getAsString());

            element = jsonObj.get("block_id");
            BigInteger blockID = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            element = jsonObj.get("full_hash");
            String fullHash = element == null || element.isJsonNull() ? "" : element.getAsString();

            element = jsonObj.get("confirmations");
            BigInteger confirmations = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            element = jsonObj.get("fee");
            BurstValue fee = BurstValue.fromNQT(element == null || element.isJsonNull() ? "" : element.getAsString());

            element = jsonObj.get("type");
            BigInteger type = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            element = jsonObj.get("signature_hash");
            String signatureHash = element == null || element.isJsonNull() ? "" : element.getAsString();

            element = jsonObj.get("signature");
            String signature = element == null || element.isJsonNull() ? "" : element.getAsString();

            element = jsonObj.get("sender");
            BurstAddress sender = new BurstAddress(element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger());

            element = jsonObj.get("recipient");
            BurstAddress recipient = new BurstAddress(element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger());

            element = jsonObj.get("created");
            String timestamp = element == null || element.isJsonNull() ? "" : element.getAsString();

            element = jsonObj.get("id");
            BigInteger transactionID = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            element = jsonObj.get("reward_recipient_name");
            BigInteger subType = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            return new Transaction(amount,
                    blockID,
                    fullHash,
                    confirmations,
                    fee,
                    type,
                    signatureHash,
                    signature,
                    sender,
                    recipient,
                    timestamp,
                    transactionID,
                    subType);
        }
    }

    private class BlockExtraDeserializer implements JsonDeserializer<BlockExtra> {

        @Override
        @SuppressWarnings("ConstantConditions")
        public BlockExtra deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObj = json.getAsJsonObject();

            JsonElement element = jsonObj.get("height");
            BigInteger height = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            element = jsonObj.get("block");
            BigInteger blockID = element == null || element.isJsonNull() ? BigInteger.ZERO : element.getAsBigInteger();

            element = jsonObj.get("blockReward");
            BurstValue blockReward = BurstValue.fromBurst(element == null || element.isJsonNull() ? "" : element.getAsString());

            JsonArray transactionsObj = jsonObj.getAsJsonArray("transactions");
            ArrayList<BigInteger> transactionIDs = new ArrayList<>();

            if (transactionsObj != null && !transactionsObj.isJsonNull()) {
                for (JsonElement transaction : transactionsObj) {
                    transactionIDs.add(transaction == null || transaction.isJsonNull() ? BigInteger.ZERO : transaction.getAsBigInteger());
                }
            }

            return new BlockExtra(height, blockID, blockReward, transactionIDs);
        }
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

    private class RecentBlocksApiResponse {
        BlocksArray data;

        private class BlocksArray {
            Block[] blocks;
        }
    }

    private class BlockApiResponse {
        Block data;
    }

    private class AccountApiResponse {
        Account data;
    }

    private class TransactionApiResponse {
        Transaction data;
    }
}
