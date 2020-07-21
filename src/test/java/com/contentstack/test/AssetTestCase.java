package com.contentstack.test;
import com.contentstack.sdk.Error;
import com.contentstack.sdk.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runners.MethodSorters;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import static junit.framework.TestCase.assertEquals;


// Run testcase for the particular class
// run mvn -Dtest=TestAssetTestCase test
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AssetTestCase {

    private static final Logger logger = LogManager.getLogger(AssetTestCase.class);
    private static String assetUid;
    private static Stack stack;
    private static CountDownLatch latch;
    private static String DEFAULT_HOST;


    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        JSONObject credential = new ReadJsonFile().readCredentials();
        if (credential != null) {
            Config config = new Config();
            String DEFAULT_API_KEY = credential.optString("api_key");
            String DEFAULT_DELIVERY_TOKEN = credential.optString("delivery_token");
            String DEFAULT_ENV = credential.optString("environment");
            DEFAULT_HOST = credential.optString("host");
            config.setHost(DEFAULT_HOST);
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


    @Test()
    public void test_A_getAllAssetsToSetAssetUID() {
        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                if (error == null) {
                    logger.info("response: " + assets.get(0).getAssetUid());
                    assetUid = assets.get(0).getAssetUid();
                }
            }
        });

    }

    @Test
    public void test_B_VerifyAssetUID() {

        final Asset asset = stack.asset(assetUid);
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    // Success Block.
                    logger.info("response: " + asset.getAssetUid());
                    assertEquals(assetUid, asset.getAssetUid());
                }
            }
        });
    }

    @Test
    public void test_C_Asset_fetch() {
        final Asset asset = stack.asset(assetUid);
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    assertEquals("blt5312f71416d6e2c8", asset.getAssetUid());
                    assertEquals("image/jpeg", asset.getFileType());
                    assertEquals("blt69a06b75160147adc2c8b3a9", asset.getCreatedBy());
                    assertEquals("sys_blt309cacf8ab432f62", asset.getUpdatedBy());
                    assertEquals("phoenix2.jpg", asset.getFileName());
                    assertEquals("482141", asset.getFileSize());
                    if (DEFAULT_HOST.equalsIgnoreCase("cdn.contentstack.io")){
                        assertEquals("https://images.contentstack.io/v3/assets/blt12c8ad610ff4ddc2/blt5312f71416d6e2c8/5704eda29ebb5cce3597b877/phoenix2.jpg", asset.getUrl());
                    }else {
                        assertEquals("https://stag-images.contentstack.io/v3/assets/blt12c8ad610ff4ddc2/blt5312f71416d6e2c8/5704eda29ebb5cce3597b877/phoenix2.jpg", asset.getUrl());
                    }

                } else {
                    assertEquals(404, error.getErrorCode());
                }
            }
        });
    }

    @Test
    public void test_D_AssetLibrary_fetch() {
        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                if (error == null) {
                    assets.forEach(asset -> {
                        logger.info("----Test--Asset-D--Success----" + asset.toJSON());
                        logger.info("----Test--Asset-D--Success----" + asset.getFileType());
                        logger.info("----Test--Asset-D--Success----" + asset.getCreatedBy());
                        logger.info("----Test--Asset-D--Success----" + asset.getUpdatedBy());
                        logger.info("----Test--Asset-D--Success----" + asset.getFileName());
                        logger.info("----Test--Asset-D--Success----" + asset.getFileSize());
                        logger.info("----Test--Asset-D--Success----" + asset.getAssetUid());
                        logger.info("----Test--Asset-D--Success----" + asset.getUrl());
                    });
                }
            }
        });
    }

    @Test
    public void test_E_AssetLibrary_includeCount_fetch() {
        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.includeCount();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                if (error == null) {
                    if(DEFAULT_HOST.equalsIgnoreCase("cdn.contentstack.io")){
                        assertEquals(14, assetLibrary.getCount());
                    }else {
                        assertEquals(20, assetLibrary.getCount());
                    }
                }
            }
        });
    }

    @Test
    public void test_F_AssetLibrary_includeRelativeUrl_fetch() {
        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.includeRelativeUrl();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                if (error == null) {
                    assertEquals("/v3/assets/blt12c8ad610ff4ddc2/blt5312f71416d6e2c8/5704eda29ebb5cce3597b877/phoenix2.jpg", assets.get(0).getUrl());
                }
            }
        });
    }

    @Test
    public void test_G_StackGetParams() {
        final Asset asset = stack.asset(assetUid);
        asset.addParam("key", "some_value");
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    logger.error(asset.getAssetUid());
                    assertEquals(assetUid, asset.getAssetUid());
                } else {
                    assertEquals(404, error.getErrorCode());
                }
            }
        });

    }

}
