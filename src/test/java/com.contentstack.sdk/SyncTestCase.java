package com.contentstack.sdk;

import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SyncTestCase {

    private Stack stack;
    private static final String prod_api_key = "blt477ba55f9a67bcdf";
    private static final String prod_delivery_Token = "cs7731f03a2feef7713546fde5";
    private static final String environment = "web";

    private int itemsSize = 0;
    private int counter = 0;
    private String dateISO = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public SyncTestCase() throws Exception {
        Config config = new Config();
        config.setHost("cdn.contentstack.io");
        stack = Contentstack.stack(prod_api_key, prod_delivery_Token, environment, config);
    }


    @Test
    public void testSyncInit() {

        stack.sync(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                counter = 33;
                if (error == null) {

                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();

                    printLog("sync stack size  :"+syncStack.getItems().size());
                    printLog("sync stack count  :"+syncStack.getCount());
                    syncStack.getItems().forEach(item->{
                        printLog("init sync item: --> "+item.toString());
                    });
                }

            }});

        assertEquals(123, counter);
    }




    @Test
    public void testSyncToken() {
        stack.syncToken("bltbb61f31a70a572e6c9506a", new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null) {
                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();

                    printLog("sync token size  :"+syncStack.getItems().size());
                    printLog("sync token count  :"+syncStack.getCount());
                    syncStack.getItems().forEach(item->{
                        printLog("token item: --> "+item.toString());
                    });
                }
            }
        });

        assertEquals( 100, itemsSize);
    }






    @Test
    public void testPaginationToken() {
        stack.syncPaginationToken("blt7f35951d259183fba680e1", new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null) {
                    itemsSize += syncStack.getItems().size();
                    counter = syncStack.getCount();

                    printLog("sync pagination size  :"+syncStack.getItems().size());
                    printLog("sync pagination count  :"+syncStack.getCount());
                    syncStack.getItems().forEach(item->{
                        printLog("pagination item: --> "+item.toString());
                    });
                }
                assertEquals( 100, itemsSize);
            }
        });

    }





    @Test
    public void testSyncWithDate() throws ParseException {

        final Date start_date = sdf.parse("2018-10-07");
        stack.syncFromDate(start_date, new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {

                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();
                    for (JSONObject jsonObject1 : syncStack.getItems()) {
                        if (jsonObject1.has("event_at")) {

                            dateISO = jsonObject1.optString("event_at");
                            System.out.println("date iso -->"+dateISO);
                            String serverDate = returnDateFromISOString(dateISO);
                            Date dateServer = null;
                            try {
                                dateServer = sdf.parse(serverDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            System.out.println("dateServer -->"+dateServer);
                            int caparator = dateServer.compareTo(start_date);
                            assertEquals(1, caparator);
                        }
                    }
                }

                assertEquals(100, itemsSize);
            }});
    }





    @Test
    public void testSyncWithContentType() {

        stack.syncContentType("session", new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {
                    itemsSize += syncStack.getItems().size();
                    counter = syncStack.getCount();

                    printLog("sync content type size  :"+syncStack.getItems().size());
                    printLog("sync content type count  :"+syncStack.getCount());
                    syncStack.getItems().forEach(item->{
                        printLog("content tyope item: --> "+item.toString());
                    });
                }
            }
        });

        assertEquals(100, itemsSize);
    }



    @Test
    public void testSyncWithLocale() {

        stack.syncLocale(Language.ENGLISH_UNITED_STATES, new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null) {
                    counter = syncStack.getCount();
                    ArrayList<JSONObject> items = syncStack.getItems();
                    String dataObject = null;
                    for (JSONObject object: items){
                        if (object.has("data"))
                            dataObject = object.optJSONObject("data").optString("locale");
                        printLog("locale dataObject: --> "+dataObject.toString());

                        if (!dataObject.isEmpty()) {
                            printLog("locale dataObject: --> "+dataObject.toString());
                            assertEquals("en-us", dataObject);
                        }
                    }

                    printLog("sync stack size  :"+syncStack.getItems().size());
                    printLog("sync stack count  :"+syncStack.getCount());
                    syncStack.getItems().forEach(item->{
                        printLog("item: --> "+item.toString());
                    });
                }
            }
        });

    }




    @Test
    public void testPublishType() {

        stack.syncPublishType(Stack.PublishType.entry_published, new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {
                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();

                    System.out.println("publish type==>"+counter);
                    syncStack.getItems().forEach(items->{
                        System.out.println("publish type items-->"+items.toString());
                    });
                }else {
                    // Error block
                    System.out.println("publish type error !");
                }
            }
        });

        assertEquals(100, itemsSize);
    }





    @Test
    public void testSyncWithAll() throws ParseException {

        Date start_date = sdf.parse("2018-10-10");
        stack.sync( "session", start_date, Language.ENGLISH_UNITED_STATES, Stack.PublishType.entry_published, new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null) {
                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();

                    System.out.println("stack with all type==>"+counter);
                    syncStack.getItems().forEach(items->{
                        System.out.println("sync with all type items-->"+items.toString());
                    });
                }

                assertEquals(100, itemsSize);
            }

        });
    }



    public String returnDateFromISOString(String isoDateString) {
        String[] dateFormate = isoDateString.split("T");
        return dateFormate[0];
    }



    private void printLog( String logMessage){
        System.out.println(logMessage);
    }
}
