// IClientInterface.aidl
package space.spulsar.pbridge;
import space.spulsar.pbridge.AidlResult;

// Declare any non-default types here with import statements

interface IClientInterface {

    void send();

    AidlResult sendSync(String contentType, String contentBody);
}
