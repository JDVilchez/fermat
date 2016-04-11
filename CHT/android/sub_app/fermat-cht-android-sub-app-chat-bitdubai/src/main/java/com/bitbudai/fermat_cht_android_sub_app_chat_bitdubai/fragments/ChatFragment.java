package com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.adapters.ChatAdapterView;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.sessions.ChatSession;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.settings.ChatSettings;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_cht_android_sub_app_chat_bitdubai.R;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatManager;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatModuleManager;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatPreferenceSettings;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

/**
 * Chat Fragment
 *
 * @author Jose Cardozo josejcb (josejcb89@gmail.com) on 30/12/15.
 * @version 1.0
 * Update by Miguel Payarez on 15/01/2016
 */

public class ChatFragment extends AbstractFermatFragment {//ActionBarActivity

    // Fermat Managers
    private ChatManager chatManager;
    private ChatModuleManager moduleManager;
    private ErrorManager errorManager;
    private SettingsManager<ChatSettings> settingsManager;
    private ChatPreferenceSettings chatSettings;
    private ChatSession chatSession;
    private Toolbar toolbar;

    //ArrayList<String> historialmensaje = new ArrayList<>();
    //SwipeRefreshLayout mSwipeRefreshLayout;
    // Defines a tag for identifying log entries
    String TAG="CHT_ChatFragment";
    private ChatAdapterView adapter;

    public static ChatFragment newInstance() { return new ChatFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        try {
            chatSession = ((ChatSession) appSession);
            moduleManager = chatSession.getModuleManager();
            chatManager = moduleManager.getChatManager();
            errorManager = appSession.getErrorManager();

            //Obtain chatSettings  or create new chat settings if first time opening chat platform
            chatSettings = null;
            try {
                chatSettings = moduleManager.getSettingsManager().loadAndGetSettings(appSession.getAppPublicKey());
            } catch (Exception e) {
                chatSettings = null;
            }

            if (chatSettings == null) {
                chatSettings = new ChatPreferenceSettings();
                chatSettings.setIsPresentationHelpEnabled(true);
                try {
                    moduleManager.getSettingsManager().persistSettings(appSession.getAppPublicKey(), chatSettings);
                } catch (Exception e) {
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                }
            }

            toolbar = getToolbar();
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.cht_ic_back_buttom));

            //ChatAdapter chatAdapter = new ChatAdapter(getContext(), (chatHistory != null) ? chatHistory : new ArrayList<ChatMessage>());

//            if(contactid!=null){
//                if(chatManager.getContactByContactId(contactid).getRemoteName().equals("Not registered contact"))
//                {
//                    setHasOptionsMenu(true);
//                }else{ setHasOptionsMenu(false); }
//            }

        } catch (Exception e) {
            if (errorManager != null)
                errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }
    }

    @Override
    public void onUpdateViewOnUIThread(String code) {
        super.onUpdateViewOnUIThread(code);
        if(code.equals("13")){
            adapter.refreshEvents();
        }
    }

//    @Override
//    public void onBackPressed() {
//        adapter.onBackPressed();
//    }

//    public interface KeyboardVisibilityListener {
//        void onKeyboardVisibilityChanged(boolean keyboardVisible);
//    }

//    public static void setKeyboardVisibilityListener(Activity activity, final ChatFragment.KeyboardVisibilityListener keyboardVisibilityListener) {
//        final View contentView = activity.findViewById(android.R.id.content);
//        final ChatAdapterView adapter = adapter;
//        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            private int mPreviousHeight;
//            @Override
//            public void onGlobalLayout() {
//                int newHeight = contentView.getHeight();
//                if (mPreviousHeight != 0) {
//                    if (mPreviousHeight > newHeight) {
//                        // Height decreased: keyboard was shown
//                        keyboardVisibilityListener.onKeyboardVisibilityChanged(true);
//                    } else if (mPreviousHeight < newHeight) {
//                        adapter.onBackPressed();
//                        keyboardVisibilityListener.onKeyboardVisibilityChanged(false);
//                    } else {
//                        // No change
//                    }
//                }
//                mPreviousHeight = newHeight;
//            }
//        });
//    }




//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        // Checks whether a hardware keyboard is available
//        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
//            Toast.makeText(getActivity(), "keyboard visible", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
//            Toast.makeText(getActivity(), "keyboard hidden", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    boolean isOpened = false;
//
//    public void setListenerToRootView(){
//        final View activityRootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
//        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
//                if (heightDiff > 100) { // 99% of the time the height diff will be due to a keyboard.
//                    Toast.makeText(getActivity(), "Gotcha!!! softKeyboardup", Toast.LENGTH_LONG).show();
//
//                    if (isOpened == false) {
//                        //Do two things, make the view top visible and the editText smaller
//                    }
//                    isOpened = true;
//                } else if (isOpened == true) {
//                    Toast.makeText(getActivity(), "softkeyborad Down!!!", Toast.LENGTH_LONG).show();
//                    isOpened = false;
//                }
//            }
//        });
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {//private void initControls() {}
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(Activities.CHT_CHAT_OPEN_CHATLIST, appSession.getAppPublicKey());
            }
        });
        getActivity().getWindow().setBackgroundDrawableResource(R.drawable.cht_background);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //getActivity().setContentView(R.layout.chat);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        adapter=new ChatAdapterView.Builder(inflater.getContext())
                .insertInto(container)
                .addModuleManager(moduleManager)
                .addErrorManager(errorManager)
                .addChatSession(chatSession)
                .addAppSession(appSession)
                .addChatManager(chatManager)
                .addChatSettings(chatSettings)
                .addToolbar(toolbar)
                .build();
        return adapter;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        // Inflate the menu items
        //inflater.inflate(R.menu.chat_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            //changeActivity(Activities.CHT_CHAT_OPEN_CONNECTIONLIST, appSession.getAppPublicKey());
            return true;
        }
        if (item.getItemId() == R.id.menu_block_contact) {
            //changeActivity(Activities.CHT_CHAT_OPEN_CONNECTIONLIST, appSession.getAppPublicKey());
            return true;
        }
        if (item.getItemId() == R.id.menu_clean_chat) {
            //changeActivity(Activities.CHT_CHAT_OPEN_CONNECTIONLIST, appSession.getAppPublicKey());
            return true;
        }
        if (item.getItemId() == R.id.menu_send_chat_email) {
            //changeActivity(Activities.CHT_CHAT_OPEN_CONNECTIONLIST, appSession.getAppPublicKey());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}