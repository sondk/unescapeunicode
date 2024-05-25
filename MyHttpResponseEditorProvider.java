package com.sondk.unescapeunicode;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.editor.extension.EditorCreationContext;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpResponseEditor;
import burp.api.montoya.ui.editor.extension.HttpResponseEditorProvider;

class MyHttpResponseEditorProvider implements HttpResponseEditorProvider {
    private final MontoyaApi api;

    MyHttpResponseEditorProvider(MontoyaApi api) {
        this.api = api;
    }

    @Override
    public ExtensionProvidedHttpResponseEditor provideHttpResponseEditor(EditorCreationContext creationContext) {
        return new MyExtensionProvidedHttpResponseEditor(api);
    }
}

