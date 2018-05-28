package com.harrysoft.burstcoinexplorer.burst.utils;

import android.util.ArrayMap;

import com.harrysoft.burstcoinexplorer.R;

import java.util.Map;

public class TransactionTypeUtils {

    private static final byte TYPE_PAYMENT = 0;
    private static final byte TYPE_MESSAGING = 1;
    private static final byte TYPE_COLORED_COINS = 2;
    private static final byte TYPE_DIGITAL_GOODS = 3;
    private static final byte TYPE_ACCOUNT_CONTROL = 4;

    private static final byte TYPE_BURST_MINING = 20;
    private static final byte TYPE_ADVANCED_PAYMENT = 21;
    private static final byte TYPE_AUTOMATED_TRANSACTIONS = 22;

    private static final byte SUBTYPE_PAYMENT_ORDINARY_PAYMENT = 0;
    private static final byte SUBTYPE_PAYMENT_ORDINARY_PAYMENT_MULTI_OUT = 1;
    private static final byte SUBTYPE_PAYMENT_ORDINARY_PAYMENT_MULTI_SAME_OUT = 2;

    private static final byte SUBTYPE_MESSAGING_ARBITRARY_MESSAGE = 0;
    private static final byte SUBTYPE_MESSAGING_ALIAS_ASSIGNMENT = 1;
    private static final byte SUBTYPE_MESSAGING_ACCOUNT_INFO = 5;
    private static final byte SUBTYPE_MESSAGING_ALIAS_SELL = 6;
    private static final byte SUBTYPE_MESSAGING_ALIAS_BUY = 7;

    private static final byte SUBTYPE_COLORED_COINS_ASSET_ISSUANCE = 0;
    private static final byte SUBTYPE_COLORED_COINS_ASSET_TRANSFER = 1;
    private static final byte SUBTYPE_COLORED_COINS_ASK_ORDER_PLACEMENT = 2;
    private static final byte SUBTYPE_COLORED_COINS_BID_ORDER_PLACEMENT = 3;
    private static final byte SUBTYPE_COLORED_COINS_ASK_ORDER_CANCELLATION = 4;
    private static final byte SUBTYPE_COLORED_COINS_BID_ORDER_CANCELLATION = 5;

    private static final byte SUBTYPE_DIGITAL_GOODS_LISTING = 0;
    private static final byte SUBTYPE_DIGITAL_GOODS_DELISTING = 1;
    private static final byte SUBTYPE_DIGITAL_GOODS_PRICE_CHANGE = 2;
    private static final byte SUBTYPE_DIGITAL_GOODS_QUANTITY_CHANGE = 3;
    private static final byte SUBTYPE_DIGITAL_GOODS_PURCHASE = 4;
    private static final byte SUBTYPE_DIGITAL_GOODS_DELIVERY = 5;
    private static final byte SUBTYPE_DIGITAL_GOODS_FEEDBACK = 6;
    private static final byte SUBTYPE_DIGITAL_GOODS_REFUND = 7;

    private static final byte SUBTYPE_AT_CREATION = 0;
    private static final byte SUBTYPE_AT_NXT_PAYMENT = 1;

    private static final byte SUBTYPE_ACCOUNT_CONTROL_EFFECTIVE_BALANCE_LEASING = 0;

    private static final byte SUBTYPE_BURST_MINING_REWARD_RECIPIENT_ASSIGNMENT = 0;

    private static final byte SUBTYPE_ADVANCED_PAYMENT_ESCROW_CREATION = 0;
    private static final byte SUBTYPE_ADVANCED_PAYMENT_ESCROW_SIGN = 1;
    private static final byte SUBTYPE_ADVANCED_PAYMENT_ESCROW_RESULT = 2;
    private static final byte SUBTYPE_ADVANCED_PAYMENT_SUBSCRIPTION_SUBSCRIBE = 3;
    private static final byte SUBTYPE_ADVANCED_PAYMENT_SUBSCRIPTION_CANCEL = 4;
    private static final byte SUBTYPE_ADVANCED_PAYMENT_SUBSCRIPTION_PAYMENT = 5;

