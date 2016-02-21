package com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_transfer.developer.bitdubai.version_1.structure.events;

import com.bitdubai.fermat_api.Agent;
import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.DealsWithPluginIdentity;
import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Specialist;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoStatus;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoTransaction;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.exceptions.CantConfirmTransactionException;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.exceptions.CantDeliverPendingTransactionsException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantExecuteQueryException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.BroadcastStatus;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantBroadcastTransactionException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantCancellBroadcastTransactionException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantGetBroadcastStatusException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantGetCryptoTransactionException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantGetTransactionCryptoStatusException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.interfaces.BitcoinNetworkManager;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.asset_vault.exceptions.CantSendAssetBitcoinsToUserException;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.asset_vault.interfaces.AssetVaultManager;
import com.bitdubai.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetMetadata;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.DAPMessageType;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.DAPTransactionType;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.DistributionStatus;
import com.bitdubai.fermat_dap_api.layer.all_definition.exceptions.CantSetObjectException;
import com.bitdubai.fermat_dap_api.layer.all_definition.network_service_message.DAPMessage;
import com.bitdubai.fermat_dap_api.layer.all_definition.network_service_message.content_message.AssetTransferContentMessage;
import com.bitdubai.fermat_dap_api.layer.all_definition.network_service_message.exceptions.CantGetDAPMessagesException;
import com.bitdubai.fermat_dap_api.layer.all_definition.network_service_message.exceptions.CantSendMessageException;
import com.bitdubai.fermat_dap_api.layer.all_definition.util.ActorUtils;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantGetAssetIssuerActorsException;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuer;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_user.exceptions.CantAssetUserActorNotFoundException;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_user.exceptions.CantCreateAssetUserActorException;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_user.exceptions.CantGetAssetUserActorsException;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_user.interfaces.ActorAssetUser;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_user.interfaces.ActorAssetUserManager;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.exceptions.CantGetIssuerNetworkServiceMessageListException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.interfaces.AssetIssuerActorNetworkServiceManager;
import com.bitdubai.fermat_dap_api.layer.dap_module.wallet_asset_issuer.exceptions.CantGetAssetStatisticException;
import com.bitdubai.fermat_dap_api.layer.dap_network_services.asset_transmission.enums.DigitalAssetMetadataTransactionType;
import com.bitdubai.fermat_dap_api.layer.dap_network_services.asset_transmission.exceptions.CantSendTransactionNewStatusNotificationException;
import com.bitdubai.fermat_dap_api.layer.dap_network_services.asset_transmission.interfaces.AssetTransmissionNetworkServiceManager;
import com.bitdubai.fermat_dap_api.layer.dap_network_services.asset_transmission.interfaces.DigitalAssetMetadataTransaction;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_issuing.exceptions.CantDeliverDigitalAssetToAssetWalletException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_issuing.interfaces.AssetIssuingTransactionNotificationAgent;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_transfer.exceptions.CantTransferDigitalAssetsException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.CantCreateDigitalAssetFileException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.CantExecuteDatabaseOperationException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.CantGetDigitalAssetFromLocalStorageException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.RecordsNotFoundException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.UnexpectedResultReturnedFromDatabaseException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.util.AssetVerification;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.exceptions.CantRegisterCreditException;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.exceptions.CantRegisterDebitException;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.exceptions.CantSaveStatisticException;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWalletManager;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.common.WalletUtilities;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.common.enums.BalanceType;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.common.enums.TransactionType;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.common.exceptions.CantGetTransactionsException;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.common.exceptions.CantLoadWalletException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_transfer.developer.bitdubai.version_1.AssetTransferDigitalAssetTransactionPluginRoot;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_transfer.developer.bitdubai.version_1.exceptions.CantCheckTransferProgressException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_transfer.developer.bitdubai.version_1.structure.database.AssetTransferDAO;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_transfer.developer.bitdubai.version_1.structure.functional.DeliverRecord;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_transfer.developer.bitdubai.version_1.structure.functional.DigitalAssetTransferVault;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_transfer.developer.bitdubai.version_1.structure.functional.DigitalAssetTransferer;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Víctor Mars (marsvicam@gmail.com) on 08/01/16.
 */
