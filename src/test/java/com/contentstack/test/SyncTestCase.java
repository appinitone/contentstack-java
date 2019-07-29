package com.contentstack.test;

import com.contentstack.sdk.Error;
import com.contentstack.sdk.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import static org.junit.Assert.assertEquals;


public class SyncTestCase {

    private Stack stack;
    private int itemsSize = 0;
    private int counter = 0;
    private String dateISO = null;
    private int include_count = 0;
    private final Logger logger = LogManager.getLogger(SyncTestCase.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private void initLog() { BasicConfigurator.configure(); }

    public SyncTestCase() throws Exception {

        initLog();
        Config config = new Config();
        config.setHost("stag-cdn.contentstack.io");
        String prod_api_key = "blt477ba55f9a67bcdf";
        String prod_delivery_Token = "cs7731f03a2feef7713546fde5";
        String environment = "web";
        stack = Contentstack.stack(prod_api_key, prod_delivery_Token, environment, config);
    }


    @Test
    public void testSyncInit() {

        stack.sync(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null) {
                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();

                    logger.debug( "sync stack size  :"+syncStack.getItems().size());
                    logger.debug("sync stack count  :"+syncStack.getCount());
                    syncStack.getItems().forEach(item-> logger.debug(  item.toString()));

                    assertEquals(counter, syncStack.getCount());
                }

            }});


    }




    @Test
    public void testSyncToken() {
        stack.syncToken("bltbb61f31a70a572e6c9506a", new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null) {
                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();

                    logger.debug("sync token size  :"+syncStack.getItems().size());
                    logger.debug("sync token count  :"+syncStack.getCount());
                    syncStack.getItems().forEach(item-> logger.debug( "sync token result"+item.toString()));
                    assertEquals( itemsSize, syncStack.getItems().size());
                }
            }
        });


    }






    @Test
    public void testPaginationToken() {
        stack.syncPaginationToken("blt7f35951d259183fba680e1", new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null) {
                    itemsSize += syncStack.getItems().size();
                    counter = syncStack.getCount();

                    logger.debug("sync pagination size  :"+syncStack.getItems().size());
                    logger.debug("sync pagination count  :"+syncStack.getCount());
                    syncStack.getItems().forEach(item-> logger.debug( "Pagination"+item.toString()));
                    //assertEquals( itemsSize, itemsSize);
                }

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
                            logger.debug( "date iso -->"+dateISO);
                            String serverDate = returnDateFromISOString(dateISO);
                            Date dateServer = null;
                            try {
                                dateServer = sdf.parse(serverDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            logger.debug( "dateServer -->"+dateServer);
                            assert dateServer != null;
                            int caparator = dateServer.compareTo(start_date);
                            assertEquals(1, caparator);
                        }
                    }

                    assertEquals(itemsSize, syncStack.getItems().size());
                }


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

                    logger.debug("sync content type size  :"+syncStack.getItems().size());
                    logger.debug("sync content type count  :"+syncStack.getCount());
                    syncStack.getItems().forEach(item-> logger.debug( "content type: "+item.toString()));

                    //assertEquals(100, itemsSize);
                }
            }
        });


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
                        assert dataObject != null;
                        logger.debug("locale dataObject: --> "+ dataObject);

                        if (!dataObject.isEmpty()) {
                            logger.debug("locale dataObject: --> "+ dataObject);
                            assertEquals("en-us", dataObject);
                        }
                    }

                    logger.debug("sync stack size  :"+syncStack.getItems().size());
                    logger.debug("sync stack count  :"+syncStack.getCount());
                    syncStack.getItems().forEach(item-> logger.debug(item.toString()));
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

                    logger.debug( "publish type==>"+counter);
                    syncStack.getItems().forEach(items-> logger.debug( "publish type"+items.toString()));

                    assertEquals(itemsSize, syncStack.getItems().size());
                }else {
                    // Error block
                    logger.debug( "publish type error !");
                }

            }
        });

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

                    logger.debug( "stack with all type==>"+counter);
                    syncStack.getItems().forEach(items-> logger.debug(  "sync with all type: "+items.toString()));

                    assertEquals(itemsSize, syncStack.getItems().size());
                }

            }

        });
    }



    @Test
    public void getAllContentTypes() {

        stack.getContentTypes( new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                logger.debug( "getAllContentTypes reponse: "+ contentTypesModel.getResultArray());
            }
        });

    }



    @Test
    public void getSingleContentType() {

        ContentType  contentType = stack.contentType("schema");
        contentType.fetch(new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                if (error==null){
                    logger.debug( "single content:"+ contentTypesModel.getResponse());
                }else {
                    logger.debug( "Error"+ error.getErrorMessage());
                }
            }
        });

        //assertEquals(100, itemsSize);
    }


    private String returnDateFromISOString(String isoDateString) {
        String[] dateFormate = isoDateString.split("T");
        return dateFormate[0];
    }

}
