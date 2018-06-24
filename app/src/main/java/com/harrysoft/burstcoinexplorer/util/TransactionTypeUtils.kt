package com.harrysoft.burstcoinexplorer.util

import android.util.ArrayMap
import com.harrysoft.burstcoinexplorer.R

object TransactionTypeUtils {

    private const val TYPE_PAYMENT: Byte = 0
    private const val TYPE_MESSAGING: Byte = 1
    private const val TYPE_COLORED_COINS: Byte = 2
    private const val TYPE_DIGITAL_GOODS: Byte = 3
    private const val TYPE_ACCOUNT_CONTROL: Byte = 4

    private const val TYPE_BURST_MINING: Byte = 20
    private const val TYPE_ADVANCED_PAYMENT: Byte = 21
    private const val TYPE_AUTOMATED_TRANSACTIONS: Byte = 22

    private const val SUBTYPE_PAYMENT_ORDINARY_PAYMENT: Byte = 0
    private const val SUBTYPE_PAYMENT_ORDINARY_PAYMENT_MULTI_OUT: Byte = 1
    private const val SUBTYPE_PAYMENT_ORDINARY_PAYMENT_MULTI_SAME_OUT: Byte = 2

    private const val SUBTYPE_MESSAGING_ARBITRARY_MESSAGE: Byte = 0
    private const val SUBTYPE_MESSAGING_ALIAS_ASSIGNMENT: Byte = 1
    private const val SUBTYPE_MESSAGING_ACCOUNT_INFO: Byte = 5
    private const val SUBTYPE_MESSAGING_ALIAS_SELL: Byte = 6
    private const val SUBTYPE_MESSAGING_ALIAS_BUY: Byte = 7

    private const val SUBTYPE_COLORED_COINS_ASSET_ISSUANCE: Byte = 0
    private const val SUBTYPE_COLORED_COINS_ASSET_TRANSFER: Byte = 1
    private const val SUBTYPE_COLORED_COINS_ASK_ORDER_PLACEMENT: Byte = 2
    private const val SUBTYPE_COLORED_COINS_BID_ORDER_PLACEMENT: Byte = 3
    private const val SUBTYPE_COLORED_COINS_ASK_ORDER_CANCELLATION: Byte = 4
    private const val SUBTYPE_COLORED_COINS_BID_ORDER_CANCELLATION: Byte = 5

    private const val SUBTYPE_DIGITAL_GOODS_LISTING: Byte = 0
    private const val SUBTYPE_DIGITAL_GOODS_DELISTING: Byte = 1
    private const val SUBTYPE_DIGITAL_GOODS_PRICE_CHANGE: Byte = 2
    private const val SUBTYPE_DIGITAL_GOODS_QUANTITY_CHANGE: Byte = 3
    private const val SUBTYPE_DIGITAL_GOODS_PURCHASE: Byte = 4
    private const val SUBTYPE_DIGITAL_GOODS_DELIVERY: Byte = 5
    private const val SUBTYPE_DIGITAL_GOODS_FEEDBACK: Byte = 6
    private const val SUBTYPE_DIGITAL_GOODS_REFUND: Byte = 7

    private const val SUBTYPE_AT_CREATION: Byte = 0
    private const val SUBTYPE_AT_NXT_PAYMENT: Byte = 1

    private const val SUBTYPE_ACCOUNT_CONTROL_EFFECTIVE_BALANCE_LEASING: Byte = 0

    private const val SUBTYPE_BURST_MINING_REWARD_RECIPIENT_ASSIGNMENT: Byte = 0

    private const val SUBTYPE_ADVANCED_PAYMENT_ESCROW_CREATION: Byte = 0
    private const val SUBTYPE_ADVANCED_PAYMENT_ESCROW_SIGN: Byte = 1
    private const val SUBTYPE_ADVANCED_PAYMENT_ESCROW_RESULT: Byte = 2
    private const val SUBTYPE_ADVANCED_PAYMENT_SUBSCRIPTION_SUBSCRIBE: Byte = 3
    private const val SUBTYPE_ADVANCED_PAYMENT_SUBSCRIPTION_CANCEL: Byte = 4
    private const val SUBTYPE_ADVANCED_PAYMENT_SUBSCRIPTION_PAYMENT: Byte = 5

