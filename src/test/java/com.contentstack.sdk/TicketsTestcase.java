package com.contentstack.sdk;

import org.junit.Test;
import org.junit.runner.JUnitCore;

public class TicketsTestcase extends JUnitCore {


    public static final String STACK_API_KEY = "blt4c0468fe43dc5bdd";
    public static final String ACCESS_TOKEN = "csbb1543164d7a0684b5a0f87f";
    public static final String ENV = "staging";
    private Stack stack;


    public TicketsTestcase() throws Exception {
        stack = Contentstack.stack( STACK_API_KEY, ACCESS_TOKEN, ENV);
    }


    @Test
    public void TicketONE(){

        Query query = stack.contentType("help_center_topic").query();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {

                if (error == null) {
                    for (Entry entry: queryResult.getResultObjects()) {
                        System.out.println("Title: " + entry.getString("title"));
                    }
                } else {
                    System.out.println("Failed: " + error.getErrorMessage() + " " + error.getErrorCode());
                }
            }
        });
    }

}
