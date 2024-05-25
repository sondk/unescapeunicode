package com.sondk.unescapeunicode;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.ui.Selection;
import burp.api.montoya.ui.editor.EditorOptions;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpResponseEditor;
import org.apache.commons.text.translate.UnicodeUnescaper;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MyExtensionProvidedHttpResponseEditor implements ExtensionProvidedHttpResponseEditor {

    private final HttpResponseEditor responseEditor;
    private final Logging logging;


    MyExtensionProvidedHttpResponseEditor(MontoyaApi api) {
        responseEditor = api.userInterface().createHttpResponseEditor(EditorOptions.READ_ONLY);
        logging = api.logging();
    }

    @Override
    public HttpResponse getResponse() {
        return null;
    }


    @Override
    public void setRequestResponse(HttpRequestResponse requestResponse) {
        HttpResponse rawResponse = requestResponse.response();
        // Unescape UNICODE body of HTTP Response
        byte[] unescapeUnicodeBody = new UnicodeUnescaper().translate(rawResponse.bodyToString()).getBytes();
        // Make a copy of response with Unescape UNICODE body
        HttpResponse unescapeUnicodeResponse = rawResponse.withBody(ByteArray.byteArray(unescapeUnicodeBody));

        // Sometimes, MIME type of request is actually JSON but Content-Type header is not application/json.
        // This make Pretty tab has no pretty anymore.
        // Add this logic to handle this case.
        if (!rawResponse.statedMimeType().description().equals("JSON") && rawResponse.inferredMimeType().description().equals("JSON")) {
            this.responseEditor.setResponse(unescapeUnicodeResponse.withUpdatedHeader("Content-Type", "application/json"));
        } else {
            this.responseEditor.setResponse(unescapeUnicodeResponse);
        }

    }

    @Override
    public boolean isEnabledFor(HttpRequestResponse requestResponse) {

        if (requestResponse.request() != null) {
            var path = requestResponse.request().pathWithoutQuery().toLowerCase();
            if (path.endsWith(".js") || path.endsWith(".gif") || path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".css")) {
                return false;
            }
        }
        // Regex to detect HTTP Response with UNICODE in body
        if (requestResponse.hasResponse()) {
            Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
            Matcher matcher = pattern.matcher(requestResponse.response().bodyToString());
            return matcher.find();
        }
        // HTTP Response has no UNICODE > return false
        return false;
    }

    @Override
    public String caption() {
        return "Unescape Unicode";
    }

    @Override
    public Component uiComponent() {
        logging.logToOutput(String.valueOf(responseEditor.uiComponent().getComponentAt(0,0)));
        return responseEditor.uiComponent();
    }

    @Override
    public Selection selectedData() {
        return null;
    }

    @Override
    public boolean isModified() {
        return responseEditor.isModified();
    }
}
