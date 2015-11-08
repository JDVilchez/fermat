package com.bitdubai.fermat.dap_plugin.layer.digital_asset_transaction.asset_distribution.developer.bitdubai.version1.structure.incoming_asset_reversed_on_crypto_network_waiting_transference_asset_user_event_handler;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
import com.bitdubai.fermat_api.layer.dmp_transaction.TransactionServiceNotStartedException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.CantSaveEventException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_distribution.developer.bitdubai.version_1.structure.database.AssetDistributionDao;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_distribution.developer.bitdubai.version_1.structure.database.AssetDistributionDatabaseConstants;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_distribution.developer.bitdubai.version_1.structure.events.AssetDistributionRecorderService;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_distribution.developer.bitdubai.version_1.structure.events.IncomingAssetReversedOnCryptoNetworkWaitingTransferenceAssetUserEventHandler;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.enums.EventType;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Luis Campo (campusprize@gmail.com) on 02/11/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class ReversedOnCryptoNetworkHandleEventTest {
    @Mock
    private EventManager eventManager;
    @Mock
    private FermatEventListener fermatEventListener1;
    @Mock
    private  FermatEventListener fermatEventListener2;
    @Mock
    private FermatEventListener fermatEventListener3;
    @Mock
    private  FermatEventListener fermatEventListener4;
    @Mock
    private  FermatEventListener fermatEventListener5;

    @Mock
    private  FermatEvent fermatEvent;
    @Mock
    private ErrorManager errorManager;
    private UUID pluginId;
    @Mock
    private PluginDatabaseSystem pluginDatabaseSystem;
    private AssetDistributionRecorderService assetDistributionRecorderService;
    @Mock
    private DatabaseFactory mockDatabaseFactory;
    private Database database = Mockito.mock(Database.class);
    private AssetDistributionDao assetDistributionDao = Mockito.mock(AssetDistributionDao.class);
    private IncomingAssetReversedOnCryptoNetworkWaitingTransferenceAssetUserEventHandler incomingAssetReversedOnCryptoNetworkWaitingTransferenceAssetUserEventHandler = Mockito.mock(IncomingAssetReversedOnCryptoNetworkWaitingTransferenceAssetUserEventHandler.class);

    @Before
    public void init() throws Exception {
        pluginId = UUID.randomUUID();
        assetDistributionRecorderService = new AssetDistributionRecorderService(assetDistributionDao, eventManager);
        assetDistributionRecorderService.setEventManager(eventManager);
        incomingAssetReversedOnCryptoNetworkWaitingTransferenceAssetUserEventHandler.setAssetDistributionRecorderService(assetDistributionRecorderService);
        setUpMockitoRules();
    }
    private void setUpMockitoRules() throws Exception {
        when(pluginDatabaseSystem.openDatabase(pluginId, AssetDistributionDatabaseConstants.ASSET_DISTRIBUTION_DATABASE)).thenReturn(database);
       when(eventManager.getNewListener(EventType.INCOMING_ASSET_ON_CRYPTO_NETWORK_WAITING_TRANSFERENCE_ASSET_USER)).thenReturn(fermatEventListener1);
        when(eventManager.getNewListener( EventType.INCOMING_ASSET_ON_BLOCKCHAIN_WAITING_TRANSFERENCE_ASSET_USER)).thenReturn(fermatEventListener2);
        when(eventManager.getNewListener(EventType.INCOMING_ASSET_REVERSED_ON_CRYPTO_NETWORK_WAITING_TRANSFERENCE_ASSET_USER)).thenReturn(fermatEventListener3);
        when(eventManager.getNewListener(EventType.INCOMING_ASSET_REVERSED_ON_BLOCKCHAIN_WAITING_TRANSFERENCE_ASSET_USER)).thenReturn(fermatEventListener4);
        when(eventManager.getNewListener(EventType.RECEIVED_NEW_DIGITAL_ASSET_METADATA_NOTIFICATION)).thenReturn(fermatEventListener5);
    }

    @Test
    public void handleEventSucces () throws FermatException {
        assetDistributionRecorderService.start();
        incomingAssetReversedOnCryptoNetworkWaitingTransferenceAssetUserEventHandler.handleEvent(fermatEvent);
    }

    @Test
    public void handleEventThrowCantSaveEventException () throws FermatException {
        assetDistributionRecorderService.start();
        try {
            incomingAssetReversedOnCryptoNetworkWaitingTransferenceAssetUserEventHandler.handleEvent(null);
        }catch (Exception ex) {
            assertThat(ex).isInstanceOf(CantSaveEventException.class);
        }
    }

    @Test
    public void handleEventThrowTransactionServiceNotStartedException () throws FermatException {
        assetDistributionRecorderService.start();
        assetDistributionRecorderService.stop();
        try {
            incomingAssetReversedOnCryptoNetworkWaitingTransferenceAssetUserEventHandler.handleEvent(null);
        }catch (Exception ex) {
            assertThat(ex).isInstanceOf(TransactionServiceNotStartedException.class);
        }
    }
}