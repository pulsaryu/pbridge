# pbridge

Make app send and fetch message easier.

## Add pbridge to your project

Via gradle:

```groovy
implementation 'space.spulsar:pbridge:1.0'
```

## Use

Fist connect target app, example the target app package name is "com.example.a", so we can do like this:
```java
BridgeManager.getInstance().connect(context, "com.example.a", new IBridgeConnection() {
    @Override
    public void onServiceConnected() {
        Log.d(TAG, "onServiceConnected: ");
    }

    @Override
    public void onServiceDisconnected() {
        Log.d(TAG, "onServiceDisconnected: ");
    }
});
```

Set fetch listener, when the targe app send message to your app, you can receive like this:
```java
BridgeManager.getInstance().setFetchListener(new IFetchListener() {
    @Override
    public void fetch(String contentType, String contentBody, IResultCallback iResultCallback) {
        Log.d(TAG, "fetch: type: " + contentType + ", body: " + contentBody);
    }

    @Override
    public IResult fetchSync(String s, String s1) {
        return null;
    }
});
```

# License
Pbridge binaries and source code can be used according to the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
