/*
 * Copyright 2011 Pierre-Yves Ricau
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package info.piwai.android.frenchkeyboardputain;

import java.util.Arrays;
import java.util.List;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

/**
 * 
 */
public class SoftKeyboard extends InputMethodService 
        implements KeyboardView.OnKeyboardActionListener {
    
    private static final int ANGRY_PUTAIN = -69;
    private static final int SAD_PUTAIN = -13;
    private static final int AMAZED_PUTAIN = -42;
    private static final int SOUTH_PUTAIN = -31;
    
    private static final List<Integer> PUTAIN_INPUTS = (List<Integer>) Arrays.asList(ANGRY_PUTAIN, SAD_PUTAIN, AMAZED_PUTAIN, SOUTH_PUTAIN);
    
    private KeyboardView mInputView;
    
    private int mLastDisplayWidth;
    
    private Keyboard mPutainKeyboard;
    
    /**
     * This is the point where you can do all of your UI initialization.  It
     * is called after creation and any configuration change.
     */
    @Override public void onInitializeInterface() {
        if (mPutainKeyboard != null) {
            // Configuration changes can happen after the keyboard gets recreated,
            // so we need to be able to re-build the keyboards if the available
            // space has changed.
            int displayWidth = getMaxWidth();
            if (displayWidth == mLastDisplayWidth) return;
            mLastDisplayWidth = displayWidth;
        }
        mPutainKeyboard = new Keyboard(this, R.xml.putain_keyboard);
    }
    
    /**
     * Called by the framework when your view for creating input needs to
     * be generated.  This will be called the first time your input method
     * is displayed, and every time it needs to be re-created such as due to
     * a configuration change.
     */
    @Override public View onCreateInputView() {
        mInputView = (KeyboardView) getLayoutInflater().inflate(R.layout.input, null);
        mInputView.setOnKeyboardActionListener(this);
        mInputView.setKeyboard(mPutainKeyboard);
        return mInputView;
    }


    /**
     * This is called when the user is done editing a field.  We can use
     * this to reset our state.
     */
    @Override public void onFinishInput() {
        super.onFinishInput();
        if (mInputView != null) {
            mInputView.closing();
        }
    }
    
    @Override public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        // Apply the selected keyboard to the input view.
        mInputView.setKeyboard(mPutainKeyboard);
        mInputView.closing();
    }
    
    // Implementation of KeyboardViewListener
    public void onKey(int primaryCode, int[] keyCodes) {
        if (PUTAIN_INPUTS.contains(primaryCode)) {
            InputConnection ic = getCurrentInputConnection();
            if (ic == null) return;
            String output = putainOutput(primaryCode);
            ic.beginBatchEdit();
            ic.commitText(output, 1);
            ic.endBatchEdit();
        }
    }

    private String putainOutput(int primaryCode) {
        switch (primaryCode) {
        case ANGRY_PUTAIN:
            return "Putain ! ";
        case AMAZED_PUTAIN:
            return "Ooh putain ?? ";
        case SAD_PUTAIN:
            return "Putain... ";
        case SOUTH_PUTAIN:
            return "Putaing con ! ";
        }
        return "";
    }

    public void onText(CharSequence text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        ic.beginBatchEdit();
        ic.commitText(text, 0);
        ic.endBatchEdit();
    }

    private void handleBackspace() {
        keyDownUp(KeyEvent.KEYCODE_DEL);
    }
    
    /**
     * Helper to send a key down / key up pair to the current editor.
     */
    private void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }

    private void handleClose() {
        requestHideSelf(0);
        mInputView.closing();
    }

    
    public void swipeRight() {
    }
    
    public void swipeLeft() {
        handleBackspace();
    }

    public void swipeDown() {
        handleClose();
    }

    public void swipeUp() {
    }
    
    public void onPress(int primaryCode) {
    }
    
    public void onRelease(int primaryCode) {
    }
}
