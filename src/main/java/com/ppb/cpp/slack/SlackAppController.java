package com.ppb.cpp.slack;


import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackAppServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet("/slack/events")
public class SlackAppController extends SlackAppServlet {
    public SlackAppController(final App app) {
        super(app);
    }
}
