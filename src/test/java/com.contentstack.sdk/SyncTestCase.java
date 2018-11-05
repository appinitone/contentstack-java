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
        stack = Contentstack.stack(prod_api_key, prod_delivery_Token, environment);
    }


    @Test
    public void test00_SyncInit() {

        stack.sync(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null) {
                    itemsSize += syncStack.getItems().size();
                    counter = syncStack.getCount();

                    System.out.println("syncStack.getItems().size()=============== "+syncStack.getItems().size());
                    System.out.println("Count==============="+syncStack.getCount());
                    syncStack.getItems().forEach(item->{
                        System.out.println("item: --> "+item);
                    });
                }

            }});

        assertEquals(itemsSize, counter);
    }




    @Test
    public void test03_sync_with_syncToken() {
        stack.syncToken("bltbb61f31a70a572e6c9506a", new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null) {
                    itemsSize += syncStack.getItems().size();
                    counter = syncStack.getCount();

                    System.out.println("sync token=============== "+syncStack.getItems().size());
                    System.out.println("sync count==============="+syncStack.getCount());
                    syncStack.getItems().forEach(item->{
                        System.out.println("item: --> "+item);
                    });
                }
            }
        });

        assertEquals( itemsSize, counter);
    }






    @Test
    public void testPaginationToken() {
        stack.syncPaginationToken("blt7f35951d259183fba680e1", new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null) {
                    itemsSize += syncStack.getItems().size();
                    counter = syncStack.getCount();

                    System.out.println("Pagination token=============== "+syncStack.getItems().size());
                    System.out.println("pagination count==============="+syncStack.getCount());
                    syncStack.getItems().forEach(item->{
                        System.out.println("item: --> "+item);
                    });
                }
                assertEquals( 7, itemsSize);
            }
        });

    }





    @Test
    public void test02__syncWithDate() throws ParseException {

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

                assertEquals(itemsSize, counter);
            }});
    }





    @Test
    public void test04_sync_with_contentType() {

        stack.syncContentType("session", new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {
                    itemsSize += syncStack.getItems().size();
                    counter = syncStack.getCount();

                    System.out.println("content type size============ "+syncStack.getItems().size());
                    System.out.println("content count========="+syncStack.getCount());
                    syncStack.getItems().forEach(item->{
                        System.out.println("item: --> "+item);
                    });
                }
            }
        });

        assertEquals(counter, itemsSize);
    }



    @Test
    public void test05_sync_with_locale() {

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
                        System.out.println("locale:===>"+dataObject);
                        if (!dataObject.isEmpty()) {
                            assertEquals("en-us", dataObject);
                        }
                    }
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
                    // Success block
                    counter = syncStack.getItems().size();
                    itemsSize = syncStack.getCount();

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

        assertEquals(counter, itemsSize);
    }





    @Test
    public void test06_sync_with_all() throws ParseException {

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

                assertEquals(counter, itemsSize);
            }

        });
    }




    public String convertUTCToISO(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        dateFormat.setTimeZone(tz);
        System.out.println("Date to ISO------>"+dateFormat.format(date));
        return dateFormat.format(date);
    }



    public String returnDateFromISOString(String isoDateString) {
        String[] dateFormate = isoDateString.split("T");
        return dateFormate[0];
    }

}