public class AssetTransferMonitorAgent implements Agent, DealsWithLogger, DealsWithErrors, DealsWithPluginDatabaseSystem, DealsWithPluginIdentity {

    private Thread agentThread;
    private LogManager logManager;
    private ErrorManager errorManager;
    private PluginDatabaseSystem pluginDatabaseSystem;
    private UUID pluginId;
    private AssetVaultManager assetVaultManager;
    private DigitalAssetTransferVault digitalAssetTransferVault;
    private AssetTransmissionNetworkServiceManager assetTransmissionManager;
    private BitcoinNetworkManager bitcoinNetworkManager;
    private DigitalAssetTransferer distributor;
    private AssetIssuerActorNetworkServiceManager assetIssuerActorNetworkServiceManager;
    private ActorAssetUserManager actorAssetUserManager;
    private AssetIssuerWalletManager assetIssuerWalletManager;

    public AssetTransferMonitorAgent(PluginDatabaseSystem pluginDatabaseSystem,
                                     ErrorManager errorManager,
                                     UUID pluginId,
                                     PluginFileSystem pluginFileSystem,
                                     AssetVaultManager assetVaultManager,
                                     BitcoinNetworkManager bitcoinNetworkManager,
                                     LogManager logManager,
                                     DigitalAssetTransferVault digitalAssetTransferVault,
                                     AssetTransmissionNetworkServiceManager assetTransmissionManager,
                                     AssetIssuerActorNetworkServiceManager assetIssuerActorNetworkServiceManager,
                                     ActorAssetUserManager actorAssetUserManager,
                                     AssetIssuerWalletManager assetIssuerWalletManager) throws CantSetObjectException {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.errorManager = errorManager;
        this.pluginId = pluginId;
        this.logManager = logManager;
        this.digitalAssetTransferVault = digitalAssetTransferVault;
        this.assetTransmissionManager = assetTransmissionManager;
        this.bitcoinNetworkManager = bitcoinNetworkManager;
        this.assetIssuerActorNetworkServiceManager = assetIssuerActorNetworkServiceManager;
        this.actorAssetUserManager = actorAssetUserManager;
        this.assetIssuerWalletManager = assetIssuerWalletManager;
        this.distributor = new DigitalAssetTransferer(
                assetVaultManager,
                errorManager,
                pluginId,
                pluginFileSystem,
                bitcoinNetworkManager);
        setAssetVaultManager(assetVaultManager);
    }

    private void setAssetVaultManager(AssetVaultManager assetVaultManager) throws CantSetObjectException {
        if (assetVaultManager == null) {
            throw new CantSetObjectException("AssetVaultManager is null");
        }
        this.assetVaultManager = assetVaultManager;
    }

    public void setDigitalAssetTransferVault(DigitalAssetTransferVault digitalAssetTransferVault) throws CantSetObjectException {
        if (digitalAssetTransferVault == null) {
            throw new CantSetObjectException("DigitalAssetDistributionVault is null");
        }
        this.digitalAssetTransferVault = digitalAssetTransferVault;
    }

    public void setAssetTransmissionManager(AssetTransmissionNetworkServiceManager assetTransmissionManager) throws CantSetObjectException {
        if (assetTransmissionManager == null) {
            throw new CantSetObjectException("assetTransmissionManager is null");
        }
        this.assetTransmissionManager = assetTransmissionManager;
    }

