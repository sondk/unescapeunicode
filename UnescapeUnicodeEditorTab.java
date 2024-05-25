package com.sondk.unescapeunicode;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

public class UnescapeUnicodeEditorTab implements BurpExtension {
    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("Unescape Unicode");
        api.userInterface().registerHttpResponseEditorProvider(new MyHttpResponseEditorProvider(api));
    }
}
