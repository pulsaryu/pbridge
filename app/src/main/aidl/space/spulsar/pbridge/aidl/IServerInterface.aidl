// IServerInterface.aidl
package space.spulsar.pbridge.aidl;

import space.spulsar.pbridge.aidl.ISendListener;
import space.spulsar.pbridge.aidl.IResult;


// Declare any non-default types here with import statements

interface IServerInterface {

    void send(in String contentType, in String contentBody, in ISendListener listener);

    IResult sendSync(in String contentType, in String contentBody);
}
