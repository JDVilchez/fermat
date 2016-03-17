package com.bitdubai.fermat_ccp_api.layer.basic_wallet.loss_protected_wallet.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantCalculateBalanceException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantRegisterCreditException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantRegisterDebitException;

/**
 * Created by Natalia Cortez 03/14/2016
 */
public interface BitcoinLossProtectedWalletBalance {

   /*
    * Get the balance of the wallet, the result represents the
    * amount of satoshis the user has.
   */

    long getBalance(BlockchainNetworkType blockchainNetworkType) throws CantCalculateBalanceException;

 /**
  * Throw the method <code>getTransactionById</code> return wallet available balance for actual exchange rate.
  * @param blockchainNetworkType
  * @param exchangeRate
  * @return
  * @throws CantCalculateBalanceException
  */
   long getBalance(BlockchainNetworkType blockchainNetworkType, long exchangeRate) throws CantCalculateBalanceException;

    void debit(BitcoinLossProtectedWalletTransactionRecord cryptoTransaction) throws CantRegisterDebitException;

    void credit(BitcoinLossProtectedWalletTransactionRecord cryptoTransaction) throws CantRegisterCreditException;

    void revertCredit(BitcoinLossProtectedWalletTransactionRecord cryptoTransaction) throws CantRegisterCreditException;


}