    @Override
    public void start() throws CantStartAgentException {

        try {

            MonitorAgent monitorAgent = new MonitorAgent();

            monitorAgent.setPluginDatabaseSystem(this.pluginDatabaseSystem);
            monitorAgent.setErrorManager(this.errorManager);
            monitorAgent.setAssetTransferDAO(new AssetTransferDAO(pluginDatabaseSystem, pluginId, digitalAssetTransferVault));
            this.agentThread = new Thread(monitorAgent, "Asset Transfer MonitorAgent");
            this.agentThread.start();
        } catch (Exception exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_ISSUING_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, exception);
        }


    }

    @Override
    public void stop() {
        this.agentThread.interrupt();
    }

    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    @Override
    public void setPluginId(UUID pluginId) {
        this.pluginId = pluginId;
    }

    /**
     * Private class which implements runnable and is started by the Agent
     * Based on MonitorAgent created by Rodrigo Acosta
     */
    private class MonitorAgent implements AssetIssuingTransactionNotificationAgent, DealsWithPluginDatabaseSystem, DealsWithErrors, Runnable {

        public final int SLEEP_TIME = AssetIssuingTransactionNotificationAgent.AGENT_SLEEP_TIME;
        ErrorManager errorManager;
        PluginDatabaseSystem pluginDatabaseSystem;
        int iterationNumber = 0;
        AssetTransferDAO assetTransferDAO;

        @Override
        public void setErrorManager(ErrorManager errorManager) {
            this.errorManager = errorManager;
        }

        @Override
        public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
            this.pluginDatabaseSystem = pluginDatabaseSystem;
        }

        @Override
        public void run() {

            logManager.log(AssetTransferDigitalAssetTransactionPluginRoot.getLogLevelByClass(this.getClass().getName()), "Asset Distribution Protocol Notification Agent: running...", null, null);
            while (true) {
                /**
                 * Increase the iteration counter
                 */
                iterationNumber++;
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException interruptedException) {
                    return;
                }

                /**
                 * now I will check if there are pending transactions to raise the event
                 */
                try {

                    logManager.log(AssetTransferDigitalAssetTransactionPluginRoot.getLogLevelByClass(this.getClass().getName()), "Iteration number " + iterationNumber, null, null);
                    doTheMainTask();
                } catch (CantCheckTransferProgressException | CantExecuteQueryException exception) {
                    errorManager.reportUnexpectedPluginException(Plugins.ASSET_TRANSFER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
                }

            }

        }

        private void doTheMainTask() throws CantExecuteQueryException, CantCheckTransferProgressException {
            try {
                checkPendingNetworkEvents();
                checkPendingTransactions();
                checkDeliveringTime();
                checkTransferMessages();

            } catch (CantExecuteDatabaseOperationException exception) {
                throw new CantExecuteQueryException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, exception, "Exception in asset distribution monitor agent", "Cannot execute database operation");
            } catch (CantSendAssetBitcoinsToUserException exception) {
                throw new CantCheckTransferProgressException(exception, "Exception in asset distribution monitor agent", "Cannot send crypto currency to asset user");
            } catch (UnexpectedResultReturnedFromDatabaseException exception) {
                throw new CantCheckTransferProgressException(exception, "Exception in asset distribution monitor agent", "Unexpected result in database query");
            } catch (CantGetCryptoTransactionException exception) {
                throw new CantCheckTransferProgressException(exception, "Exception in asset distribution monitor agent", "Cannot get genesis transaction from asset vault");
            } catch (CantDeliverPendingTransactionsException | CantSendTransactionNewStatusNotificationException exception) {
                throw new CantCheckTransferProgressException(exception, "Exception in asset distribution monitor agent", "Cannot deliver pending transactions");
            } catch (CantTransferDigitalAssetsException exception) {
                throw new CantCheckTransferProgressException(exception, "Exception in asset distribution monitor agent", "Cannot distribute digital asset");
            } catch (CantConfirmTransactionException exception) {
                throw new CantCheckTransferProgressException(exception, "Exception in asset distribution monitor agent", "Cannot confirm transaction");
            } catch (CantGetDigitalAssetFromLocalStorageException exception) {
                throw new CantCheckTransferProgressException(exception, "Exception in asset distribution monitor agent", "Cannot get DigitalAssetMetadata from local storage");
            } catch (CantDeliverDigitalAssetToAssetWalletException exception) {
                throw new CantCheckTransferProgressException(exception, "Exception in asset distribution monitor agent", "Cannot set Credit in asset issuer wallet");
            } catch (RecordsNotFoundException | CantGetAssetStatisticException | CantLoadWalletException e) {
                throw new CantCheckTransferProgressException(e, "Exception in asset distribution monitor agent", "There was a problem registering the distribution statistic");
            } catch (CantGetTransactionsException | CantGetTransactionCryptoStatusException | CantGetAssetIssuerActorsException | CantRegisterDebitException | CantRegisterCreditException e) {
                throw new CantCheckTransferProgressException(e, "Exception in asset distribution monitor agent", "There was a problem while reversing a transaction.");
            } catch (CantAssetUserActorNotFoundException | CantGetAssetUserActorsException e) {
                throw new CantCheckTransferProgressException(e, "Exception in asset distribution monitor agent", "There was a problem while getting the user list.");
            } catch (CantCancellBroadcastTransactionException | CantGetDAPMessagesException | CantSetObjectException | CantCreateDigitalAssetFileException | CantGetBroadcastStatusException | CantBroadcastTransactionException | InvalidParameterException | CantSendMessageException | CantGetIssuerNetworkServiceMessageListException | CantCreateAssetUserActorException | CantSaveStatisticException e) {
                e.printStackTrace();
            }
        }

        /**
         * This method check the pending transactions registered in database and take actions according to CryptoStatus
         *
         * @throws CantExecuteQueryException
         * @throws CantCheckTransferProgressException
         * @throws CantGetCryptoTransactionException
         * @throws UnexpectedResultReturnedFromDatabaseException
         * @throws CantGetDigitalAssetFromLocalStorageException
         * @throws CantDeliverDigitalAssetToAssetWalletException
         */
        private void checkPendingTransactions() throws CantExecuteQueryException, CantCheckTransferProgressException, CantGetCryptoTransactionException, UnexpectedResultReturnedFromDatabaseException, CantGetDigitalAssetFromLocalStorageException, CantDeliverDigitalAssetToAssetWalletException, CantLoadWalletException, RecordsNotFoundException, CantGetAssetStatisticException, CantGetTransactionCryptoStatusException, CantGetBroadcastStatusException, CantBroadcastTransactionException, CantCancellBroadcastTransactionException, CantGetTransactionsException, CantGetAssetUserActorsException, CantAssetUserActorNotFoundException, CantRegisterDebitException, CantGetAssetIssuerActorsException, CantRegisterCreditException, CantCreateDigitalAssetFileException {

            for (DeliverRecord record : assetTransferDAO.getDeliveredRecords()) {
                switch (bitcoinNetworkManager.getCryptoStatus(record.getGenesisTransactionSent())) {
                    case ON_BLOCKCHAIN:
                    case IRREVERSIBLE:
                        CryptoTransaction transactionOnBlockChain = AssetVerification.getCryptoTransactionFromCryptoNetworkByCryptoStatus(bitcoinNetworkManager, record.getDigitalAssetMetadata(), CryptoStatus.ON_BLOCKCHAIN);
                        if (transactionOnBlockChain == null) break; //Not yet....
                        DigitalAssetMetadata digitalAssetMetadata = digitalAssetTransferVault.updateMetadataTransactionChain(record.getGenesisTransaction(), transactionOnBlockChain);
                        digitalAssetTransferVault.updateWalletBalance(digitalAssetMetadata, transactionOnBlockChain, BalanceType.BOOK, TransactionType.DEBIT, DAPTransactionType.RECEPTION, record.getActorAssetUserPublicKey(), Actors.DAP_ASSET_USER, WalletUtilities.DEFAULT_MEMO_DISTRIBUTION);
                        assetTransferDAO.updateDeliveringStatusForTxId(record.getTransactionId(), DistributionStatus.DISTRIBUTION_FINISHED);
                        updateDistributionStatus(DistributionStatus.DISTRIBUTION_FINISHED, record.getGenesisTransaction());
                        break;
                    case REVERSED_ON_BLOCKCHAIN:
                        assetTransferDAO.updateDeliveringStatusForTxId(record.getTransactionId(), DistributionStatus.DISTRIBUTION_FINISHED);
                        updateDistributionStatus(DistributionStatus.DISTRIBUTION_FINISHED, record.getGenesisTransaction());
                        break;
                    case REVERSED_ON_CRYPTO_NETWORK:
                        assetTransferDAO.updateDeliveringStatusForTxId(record.getTransactionId(), DistributionStatus.DISTRIBUTION_FINISHED);
                        updateDistributionStatus(DistributionStatus.DISTRIBUTION_FINISHED, record.getGenesisTransaction());
                        break;
                }
            }

            for (DeliverRecord record : assetTransferDAO.getSendingCryptoRecords()) {
                BroadcastStatus status = bitcoinNetworkManager.getBroadcastStatus(record.getGenesisTransactionSent());
                switch (status.getStatus()) {
                    case WITH_ERROR:
                        System.out.println("VAMM: TRANSFER BROADCAST WITH ERROR");
                        if (record.getAttemptNumber() < AssetTransferDigitalAssetTransactionPluginRoot.BROADCASTING_MAX_ATTEMPT_NUMBER) {
                            System.out.println("VAMM: ATTEMPT NUMBER: " + record.getAttemptNumber());
                            assetTransferDAO.newAttempt(record.getTransactionId());
                            bitcoinNetworkManager.broadcastTransaction(record.getGenesisTransactionSent());
                        } else {
                            System.out.println("VAMM: MAX NUMBER OF ATTEMPT REACHED, CANCELLING.");
                            bitcoinNetworkManager.cancelBroadcast(record.getGenesisTransactionSent());
                        }
                        break;
                    case BROADCASTED:
                        assetTransferDAO.updateDeliveringStatusForTxId(record.getTransactionId(), DistributionStatus.DELIVERED);
                        break;
                    case BROADCASTING:
                        break;
                    case CANCELLED:
                        record.getDigitalAssetMetadata().removeLastTransaction();
                        digitalAssetTransferVault.updateWalletBalance(record.getDigitalAssetMetadata(), distributor.foundCryptoTransaction(record.getDigitalAssetMetadata()), BalanceType.AVAILABLE, TransactionType.CREDIT, DAPTransactionType.RECEPTION, record.getActorAssetUserPublicKey(), Actors.DAP_ASSET_USER, WalletUtilities.DEFAULT_MEMO_ROLLBACK);
                        assetTransferDAO.failedToSendCrypto(record.getTransactionId());
                        break;
                }
            }

        }

        private void checkPendingNetworkEvents() throws CantExecuteQueryException, CantCheckTransferProgressException, UnexpectedResultReturnedFromDatabaseException, CantTransferDigitalAssetsException, CantConfirmTransactionException, CantSendAssetBitcoinsToUserException, CantGetDigitalAssetFromLocalStorageException, CantGetCryptoTransactionException, CantDeliverDigitalAssetToAssetWalletException, CantDeliverPendingTransactionsException, RecordsNotFoundException, CantBroadcastTransactionException, CantLoadWalletException, CantGetAssetUserActorsException, CantAssetUserActorNotFoundException, CantSendMessageException, CantSetObjectException, CantGetTransactionsException, CantGetAssetIssuerActorsException, CantRegisterDebitException, CantRegisterCreditException {
            if (isPendingNetworkLayerEvents()) {
                System.out.println("ASSET TRANSFER is network layer pending events");
                List<Transaction<DigitalAssetMetadataTransaction>> pendingEventsList = assetTransmissionManager.getPendingTransactions(Specialist.ASSET_ISSUER_SPECIALIST);
                System.out.println("ASSET TRANSFER is " + pendingEventsList.size() + " events");
                for (Transaction<DigitalAssetMetadataTransaction> transaction : pendingEventsList) {
                    if (transaction.getInformation().getReceiverType() == PlatformComponentType.ACTOR_ASSET_USER) {
                        DigitalAssetMetadataTransaction digitalAssetMetadataTransaction = transaction.getInformation();
                        System.out.println("ASSET TRANSFER Digital Asset Metadata Transaction: " + digitalAssetMetadataTransaction);
                        DigitalAssetMetadataTransactionType digitalAssetMetadataTransactionType = digitalAssetMetadataTransaction.getType();
                        System.out.println("ASSET TRANSFER Digital Asset Metadata Transaction Type: " + digitalAssetMetadataTransactionType);
                        if (digitalAssetMetadataTransactionType == DigitalAssetMetadataTransactionType.TRANSACTION_STATUS_UPDATE) {
                            String userId = digitalAssetMetadataTransaction.getSenderId();
                            System.out.println("ASSET TRANSFER User Id: " + userId);
                            String genesisTransaction = digitalAssetMetadataTransaction.getGenesisTransaction();
                            System.out.println("ASSET TRANSFER Genesis Transaction: " + genesisTransaction);
                            if (!assetTransferDAO.isGenesisTransactionRegistered(genesisTransaction)) {
                                System.out.println("ASSET TRANSFER This genesisTransaction is not registered in database: " + genesisTransaction);
                                continue;
                            }
                            String registeredUserActorId = assetTransferDAO.getActorUserPublicKeyByGenesisTransaction(genesisTransaction);
                            System.out.println("ASSET TRANSFER User Actor Is: " + registeredUserActorId);
                            if (!registeredUserActorId.equals(userId)) {
                                throw new CantTransferDigitalAssetsException("User id from Asset distribution: " + userId + "\nRegistered publicKey: " + registeredUserActorId + "They are not equals");
                            }
                            assetTransferDAO.updateDistributionStatusByGenesisTransaction(digitalAssetMetadataTransaction.getDistributionStatus(), genesisTransaction);
                            assetTransmissionManager.confirmReception(transaction.getTransactionID());
                        }
                    }
                }
                assetTransferDAO.updateEventStatus(assetTransferDAO.getPendingNetworkLayerEvents().get(0));
            }

            //ASSET ACCEPTED BY USER
            List<String> assetAcceptedGenesisTransactionList = assetTransferDAO.getGenesisTransactionByAssetAcceptedStatus();
            for (String assetAcceptedGenesisTransaction : assetAcceptedGenesisTransactionList) {
                String actorUserCryptoAddress = assetTransferDAO.getActorUserCryptoAddressByGenesisTransaction(assetAcceptedGenesisTransaction);
                System.out.println("ASSET TRANSFER actorUserCryptoAddress: " + actorUserCryptoAddress);
                //For now, I set the cryptoAddress for Bitcoins
                CryptoAddress cryptoAddressTo = new CryptoAddress(actorUserCryptoAddress, CryptoCurrency.BITCOIN);
                System.out.println("ASSET TRANSFER cryptoAddressTo: " + cryptoAddressTo);
                DeliverRecord record = assetTransferDAO.getLastDelivering(assetAcceptedGenesisTransaction);
                switch (record.getState()) {
                    case DELIVERING:
                        DigitalAssetMetadata digitalAsset = digitalAssetTransferVault.getDigitalAssetMetadataFromWallet(assetAcceptedGenesisTransaction, record.getNetworkType());
                        //NOTIFYING ISSUER THAT I'M TRANSFERING THIS ASSET
                        updateDistributionStatus(DistributionStatus.NOTIFYING_ISSUER, assetAcceptedGenesisTransaction);
                        assetTransferDAO.updateDeliveringStatusForTxId(record.getTransactionId(), DistributionStatus.NOTIFYING_ISSUER);
                        ActorAssetUser userToSend = getUserForGenesisTx(assetAcceptedGenesisTransaction);
                        sendActorInformation(digitalAsset, userToSend, record.getNetworkType());

                        //SENDING THE CRYPTO
                        updateDistributionStatus(DistributionStatus.SENDING_CRYPTO, assetAcceptedGenesisTransaction);
                        assetTransferDAO.sendingBitcoins(assetAcceptedGenesisTransaction, digitalAsset.getLastTransactionHash());
                        sendCryptoAmountToRemoteActor(digitalAsset);
                        break;
                    case DELIVERING_CANCELLED:
                        assetTransferDAO.updateDistributionStatusByGenesisTransaction(DistributionStatus.SENDING_CRYPTO_FAILED, assetAcceptedGenesisTransaction);
                        break;
                    default:
                        System.out.println("This transaction has already been updated.");
                        break;
                }
            }

            //ASSET REJECTED BY USER
            //TODO MODIFY THIS!!!!
            List<String> assetRejectedByContractGenesisTransactionList = assetTransferDAO.getGenesisTransactionByAssetRejectedByContractStatus();
            for (String assetRejectedGenesisTransaction : assetRejectedByContractGenesisTransactionList) {
                DigitalAssetMetadata digitalAssetMetadata = digitalAssetTransferVault.getUserWallet(BlockchainNetworkType.getDefaultBlockchainNetworkType()).getDigitalAssetMetadata(assetRejectedGenesisTransaction);
                String internalId = assetTransferDAO.getTransactionIdByGenesisTransaction(assetRejectedGenesisTransaction);
                List<CryptoTransaction> genesisTransactionList = bitcoinNetworkManager.getCryptoTransactions(digitalAssetMetadata.getLastTransactionHash());
                if (genesisTransactionList == null || genesisTransactionList.isEmpty()) {
                    throw new CantCheckTransferProgressException("Cannot get the CryptoTransaction from Crypto Network for " + assetRejectedGenesisTransaction);
                }
                String userPublicKey = assetTransferDAO.getActorUserPublicKeyByGenesisTransaction(assetRejectedGenesisTransaction);
                System.out.println("ASSET REJECTED BY CONTRACT!!! : " + digitalAssetMetadata);
                digitalAssetTransferVault.updateWalletBalance(digitalAssetMetadata, genesisTransactionList.get(0), BalanceType.AVAILABLE, TransactionType.CREDIT, DAPTransactionType.RECEPTION, userPublicKey, Actors.DAP_ASSET_USER, WalletUtilities.DEFAULT_MEMO_DISTRIBUTION);
            }

            List<String> assetRejectedByHashGenesisTransactionList = assetTransferDAO.getGenesisTransactionByAssetRejectedByHashStatus();
            for (String assetRejectedGenesisTransaction : assetRejectedByHashGenesisTransactionList) {
                DigitalAssetMetadata digitalAssetMetadata = digitalAssetTransferVault.getUserWallet(BlockchainNetworkType.getDefaultBlockchainNetworkType()).getDigitalAssetMetadata(assetRejectedGenesisTransaction);
                String internalId = assetTransferDAO.getTransactionIdByGenesisTransaction(assetRejectedGenesisTransaction);
                List<CryptoTransaction> genesisTransactionList = bitcoinNetworkManager.getCryptoTransactions(digitalAssetMetadata.getLastTransactionHash());
                if (genesisTransactionList == null || genesisTransactionList.isEmpty()) {
                    throw new CantCheckTransferProgressException("Cannot get the CryptoTransaction from Crypto Network for " + assetRejectedGenesisTransaction);
                }
                System.out.println("ASSET REJECTED BY HASH!!! : " + digitalAssetMetadata);
                String userPublicKey = assetTransferDAO.getActorUserPublicKeyByGenesisTransaction(assetRejectedGenesisTransaction);
                digitalAssetTransferVault.updateWalletBalance(digitalAssetMetadata, genesisTransactionList.get(0), BalanceType.AVAILABLE, TransactionType.CREDIT, DAPTransactionType.RECEPTION, userPublicKey, Actors.DAP_ASSET_USER, WalletUtilities.DEFAULT_MEMO_DISTRIBUTION);
            }
        }

        private void checkDeliveringTime() throws CantExecuteDatabaseOperationException, CantCheckTransferProgressException, CantExecuteQueryException, UnexpectedResultReturnedFromDatabaseException, CantGetCryptoTransactionException, CantGetTransactionsException, CantLoadWalletException, CantRegisterCreditException, CantRegisterDebitException, CantGetAssetIssuerActorsException, CantSendTransactionNewStatusNotificationException, CantGetAssetUserActorsException, CantAssetUserActorNotFoundException, InvalidParameterException {
            for (DeliverRecord record : assetTransferDAO.getDeliveringRecords()) {
                DistributionStatus currentStatus = assetTransferDAO.getDistributionStatusForGenesisTx(record.getGenesisTransaction());
                if (new Date().after(record.getTimeOut()) && currentStatus == DistributionStatus.DELIVERING) {
                    try {
                        bitcoinNetworkManager.cancelBroadcast(record.getDigitalAssetMetadata().getLastTransactionHash());
                    } catch (CantCancellBroadcastTransactionException e) {
                        e.printStackTrace();
                    }
                    record.getDigitalAssetMetadata().removeLastTransaction();
                    digitalAssetTransferVault.updateWalletBalance(record.getDigitalAssetMetadata(), distributor.foundCryptoTransaction(record.getDigitalAssetMetadata()), BalanceType.AVAILABLE, TransactionType.CREDIT, DAPTransactionType.RECEPTION, record.getActorAssetUserPublicKey(), Actors.DAP_ASSET_USER, WalletUtilities.DEFAULT_MEMO_ROLLBACK);
                    assetTransferDAO.cancelDelivering(record.getTransactionId());
                }
            }
        }

        private void checkTransferMessages() throws CantCreateAssetUserActorException, CantGetIssuerNetworkServiceMessageListException, CantLoadWalletException, CantSaveStatisticException, CantGetDAPMessagesException {
            for (DAPMessage message : assetIssuerActorNetworkServiceManager.getUnreadDAPMessagesByType(DAPMessageType.ASSET_TRANSFER)) {
                AssetTransferContentMessage content = (AssetTransferContentMessage) message.getMessageContent();
                ActorAssetUser actorTo = (ActorAssetUser) content.getActor();
                actorAssetUserManager.createActorAssetUserRegisterInNetworkService(actorTo);
                assetIssuerWalletManager.loadAssetIssuerWallet(WalletUtilities.WALLET_PUBLIC_KEY, content.getNetworkType()).newMovement(message.getActorSender(), actorTo, content.getMetadataId());
            }
        }

        private void updateDistributionStatus(DistributionStatus distributionStatus, String genesisTransaction) throws CantExecuteQueryException, UnexpectedResultReturnedFromDatabaseException {
            assetTransferDAO.updateDistributionStatusByGenesisTransaction(distributionStatus, genesisTransaction);
        }

        private boolean isPendingNetworkLayerEvents() throws CantExecuteQueryException {
            return assetTransferDAO.isPendingNetworkLayerEvents();
        }

        public void setAssetTransferDAO(AssetTransferDAO assetTransferDAO) {
            this.assetTransferDAO = assetTransferDAO;
        }

        private void sendCryptoAmountToRemoteActor(DigitalAssetMetadata digitalAssetMetadata) throws CantBroadcastTransactionException {
            System.out.println("ASSET TRANSFER sending genesis amount from asset vault");
            bitcoinNetworkManager.broadcastTransaction(digitalAssetMetadata.getLastTransactionHash());
        }

        private void sendActorInformation(DigitalAssetMetadata digitalAssetMetadata, ActorAssetUser newUser, BlockchainNetworkType networkType) throws CantSetObjectException, CantGetAssetUserActorsException, CantSendMessageException {
            AssetTransferContentMessage content = new AssetTransferContentMessage(newUser, digitalAssetMetadata.getMetadataId(), networkType);
            ActorAssetUser actorSender = actorAssetUserManager.getActorAssetUser();
            ActorAssetIssuer actorReceiver = (ActorAssetIssuer) ActorUtils.constructActorFromIdentity(digitalAssetMetadata.getDigitalAsset().getIdentityAssetIssuer());
            DAPMessage dapMessage = new DAPMessage(content, actorSender, actorReceiver);
            assetIssuerActorNetworkServiceManager.sendMessage(dapMessage);
        }

        public ActorAssetUser getUserForGenesisTx(String genesisTx) throws CantCheckTransferProgressException, UnexpectedResultReturnedFromDatabaseException, CantAssetUserActorNotFoundException, CantGetAssetUserActorsException {
            return actorAssetUserManager.getActorByPublicKey(assetTransferDAO.getActorUserPublicKeyByGenesisTransaction(genesisTx));
        }

    }
}