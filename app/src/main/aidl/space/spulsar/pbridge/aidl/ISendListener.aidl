// IReceiveListener.aidl
package space.spulsar.pbridge.aidl;

// Declare any non-default types here with import statements

interface ISendListener {

    void onComplete(in String contentType, in String contentBody);
}
