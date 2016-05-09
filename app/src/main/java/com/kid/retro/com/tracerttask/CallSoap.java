package com.kid.retro.com.tracerttask;

/**
 * Created by Kid on 1/9/2016.
 */
public class CallSoap
{
    public final String SOAP_ACTION = " http://tempuri.org/UserRegistration";

    public final String OPERATION_NAME = "UserRegistration";

    public  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

    public  final String SOAP_ADDRESS = "http://retroinfotech.com/tracert/tracert.asmx";

    public CallSoap()
    {
        //SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //JSONObject jsonObject = new JSONObject();

    }

}