    public static Map<Byte, Integer> getTransactionTypes() {
        Map<Byte, Integer> transactionTypes = new ArrayMap<>();
        transactionTypes.put(TYPE_PAYMENT, R.string.transaction_type_payment);
        transactionTypes.put(TYPE_MESSAGING, R.string.transaction_type_messaging);
        transactionTypes.put(TYPE_COLORED_COINS, R.string.transaction_type_colored_coins);
        transactionTypes.put(TYPE_DIGITAL_GOODS, R.string.transaction_type_digital_goods);
        transactionTypes.put(TYPE_ACCOUNT_CONTROL, R.string.transaction_type_account_control);
        transactionTypes.put(TYPE_BURST_MINING, R.string.transaction_type_mining);
        transactionTypes.put(TYPE_ADVANCED_PAYMENT, R.string.transaction_type_mining);
        transactionTypes.put(TYPE_AUTOMATED_TRANSACTIONS, R.string.transaction_type_automated_transactions);
        return transactionTypes;
    }

    public static Map<Byte, Map<Byte, Integer>> getTransactionSubTypes() {
        Map<Byte, Map<Byte, Integer>> transactionTypes = new ArrayMap<>();
        Map<Byte, Integer> paymentSubTypes = new ArrayMap<>();
        Map<Byte, Integer> messagingSubTypes = new ArrayMap<>();
        Map<Byte, Integer> coloredCoinSubTypes = new ArrayMap<>();
        Map<Byte, Integer> digitalGoodsSubTypes = new ArrayMap<>();
        Map<Byte, Integer> accountControlSubTypes = new ArrayMap<>();
        Map<Byte, Integer> burstMiningSubTypes = new ArrayMap<>();
        Map<Byte, Integer> advancedPaymentSubTypes = new ArrayMap<>();
        Map<Byte, Integer> automatedTransactionSubTypes = new ArrayMap<>();

        paymentSubTypes.put(SUBTYPE_PAYMENT_ORDINARY_PAYMENT, R.string.transaction_type_payment_ordinary);
        paymentSubTypes.put(SUBTYPE_PAYMENT_ORDINARY_PAYMENT_MULTI_OUT, R.string.transaction_type_payment_ordinary_multi_out);
        paymentSubTypes.put(SUBTYPE_PAYMENT_ORDINARY_PAYMENT_MULTI_SAME_OUT, R.string.transaction_type_payment_ordinary_multi_same_out);

        messagingSubTypes.put(SUBTYPE_MESSAGING_ARBITRARY_MESSAGE, R.string.transaction_type_messaging_arbitrary_message);
        messagingSubTypes.put(SUBTYPE_MESSAGING_ALIAS_ASSIGNMENT, R.string.transaction_type_messaging_alias_assignment);
        messagingSubTypes.put(SUBTYPE_MESSAGING_ACCOUNT_INFO, R.string.transaction_type_messaging_account_info);
        messagingSubTypes.put(SUBTYPE_MESSAGING_ALIAS_SELL, R.string.transaction_type_messaging_alias_sell);
        messagingSubTypes.put(SUBTYPE_MESSAGING_ALIAS_BUY, R.string.transaction_type_messaging_alias_buy);

        coloredCoinSubTypes.put(SUBTYPE_COLORED_COINS_ASSET_ISSUANCE, R.string.transaction_type_colored_coins_asset_issuance);
        coloredCoinSubTypes.put(SUBTYPE_COLORED_COINS_ASSET_TRANSFER, R.string.transaction_type_colored_coins_asset_transfer);
        coloredCoinSubTypes.put(SUBTYPE_COLORED_COINS_ASK_ORDER_PLACEMENT, R.string.transaction_type_colored_coins_ask_order_placement);
        coloredCoinSubTypes.put(SUBTYPE_COLORED_COINS_BID_ORDER_PLACEMENT, R.string.transaction_type_colored_coins_bid_order_placement);
        coloredCoinSubTypes.put(SUBTYPE_COLORED_COINS_ASK_ORDER_CANCELLATION, R.string.transaction_type_colored_coins_ask_order_cancellation);
        coloredCoinSubTypes.put(SUBTYPE_COLORED_COINS_BID_ORDER_CANCELLATION, R.string.transaction_type_colored_coins_bid_order_cancellation);

        digitalGoodsSubTypes.put(SUBTYPE_DIGITAL_GOODS_LISTING, R.string.transaction_type_digital_goods_listing);
        digitalGoodsSubTypes.put(SUBTYPE_DIGITAL_GOODS_DELISTING, R.string.transaction_type_digital_goods_delisting);
        digitalGoodsSubTypes.put(SUBTYPE_DIGITAL_GOODS_PRICE_CHANGE, R.string.transaction_type_digital_goods_price_change);
        digitalGoodsSubTypes.put(SUBTYPE_DIGITAL_GOODS_QUANTITY_CHANGE, R.string.transaction_type_digital_goods_quantity_change);
        digitalGoodsSubTypes.put(SUBTYPE_DIGITAL_GOODS_PURCHASE, R.string.transaction_type_digital_goods_purchase);
        digitalGoodsSubTypes.put(SUBTYPE_DIGITAL_GOODS_DELIVERY, R.string.transaction_type_digital_goods_delivery);
        digitalGoodsSubTypes.put(SUBTYPE_DIGITAL_GOODS_FEEDBACK, R.string.transaction_type_digital_goods_feedback);
        digitalGoodsSubTypes.put(SUBTYPE_DIGITAL_GOODS_REFUND, R.string.transaction_type_digital_goods_refund);

        accountControlSubTypes.put(SUBTYPE_ACCOUNT_CONTROL_EFFECTIVE_BALANCE_LEASING, R.string.transaction_type_account_control_effective_balance_leasing);

        burstMiningSubTypes.put(SUBTYPE_BURST_MINING_REWARD_RECIPIENT_ASSIGNMENT, R.string.transaction_type_mining_reward_recipient_assignment);

        advancedPaymentSubTypes.put(SUBTYPE_ADVANCED_PAYMENT_ESCROW_CREATION, R.string.transaction_type_advanced_payment_escrow_creation);
        advancedPaymentSubTypes.put(SUBTYPE_ADVANCED_PAYMENT_ESCROW_SIGN, R.string.transaction_type_advanced_payment_escrow_sign);
        advancedPaymentSubTypes.put(SUBTYPE_ADVANCED_PAYMENT_ESCROW_RESULT, R.string.transaction_type_advanced_payment_escrow_result);
        advancedPaymentSubTypes.put(SUBTYPE_ADVANCED_PAYMENT_SUBSCRIPTION_SUBSCRIBE, R.string.transaction_type_advanced_payment_subscription_subscribe);
        advancedPaymentSubTypes.put(SUBTYPE_ADVANCED_PAYMENT_SUBSCRIPTION_CANCEL, R.string.transaction_type_advanced_payment_subscription_cancel);
        advancedPaymentSubTypes.put(SUBTYPE_ADVANCED_PAYMENT_SUBSCRIPTION_PAYMENT, R.string.transaction_type_advanced_payment_subscription_payment);

        automatedTransactionSubTypes.put(SUBTYPE_AT_CREATION, R.string.transaction_type_automated_transactions_create);
        automatedTransactionSubTypes.put(SUBTYPE_AT_NXT_PAYMENT, R.string.transaction_type_automated_transactions_payment);

        transactionTypes.put(TYPE_PAYMENT, paymentSubTypes);
        transactionTypes.put(TYPE_MESSAGING, messagingSubTypes);
        transactionTypes.put(TYPE_COLORED_COINS, coloredCoinSubTypes);
        transactionTypes.put(TYPE_DIGITAL_GOODS, digitalGoodsSubTypes);
        transactionTypes.put(TYPE_ACCOUNT_CONTROL, accountControlSubTypes);
        transactionTypes.put(TYPE_BURST_MINING, burstMiningSubTypes);
        transactionTypes.put(TYPE_ADVANCED_PAYMENT, advancedPaymentSubTypes);
        transactionTypes.put(TYPE_AUTOMATED_TRANSACTIONS, automatedTransactionSubTypes);

        return transactionTypes;
    }
}
