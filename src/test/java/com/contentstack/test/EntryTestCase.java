package com.contentstack.test;

import com.contentstack.sdk.Error;
import com.contentstack.sdk.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static junit.framework.TestCase.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntryTestCase {

    private static final Logger logger = LogManager.getLogger(EntryTestCase.class.getName());
    private static String entryUID;
    private static String content_type_uid;
    private static CountDownLatch latch;
    private static Stack stack;


    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        content_type_uid = "product";
        JSONObject credential = new ReadJsonFile().readCredentials();
        if (credential != null) {
            Config config = new Config();
            String  host = credential.optString("host");
            config.setHost(host);
            String DEFAULT_API_KEY = credential.optString("api_key");
            String DEFAULT_DELIVERY_TOKEN = credential.optString("delivery_token");
            String DEFAULT_ENV = credential.optString("environment");
            stack = Contentstack.stack(DEFAULT_API_KEY, DEFAULT_DELIVERY_TOKEN, DEFAULT_ENV, config);
        }
        latch = new CountDownLatch(1);
        logger.info("test started...");
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
        logger.info("When all the test cases of class finishes...");
        logger.info("Total testcase: " + latch.getCount());
    }

    /**
     * Sets up the test fixture.
     * (Called before every test case method.)
     */
    @Before
    public void setUp() {
        latch = new CountDownLatch(1);
    }


    /**
     * Tears down the test fixture.
     * (Called after every test case method.)
     */
    @After
    public void tearDown() {
        logger.info("Runs after every testcase completes.");
        latch.countDown();
    }


    @Test
    public void test_01_findAllEntries() {
        final Query query = stack.contentType(content_type_uid).query();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    entryUID = queryresult.getResultObjects().get(15).getUid();
                    latch.countDown();
                }
            }
        });
    }

    @Test
    public void test_02_only_fetch() {
        final Entry entry = stack.contentType(content_type_uid).entry(entryUID);
        entry.only(new String[]{"price"});
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    assertEquals(786, entry.toJSON().get("price"));
                }
            }
        });
    }

    @Test
    public void test_03_except_fetch() {
        final Entry entry = stack.contentType(content_type_uid).entry(entryUID);
        entry.except(new String[]{"title"});
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    assertFalse(entry.toJSON().has("title"));
                } else {
                    assertEquals(422, error.getErrorCode());
                }
            }
        });
    }

    @Test
    public void test_04_includeReference_fetch() {
        final Entry entry = stack.contentType(content_type_uid).entry(entryUID);
        entry.includeReference("category");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    JSONArray categoryArray = entry.getJSONArray("category");
                    categoryArray.forEach(object -> assertTrue(object.toString().contains("_content_type_uid")));
                }
            }
        });
    }

    @Test
    public void test_05_includeReferenceOnly_fetch() {
        final Entry entry = stack.contentType(content_type_uid).entry(entryUID);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("title");
        strings.add("orange");
        strings.add("mango");
        entry.onlyWithReferenceUid(strings, "category");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    assertEquals("laptop", entry.toJSON().getString("title"));
                }
            }
        });

    }


    @Test
    public void test_06_includeReferenceExcept_fetch() throws InterruptedException {
        final Entry entry = stack.contentType(content_type_uid).entry(entryUID);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("color");
        strings.add("price_in_usd");
        entry.exceptWithReferenceUid(strings, "category");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();

    }


    @Test
    public void test_07_getMarkdown_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry(entryUID);
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
    }


    @Test
    public void test_08_get() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry(entryUID);
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
    }


    @Test
    public void test_09_getParam() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry(entryUID);
        entry.addParam("include_dimensions", "true");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
    }


    @Test
    public void test_10_IncludeReferenceContentTypeUID() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry(entryUID);
        entry.includeReferenceContentTypeUID();
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    JSONObject jsonResult = entry.toJSON();
                    try {
                        JSONArray cartList = (JSONArray) jsonResult.get("cart");
                        Object whatTYPE = cartList.get(0);
                        if (whatTYPE instanceof JSONObject) {
                            assertTrue(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();

    }


    @Test
    public void test_11_Locale() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry(entryUID);
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    String checkResp = entry.getLocale();
                    logger.debug(checkResp);
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();
    }

    @Test
    public void test_12_entry_except() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry(entryUID);
        String[] allValues = {"color", "price_in_usd"};
        entry.except(allValues);
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    String checkResp = entry.getLocale();
                    logger.debug(checkResp);
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
    }

}
