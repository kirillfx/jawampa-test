import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClientBuilder;
import ws.wamp.jawampa.connection.IWampConnectorProvider;
import ws.wamp.jawampa.transport.netty.NettyWampClientConnectorProvider;

/**
 * Created by kirill on 18/7/17.
 */
public class Main {

    public static void main(String[] args) {

        WampClient client;

        try {
            WampClientBuilder builder = new WampClientBuilder();
            IWampConnectorProvider connectorProvider = new NettyWampClientConnectorProvider();
            builder.withConnectorProvider(connectorProvider)
                    .withUri("wss://api.poloniex.com")
                    .withRealm("realm1")
                    .withInfiniteReconnects()
                    .withReconnectInterval(5, TimeUnit.SECONDS);


            client = builder.build();
            client.open();
            Thread.sleep(5000);

            client.statusChanged().subscribe(t1 -> {
                if (t1 instanceof WampClient.ConnectedState) {
                    System.out.println("subscribing");
                    client.makeSubscription("trollbox")
                            .subscribe((s) -> { System.out.println(s.arguments()); });
                    client.makeSubscription("ticker")
                            .subscribe((s) -> { System.out.println(s.arguments()); });
                } else {
                    System.out.println("Not connected");
                }
            });

            System.in.read();
            System.out.println("Done");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

}