    @JvmStatic
    val transactionTypes: Map<Byte, Int>
        get() {
            val transactionTypes = ArrayMap<Byte, Int>()
            transactionTypes[TYPE_PAYMENT] = R.string.transaction_type_payment
            transactionTypes[TYPE_MESSAGING] = R.string.transaction_type_messaging
            transactionTypes[TYPE_COLORED_COINS] = R.string.transaction_type_colored_coins
            transactionTypes[TYPE_DIGITAL_GOODS] = R.string.transaction_type_digital_goods
            transactionTypes[TYPE_ACCOUNT_CONTROL] = R.string.transaction_type_account_control
            transactionTypes[TYPE_BURST_MINING] = R.string.transaction_type_mining
            transactionTypes[TYPE_ADVANCED_PAYMENT] = R.string.transaction_type_mining
            transactionTypes[TYPE_AUTOMATED_TRANSACTIONS] = R.string.transaction_type_automated_transactions
            return transactionTypes
        }

    @JvmStatic
    val transactionSubTypes: Map<Byte, Map<Byte, Int>>
        get() {
            val transactionTypes = ArrayMap<Byte, Map<Byte, Int>>()
            val paymentSubTypes = ArrayMap<Byte, Int>()
            val messagingSubTypes = ArrayMap<Byte, Int>()
            val coloredCoinSubTypes = ArrayMap<Byte, Int>()
            val digitalGoodsSubTypes = ArrayMap<Byte, Int>()
            val accountControlSubTypes = ArrayMap<Byte, Int>()
            val burstMiningSubTypes = ArrayMap<Byte, Int>()
            val advancedPaymentSubTypes = ArrayMap<Byte, Int>()
            val automatedTransactionSubTypes = ArrayMap<Byte, Int>()

            paymentSubTypes[SUBTYPE_PAYMENT_ORDINARY_PAYMENT] = R.string.transaction_type_payment_ordinary
            paymentSubTypes[SUBTYPE_PAYMENT_ORDINARY_PAYMENT_MULTI_OUT] = R.string.transaction_type_payment_ordinary_multi_out
            paymentSubTypes[SUBTYPE_PAYMENT_ORDINARY_PAYMENT_MULTI_SAME_OUT] = R.string.transaction_type_payment_ordinary_multi_same_out

            messagingSubTypes[SUBTYPE_MESSAGING_ARBITRARY_MESSAGE] = R.string.transaction_type_messaging_arbitrary_message
            messagingSubTypes[SUBTYPE_MESSAGING_ALIAS_ASSIGNMENT] = R.string.transaction_type_messaging_alias_assignment
            messagingSubTypes[SUBTYPE_MESSAGING_ACCOUNT_INFO] = R.string.transaction_type_messaging_account_info
            messagingSubTypes[SUBTYPE_MESSAGING_ALIAS_SELL] = R.string.transaction_type_messaging_alias_sell
            messagingSubTypes[SUBTYPE_MESSAGING_ALIAS_BUY] = R.string.transaction_type_messaging_alias_buy

            coloredCoinSubTypes[SUBTYPE_COLORED_COINS_ASSET_ISSUANCE] = R.string.transaction_type_colored_coins_asset_issuance
            coloredCoinSubTypes[SUBTYPE_COLORED_COINS_ASSET_TRANSFER] = R.string.transaction_type_colored_coins_asset_transfer
            coloredCoinSubTypes[SUBTYPE_COLORED_COINS_ASK_ORDER_PLACEMENT] = R.string.transaction_type_colored_coins_ask_order_placement
            coloredCoinSubTypes[SUBTYPE_COLORED_COINS_BID_ORDER_PLACEMENT] = R.string.transaction_type_colored_coins_bid_order_placement
            coloredCoinSubTypes[SUBTYPE_COLORED_COINS_ASK_ORDER_CANCELLATION] = R.string.transaction_type_colored_coins_ask_order_cancellation
            coloredCoinSubTypes[SUBTYPE_COLORED_COINS_BID_ORDER_CANCELLATION] = R.string.transaction_type_colored_coins_bid_order_cancellation

            digitalGoodsSubTypes[SUBTYPE_DIGITAL_GOODS_LISTING] = R.string.transaction_type_digital_goods_listing
            digitalGoodsSubTypes[SUBTYPE_DIGITAL_GOODS_DELISTING] = R.string.transaction_type_digital_goods_delisting
            digitalGoodsSubTypes[SUBTYPE_DIGITAL_GOODS_PRICE_CHANGE] = R.string.transaction_type_digital_goods_price_change
            digitalGoodsSubTypes[SUBTYPE_DIGITAL_GOODS_QUANTITY_CHANGE] = R.string.transaction_type_digital_goods_quantity_change
            digitalGoodsSubTypes[SUBTYPE_DIGITAL_GOODS_PURCHASE] = R.string.transaction_type_digital_goods_purchase
            digitalGoodsSubTypes[SUBTYPE_DIGITAL_GOODS_DELIVERY] = R.string.transaction_type_digital_goods_delivery
            digitalGoodsSubTypes[SUBTYPE_DIGITAL_GOODS_FEEDBACK] = R.string.transaction_type_digital_goods_feedback
            digitalGoodsSubTypes[SUBTYPE_DIGITAL_GOODS_REFUND] = R.string.transaction_type_digital_goods_refund

            accountControlSubTypes[SUBTYPE_ACCOUNT_CONTROL_EFFECTIVE_BALANCE_LEASING] = R.string.transaction_type_account_control_effective_balance_leasing

            burstMiningSubTypes[SUBTYPE_BURST_MINING_REWARD_RECIPIENT_ASSIGNMENT] = R.string.transaction_type_mining_reward_recipient_assignment

            advancedPaymentSubTypes[SUBTYPE_ADVANCED_PAYMENT_ESCROW_CREATION] = R.string.transaction_type_advanced_payment_escrow_creation
            advancedPaymentSubTypes[SUBTYPE_ADVANCED_PAYMENT_ESCROW_SIGN] = R.string.transaction_type_advanced_payment_escrow_sign
            advancedPaymentSubTypes[SUBTYPE_ADVANCED_PAYMENT_ESCROW_RESULT] = R.string.transaction_type_advanced_payment_escrow_result
            advancedPaymentSubTypes[SUBTYPE_ADVANCED_PAYMENT_SUBSCRIPTION_SUBSCRIBE] = R.string.transaction_type_advanced_payment_subscription_subscribe
            advancedPaymentSubTypes[SUBTYPE_ADVANCED_PAYMENT_SUBSCRIPTION_CANCEL] = R.string.transaction_type_advanced_payment_subscription_cancel
            advancedPaymentSubTypes[SUBTYPE_ADVANCED_PAYMENT_SUBSCRIPTION_PAYMENT] = R.string.transaction_type_advanced_payment_subscription_payment

            automatedTransactionSubTypes[SUBTYPE_AT_CREATION] = R.string.transaction_type_automated_transactions_create
            automatedTransactionSubTypes[SUBTYPE_AT_NXT_PAYMENT] = R.string.transaction_type_automated_transactions_payment

            transactionTypes[TYPE_PAYMENT] = paymentSubTypes
            transactionTypes[TYPE_MESSAGING] = messagingSubTypes
            transactionTypes[TYPE_COLORED_COINS] = coloredCoinSubTypes
            transactionTypes[TYPE_DIGITAL_GOODS] = digitalGoodsSubTypes
            transactionTypes[TYPE_ACCOUNT_CONTROL] = accountControlSubTypes
            transactionTypes[TYPE_BURST_MINING] = burstMiningSubTypes
            transactionTypes[TYPE_ADVANCED_PAYMENT] = advancedPaymentSubTypes
            transactionTypes[TYPE_AUTOMATED_TRANSACTIONS] = automatedTransactionSubTypes

            return transactionTypes
        }
}
