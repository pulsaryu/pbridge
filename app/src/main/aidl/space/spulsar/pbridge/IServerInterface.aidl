// IServerInterface.aidl
package space.spulsar.pbridge;

import space.spulsar.pbridge.IClientInterface;
import space.spulsar.pbridge.AidlResult;


// Declare any non-default types here with import statements

interface IServerInterface {

    void registerClient(in IClientInterface client);

    void send();

    AidlResult sendSync(String contentType, String contentBody);
}
