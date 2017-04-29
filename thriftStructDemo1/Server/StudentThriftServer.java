import java.io.*;
import org.apache.thrift.*;
import org.apache.thrift.server.*;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;

import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.TException;

import java.util.List;
import java.util.*;

public class StudentThriftServer {
    public static final int SERVER_PORT = 9527;

    public static void main(String[] arg) throws TException{
        TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
        TProcessor tProcessor = new StudentService.Processor(new StudentServiceImpl());

        Factory protFactory = new TBinaryProtocol.Factory(true, true);

        TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
			  args.processor(tProcessor);
			  args.protocolFactory(protFactory);
        //block I/O
        //TServer server = new TSimpleServer(tProcessor, serverTransport); error!
        TServer server = new TThreadPoolServer(args);

        System.out.println("Start server on port 9527...");
        server.serve();
    }
}
