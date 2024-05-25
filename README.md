Unescape Unicode Extension
============================

###### Adds a new tab to Burp's HTTP response editor, in order to handle a data escape unicode format

 ---

This extension provides a new tab on the message editor for responses that contain escape unicode characters.

The extension uses the following techniques:
- It creates a custom response tab on the message editor, provided that `escape unicode characters parameter` is present
- The editor is set to be read-only
- The value of body is UNICODE unescaped and displayed in the `Unescape Unicode` tab
