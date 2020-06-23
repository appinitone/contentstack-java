package com.contentstack.test;

import com.contentstack.sdk.Config;
import com.contentstack.sdk.Contentstack;
import com.contentstack.sdk.Stack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.JUnitCore;

import java.util.LinkedHashMap;
import java.util.concurrent.CountDownLatch;


public class ImageTransformTestcase extends JUnitCore {

    static final Logger logger = LogManager.getLogger(ImageTransformTestcase.class.getName());
    private static CountDownLatch latch;
    private static Stack stack;
    private final LinkedHashMap<String, Object> imageParams = new LinkedHashMap<String, Object>();
    private final String IMAGE_URL = "https://images.contentstack.io/v3/assets/blt903007d63561dea2/blt638399801b6bd23c/59afa6406c11eb860ddf04aa/download";


    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
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
    public void test_00_fetchAllImageTransformation() throws InterruptedException {

        imageParams.put("auto", "webp");
        imageParams.put("quality", 200);
        imageParams.put("width", 100);
        imageParams.put("height", 50);
        imageParams.put("format", "png");
        imageParams.put("crop", "3:5");
        imageParams.put("trim", "20,20,20,20");
        imageParams.put("disable", "upscale");
        imageParams.put("pad", "10,10,10,10");
        imageParams.put("bg-color", "#FFFFFF");
        imageParams.put("dpr", 20);
        imageParams.put("canvas", "3:5");
        imageParams.put("orient", "l");

        String image_url = stack.ImageTransform(IMAGE_URL, imageParams);
        int counter = 0;
        /* check url contains "?" */
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")) {
            String[] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String[] getAllPairs = rightUrl.split("\\&");
            counter = 0;
            if (imageParams.size() > 0) {
                for (int i = 0; i < imageParams.size(); i++) {
                    String keyValueParis = getAllPairs[i];
                    logger.info("pairs:--> " + keyValueParis);
                    ++counter;
                }
            }
        } else {
            logger.info("Testcases Failed");
            try {
                latch.await();
            } catch (Exception e) {
                logger.info("---------------||" + e.toString());
            }
        }

        if (counter == imageParams.size()) {
            latch.countDown();
            logger.info("Testcases Passed");
        } else {
            logger.info("Testcases Failed");
            try {
                latch.await();
            } catch (Exception e) {
                logger.info("---------------||" + e.toString());
            }
        }
    }


    @Test
    public void test_01_fetchAllImageTransformation() throws InterruptedException {

        imageParams.put("auto", "webp");
        imageParams.put("quality", 200);
        imageParams.put("width", 100);
        imageParams.put("height", 50);
        imageParams.put("format", "png");
        imageParams.put("crop", "3:5");

        String image_url = stack.ImageTransform(IMAGE_URL, imageParams);
        int counter = 0;
        /* check url contains "?" */
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")) {
            String[] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String[] getAllPairs = rightUrl.split("\\&");
            counter = 0;
            if (imageParams.size() > 0) {
                for (int i = 0; i < imageParams.size(); i++) {
                    String keyValueParis = getAllPairs[i];
                    logger.info("pairs:--> " + keyValueParis);
                    ++counter;
                }
            }
        } else {
            logger.info("Testcases Failed");
            try {
                latch.await();
            } catch (Exception e) {
                logger.info("---------------||" + e.toString());
            }
        }

        if (counter == imageParams.size()) {
            latch.countDown();
            logger.info("Testcases Passed");
        } else {
            logger.info("Testcases Failed");
            try {
                latch.await();
            } catch (Exception e) {
                logger.info("---------------||" + e.toString());
            }
        }
    }


    @Test
    public void test_02_fetchAllImageTransformation() throws InterruptedException {
        imageParams.put("trim", "20,20,20,20");
        imageParams.put("disable", "upscale");
        imageParams.put("pad", "10,10,10,10");
        imageParams.put("bg-color", "#FFFFFF");
        imageParams.put("dpr", 20);
        imageParams.put("canvas", "3:5");
        imageParams.put("orient", "l");
        String image_url = stack.ImageTransform(IMAGE_URL, imageParams);
        int counter = 0;
        /* check url contains "?" */
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")) {
            String[] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String[] getAllPairs = rightUrl.split("\\&");
            counter = 0;
            if (imageParams.size() > 0) {
                for (int i = 0; i < imageParams.size(); i++) {
                    String keyValueParis = getAllPairs[i];
                    logger.info("pairs:--> " + keyValueParis);
                    ++counter;
                }
            }
        } else {
            logger.info("Testcases Failed");
            try {
                latch.await();
            } catch (Exception e) {
                logger.info("---------------||" + e.toString());
            }
        }

        if (counter == imageParams.size()) {
            latch.countDown();
            logger.info("Testcases Passed");
        } else {
            logger.info("Testcases Failed");
            try {
                latch.await();
            } catch (Exception e) {
                logger.info("---------------||" + e.toString());
            }
        }
    }


    @Test
    public void test_03_fetchAllImageTransformation() throws InterruptedException {


        imageParams.put("trim", "20,20,20,20");
        imageParams.put("disable", "upscale");
        imageParams.put("canvas", "3:5");
        imageParams.put("orient", "l");

        String image_url = stack.ImageTransform(IMAGE_URL, imageParams);
        int counter = 0;
        /* check url contains "?" */
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")) {
            String[] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String[] getAllPairs = rightUrl.split("\\&");
            counter = 0;
            if (imageParams.size() > 0) {
                for (int i = 0; i < imageParams.size(); i++) {
                    String keyValueParis = getAllPairs[i];
                    logger.info("pairs:--> " + keyValueParis);
                    ++counter;
                }
            }
        } else {
            logger.info("Testcases Failed");
            try {
                latch.await();
            } catch (Exception e) {
                logger.info("---------------||" + e.toString());
            }
        }

        if (counter == imageParams.size()) {
            latch.countDown();
            logger.info("Testcases Passed");
        } else {
            logger.info("Testcases Failed");
            try {
                latch.await();
            } catch (Exception e) {
                logger.info("---------------||" + e.toString());
            }
        }
    }
}
